package com.example.kirayeka;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.Database.Database;
import com.example.kirayeka.DemoStructure.Wishlist;
import com.example.kirayeka.DemoStructure.RentReq;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kirayeka.Interface.ItemClickListener;import com.example.kirayeka.DemoStructure.Ritemz;
import com.example.kirayeka.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference itemList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String categoryId = "";

    FirebaseRecyclerAdapter<Ritemz, ProductViewHolder> searchAdapter;
    FirebaseRecyclerAdapter<Ritemz, ProductViewHolder> adapter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    Database localDB;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        //Firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference("Items");
        localDB=new Database(this);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getIntent()!=null)
                    categoryId=getIntent().getStringExtra("CategoryId");
                if(!categoryId.isEmpty()&&categoryId!=null)
                {
                    if(Checker.isConnectedToInterner(getBaseContext()))
                        loadListItem(categoryId);
                }
                else{
                    Toast.makeText(ItemList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(getIntent()!=null)
                    categoryId=getIntent().getStringExtra("CategoryId");
                if(!categoryId.isEmpty()&&categoryId!=null)
                {
                    if(Checker.isConnectedToInterner(getBaseContext()))
                        loadListItem(categoryId);
                }
                else{
                    Toast.makeText(ItemList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });

        recyclerView = findViewById(R.id.recycler_item);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent here
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        assert categoryId != null;
        if(!categoryId.isEmpty()){
            loadListItem(categoryId);
        }

        materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search Product");
//        loadSuggest();
        //      materialSearchBar.setLastSuggestions(suggestList);
        //    materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<String> suggest=new ArrayList<String>();
                for(String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        Query searchByName=itemList.orderByChild("Name").equalTo(text.toString());
        FirebaseRecyclerOptions<Ritemz> itemOptions=new FirebaseRecyclerOptions.Builder<Ritemz>()
                .setQuery(searchByName,Ritemz.class)
                .build();
       searchAdapter=new FirebaseRecyclerAdapter<Ritemz, ProductViewHolder>(itemOptions) {
           @Override
           protected void onBindViewHolder(@NonNull ProductViewHolder viewHolder, int position, @NonNull Ritemz model) {
               viewHolder.item_name.setText(model.getName());
               viewHolder.item_rent.setText(String.format("₹ %s",model.getRent()));
               Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.item_image);
               Ritemz local = model;
               viewHolder.setItemClickListener(new ItemClickListener() {
                   @Override
                   public void onClick(View view, int position, boolean isLongClick) {
                       //start new activity
                       Intent itemDetail =  new Intent(ItemList.this, RentProduct.class);
                       itemDetail.putExtra("ItemId", searchAdapter.getRef(position).getKey());
                       startActivity(itemDetail);

                   }
               });
           }

           @NonNull
           @Override
           public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View itemView=LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.rent_it,parent,false);
               return new ProductViewHolder(itemView);
           }
       };
       searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadListItem(String categoryId) {
        Query searchByName=itemList.orderByChild("mcId").equalTo(categoryId);
        FirebaseRecyclerOptions<Ritemz> itemOptions=new FirebaseRecyclerOptions.Builder<Ritemz>()
                .setQuery(searchByName,Ritemz.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Ritemz, ProductViewHolder>(itemOptions) {
            @Override
            protected void onBindViewHolder(final ProductViewHolder viewHolder, final int position, final Ritemz model) {
                viewHolder.item_name.setText(model.getName());
                viewHolder.item_rent.setText(String.format("₹ %s",model.getRent()));
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.item_image);
                if (new Database(getBaseContext()).checkItemExists(adapter.getRef(position).getKey(), Checker.currentUser.getPhone()))
                    viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Database(getBaseContext()).addToCart(new RentReq(
                                    Checker.currentUser.getPhone(),
                                    adapter.getRef(position).getKey(),
                                    model.getName(),
                                    "1",
                                    model.getRent(),
                                    model.getOff5(),
                                    model.getImage()

                            ));

                            Toast.makeText(ItemList.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                        }
                    });

                if (localDB.isFavorites(adapter.getRef(position).getKey(), Checker.currentUser.getPhone()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Wishlist favorites=new Wishlist();
                        favorites.setItemId(adapter.getRef(position).getKey());
                        favorites.setItemName(model.getName());
                        favorites.setItemDetails(model.getDetails());
                        favorites.setItemOff5(model.getOff5());
                        favorites.setItemImage(model.getImage());
                        favorites.setItemMenuId(model.getmcId());
                        favorites.setPhoneNo(Checker.currentUser.getPhone());
                        favorites.setItemRent(model.getRent());



                        if (!localDB.isFavorites(adapter.getRef(position).getKey(), Checker.currentUser.getPhone())) {
                            localDB.addToFavorites(favorites);
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ItemList.this, "" + model.getName() + " added to wishlist", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromFavorites(adapter.getRef(position).getKey(), Checker.currentUser.getPhone());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(ItemList.this, "" + model.getName() + " removed from wishlist", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
                Ritemz local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new activity
                        Intent itemDetail = new Intent(ItemList.this, RentProduct.class);
                        itemDetail.putExtra("ItemId", adapter.getRef(position).getKey());
                        startActivity(itemDetail);

                    }
                });
            }


        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rent_it, parent, false);
            return new ProductViewHolder(itemView);
        }

       };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

}
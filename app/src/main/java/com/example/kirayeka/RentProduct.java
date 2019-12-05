package com.example.kirayeka;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andremion.counterfab.CounterFab;
import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.Database.Database;import com.example.kirayeka.DemoStructure.Ritemz;
import com.example.kirayeka.DemoStructure.RentReq;
import com.example.kirayeka.ViewHolder.ProductViewHolder;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RentProduct extends AppCompatActivity {

    TextView item_name, item_rent, item_details;
    ImageView item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CounterFab btnCart;
    ElegantNumberButton numberButton;
    FirebaseDatabase database;
    DatabaseReference items;
    Ritemz currentItem;
    String itemId = "";
    FirebaseRecyclerAdapter<Ritemz, ProductViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_product);

        //Firebase
        database = FirebaseDatabase.getInstance();
        items = database.getReference("Items");

        //Init View
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (CounterFab) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new RentReq(
                        Checker.currentUser.getPhone(),
                        itemId,
                        currentItem.getName(),
                        numberButton.getNumber(),
                        currentItem.getRent(),
                        currentItem.getOff5(),
                        currentItem.getImage()
                ));

                Toast.makeText(RentProduct.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });

        btnCart.setCount(new Database(this).getCountCart(Checker.currentUser.getPhone()));

        item_details = (TextView) findViewById(R.id.item_details);
        item_name = (TextView) findViewById(R.id.item_name);
        item_rent = (TextView) findViewById(R.id.item_rent);
        item_image = (ImageView) findViewById(R.id.img_item);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get item ID from Intent
        if(getIntent() != null)
            itemId = getIntent().getStringExtra("ItemId");
        if(!itemId.isEmpty()){
            if(Checker.isConnectedToInterner(getBaseContext()))
            getDetailItem(itemId);
            else
            {
                Toast.makeText(RentProduct.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return;

            }
        }

    }

    private void getDetailItem(String itemId) {
        items.child(itemId).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                currentItem = dataSnapshot.getValue(Ritemz.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentItem.getImage()).into(item_image);

                collapsingToolbarLayout.setTitle(currentItem.getName());

                item_rent.setText(currentItem.getRent());

                item_name.setText(currentItem.getName());

                item_details.setText(currentItem.getDetails());

            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

    }
}

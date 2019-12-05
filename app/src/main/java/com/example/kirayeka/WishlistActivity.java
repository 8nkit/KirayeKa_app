package com.example.kirayeka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.Database.Database;
import com.example.kirayeka.Helper.RecyclerItemTouchHelper;
import com.example.kirayeka.Interface.RecyclerItemTouchHelperListener;
import com.example.kirayeka.DemoStructure.Wishlist;
import com.example.kirayeka.ViewHolder.WishlistAdapter;
import com.example.kirayeka.ViewHolder.WishlistViewHolder;
import com.google.android.material.snackbar.Snackbar;

public class WishlistActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    WishlistAdapter adapter;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        rootLayout=(RelativeLayout)findViewById(R.id.root_layout);
        recyclerView = findViewById(R.id.recycler_fav);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        loadFavorites();

    }

    private void loadFavorites() {
        adapter=new WishlistAdapter(this, new Database(this).getAllFavorites(Checker.currentUser.getPhone()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof WishlistViewHolder){
           final String name=((WishlistAdapter)recyclerView.getAdapter()).getItem(position).getItemName();
            final Wishlist deleteItem=((WishlistAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex=viewHolder.getAdapterPosition();
            adapter.removeItem(viewHolder.getAdapterPosition());
            new Database(getBaseContext()).removeFromFavorites(deleteItem.getItemId(), Checker.currentUser.getPhone());
            Snackbar snackbar=Snackbar.make(rootLayout,name+"removed from favorites ",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addToFavorites(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}

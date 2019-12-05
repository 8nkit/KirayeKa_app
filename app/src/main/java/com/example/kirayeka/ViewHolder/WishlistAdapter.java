package com.example.kirayeka.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.Database.Database;
import com.example.kirayeka.RentProduct;
import com.example.kirayeka.ItemList;
import com.example.kirayeka.Interface.ItemClickListener;import com.example.kirayeka.DemoStructure.Wishlist;import com.example.kirayeka.DemoStructure.Ritemz;
import com.example.kirayeka.DemoStructure.RentReq;
import com.example.kirayeka.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistViewHolder> {



    private Context context;
    private List<Wishlist> favoritesList;

    public WishlistAdapter(Context context, List<Wishlist> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context)
                .inflate(R.layout.rent_prod,parent,false);
        return new WishlistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder viewHolder, final int position) {
        viewHolder.item_name.setText(favoritesList.get(position).getItemName());
        viewHolder.item_rent.setText(String.format("Rs. %s",favoritesList.get(position).getItemRent().toString()));
        Picasso.with(context).load(favoritesList.get(position).getItemImage()).into(viewHolder.item_image);
      //  if (new Database(getBaseContext()).checkItemExists(adapter.getRef(position).getKey(), Checker.currentUser.getPhone()))
        viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExists = new Database(context).checkItemExists(favoritesList.get(position).getItemId(), Checker.currentUser.getPhone());
                if (!isExists) {
                    new Database(context).addToCart(new RentReq(
                            Checker.currentUser.getPhone(),
                            favoritesList.get(position).getItemId(),
                            favoritesList.get(position).getItemName(),
                            "1",
                            favoritesList.get(position).getItemRent(),
                            favoritesList.get(position).getItemOff5(),
                            favoritesList.get(position).getItemImage()


                    ));
                }/* else {
                        new Database(context).increaseCart(Checker.currentUser.getPhone(), favoritesList.get(position).getItemId());
                    }
*/
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        final Wishlist local = favoritesList.get(position);
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //start new activity
                Intent itemDetail = new Intent(context, RentProduct.class);
                itemDetail.putExtra("ItemId", favoritesList.get(position).getItemId());
                context.startActivity(itemDetail);

            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public void removeItem(int position){
        favoritesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Wishlist item,int position){
        favoritesList.add(position,item);
        notifyItemInserted(position);
    }

    public Wishlist getItem(int position){
        return favoritesList.get(position);
    }

}

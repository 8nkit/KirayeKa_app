package com.example.kirayeka.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kirayeka.Interface.ItemClickListener;
import com.example.kirayeka.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView item_name,item_rent;
    public ImageView item_image,fav_image,quick_cart;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public ProductViewHolder(View itemView) {
        super(itemView);

        item_name =(TextView)itemView.findViewById(R.id.item_name);
        item_image = (ImageView)itemView.findViewById(R.id.item_image);
        item_rent=(TextView)itemView.findViewById(R.id.item_rent);
        fav_image = (ImageView)itemView.findViewById(R.id.fav);
        quick_cart=(ImageView)itemView.findViewById(R.id.btn_quick_cart);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

}

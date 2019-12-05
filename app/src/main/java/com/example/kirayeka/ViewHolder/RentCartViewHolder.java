package com.example.kirayeka.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.Interface.ItemClickListener;
import com.example.kirayeka.R;

public class RentCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView txt_cart_name, txt_rent;
    public ImageView img_cart_count;
    public ImageView cart_image;
    public RelativeLayout view_background;
    public LinearLayout view_foreground;
    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public RentCartViewHolder(View itemView){
        super(itemView);
        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_rent = itemView.findViewById(R.id.cart_item_Rent);
        img_cart_count = itemView.findViewById(R.id.cart_item_count);
        cart_image=(ImageView)itemView.findViewById(R.id.cart_image);
        view_background=(RelativeLayout)itemView.findViewById(R.id.view_background);
        view_foreground=(LinearLayout)itemView.findViewById(R.id.view_foreground);
        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View view){

       // itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select action");
        contextMenu.add(0,0,getAdapterPosition(), Checker.DELETE);
    }


    //public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.setHeaderTitle(Checker.DELETE);

    //      menu.add(0,0,getAdapterPosition(),Checker.DELETE);
    //}

}


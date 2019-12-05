package com.example.kirayeka.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.kirayeka.RentCart;
import com.example.kirayeka.DemoStructure.RentReq;
import com.example.kirayeka.R;
import com.example.kirayeka.RentCart;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RentAdapter extends RecyclerView.Adapter<RentCartViewHolder>{

    private List<RentReq> listData = new ArrayList<>();
    private Context context;
    private RentCart cart;
    public RentAdapter(List<RentReq> listData, RentCart cart){
        this.listData = listData;
        this.cart = cart;
    }

    @Override
    public RentCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new RentCartViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RentCartViewHolder holder, int position) {
        Picasso.with(cart.getBaseContext())
                .load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);
        TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position).getDayss(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int rent = (Integer.parseInt(listData.get(position).getRent()))*(Integer.parseInt(listData.get(position).getDayss()));
        holder.txt_rent.setText(fmt.format(rent));
        holder.txt_cart_name.setText(listData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {

        return listData.size();
    }

    public RentReq getItem(int position){return listData.get(position);}
    //delete that getItem so that it can be same as Ankit 's new
    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(RentReq item,int position){
        listData.add(position,item);
        notifyItemInserted(position);
    }

}

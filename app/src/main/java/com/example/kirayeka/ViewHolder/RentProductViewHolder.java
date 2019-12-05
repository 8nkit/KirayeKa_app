package com.example.kirayeka.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kirayeka.Interface.ItemClickListener;
import com.example.kirayeka.R;


public class RentProductViewHolder extends RecyclerView.ViewHolder{

    public TextView txtOrderId, txtRentQuo, txtOrderphone, txtOrderAddress,txtOrderDate;

    private ItemClickListener itemClickListener;

    public RentProductViewHolder(View itemView){
        super(itemView);

        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtRentQuo = itemView.findViewById(R.id.order_status);
        txtOrderphone = itemView.findViewById(R.id.order_phone);
        txtOrderDate = itemView.findViewById(R.id.order_date);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

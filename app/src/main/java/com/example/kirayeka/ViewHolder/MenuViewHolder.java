package com.example.kirayeka.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kirayeka.Interface.ItemClickListener;
import com.example.kirayeka.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtMenuName;
        public ImageView imageView;

        private ItemClickListener itemClickListener;

        public MenuViewHolder(View itemView){
            super(itemView);

            txtMenuName = itemView.findViewById(R.id.menu_name);
            imageView = itemView.findViewById(R.id.menu_image);

            itemView.setOnClickListener(this);

        }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
        public void onClick(View view)
        {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
}

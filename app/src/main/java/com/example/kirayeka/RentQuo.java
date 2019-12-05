package com.example.kirayeka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kirayeka.Checker.Checker;import com.example.kirayeka.DemoStructure.Ritemz;
import com.example.kirayeka.DemoStructure.dataSource;
import com.example.kirayeka.ViewHolder.ProductViewHolder;
import com.example.kirayeka.ViewHolder.RentProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RentQuo extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;


    FirebaseDatabase database;
    DatabaseReference requests;
    FirebaseRecyclerAdapter<dataSource, RentProductViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_quo);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Rented");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Checker.currentUser.getPhone());

    }

    private void loadOrders(String phone) {
        Query getOrderByUser=requests.orderByChild("phone")
                .equalTo(phone);
        FirebaseRecyclerOptions<dataSource> orderOptions=new FirebaseRecyclerOptions.Builder<dataSource>()
                .setQuery(getOrderByUser,dataSource.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<dataSource, RentProductViewHolder>(orderOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RentProductViewHolder viewHolder, int position, @NonNull dataSource model) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtRentQuo.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderphone.setText(model.getPhone());
                viewHolder.txtOrderDate.setText(Checker.getDate(Long.parseLong(adapter.getRef(position).getKey())));
            }

            @NonNull
            @Override
            public RentProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout,parent,false);
                return new RentProductViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "Preparing";
        else if(status.equals("2"))
            return "Packaging";
        else
            return "Item Ready";
    }
}

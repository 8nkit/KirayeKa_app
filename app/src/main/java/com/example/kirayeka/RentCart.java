package com.example.kirayeka;

import android.app.AlertDialog;
import com.example.kirayeka.DemoStructure.RentReq;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.Database.Database;
import com.example.kirayeka.DemoStructure.Ritemz;
import com.example.kirayeka.Helper.RecyclerItemTouchHelper;
import com.example.kirayeka.Interface.RecyclerItemTouchHelperListener;import com.example.kirayeka.DemoStructure.Ritemz;
import com.example.kirayeka.DemoStructure.RentReq;
import com.example.kirayeka.DemoStructure.dataSource;
import com.example.kirayeka.ViewHolder.RentAdapter;
import com.example.kirayeka.ViewHolder.RentCartViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.kirayeka.RentCart.total;


public class RentCart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private RentAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference requests;
Database db;
    TextView txtTotalRent;
    Button btnPlace;
    float totalRent;

    List<RentReq> cart = new ArrayList<>();
//    RentAdapter adapter;

    //name the threads
    //name the variables in static, so they can be accessed and updated by the inventorylistthread
    static List<List<RentReq>> orderList = new ArrayList<>();
    static List<Ritemz> inventoryList = new ArrayList<>();
    //static Item inventory;
    static List<String> requestId  = new ArrayList<>();
    //The orderList is for inventoryList, the requestList is for the KitchenThread
    static List<dataSource> requestList = new ArrayList<>();
    static float total;

    //partial request flag
    private boolean partial = false;

    //unavailable item information
    static String unavailableitemnames="";
    static float unavailableitemrent=0;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_cart);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests =  database.getReference("Rented");

        //Init
        //RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
       layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        txtTotalRent = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update invetoryList immediately first

                //Create new Request
                if(cart.size()>0)
                showAlertDialog();
                else
                    Toast.makeText(RentCart.this, "Empty Cart", Toast.LENGTH_SHORT).show();



                //If user choose Partial order, then do showAlertDialog() again, and set the partial flag to true in order to set this request partially
                    /*showAlertDialog();
                    partial = true;*/

            }

        });


        //When the "Place Order" button clicked
        loadListItem();

    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RentCart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your address");
        final EditText edtAddress = new EditText(RentCart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataSource request = new dataSource(
                        Checker.currentUser.getPhone(),
                        Checker.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalRent.getText().toString(),
                        cart
                );

                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                new Database(getBaseContext()).cleanCart(Checker.currentUser.getPhone());

                Toast.makeText(RentCart.this, "Thank you, Rent Requested", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
alertDialog.show();

    }


    private void loadListItem() {
        cart = new Database(this).getCarts(Checker.currentUser.getPhone());
        orderList.add(cart);
        adapter = new RentAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calculate total rent
        total = 0;
        for(RentReq order:cart)
            total+=(float) (Integer.parseInt(order.getRent()))*(Integer.parseInt(order.getDayss()));
        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


        float tax= (float) (total*0.06);
        float profit = (float) (total*0.3);
        total+=tax+profit;

        totalRent =total;

        txtTotalRent.setText(fmt.format(total));

    }
@Override
    public boolean onContextItemSelected(MenuItem item){
       if(item.getTitle().equals(Checker.DELETE))
           deleteCart(item.getOrder());
        return true;
}

    private void deleteCart(int position) {
        cart.remove(position);
        new Database(this).cleanCart(Checker.currentUser.getPhone());
        for(RentReq item:cart)
            new Database(this).addToCart(item);

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof RentCartViewHolder){
            String name= ((RentAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
            final RentReq deleteItem=((RentAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex=viewHolder.getAdapterPosition();
            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(),Checker.currentUser.getPhone());
            int total=0;
            List<RentReq>  orders=new Database(getBaseContext()).getCarts(Checker.currentUser.getPhone());
            for(RentReq item:orders)
                total+=(Integer.parseInt(item.getRent()))*(Integer.parseInt(item.getDayss()));
            Locale locale=new Locale("en","IN");
            NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
            txtTotalRent.setText(fmt.format(total));


            Snackbar snackbar=Snackbar.make(rootLayout,name+"Item Removed ",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               adapter.restoreItem(deleteItem,deleteIndex);
               new Database(getBaseContext()).addToCart(deleteItem);
                    int total=0;
                    List<RentReq>  orders=new Database(getBaseContext()).getCarts(Checker.currentUser.getPhone());
                    for(RentReq item:orders)
                        total+=(Integer.parseInt(item.getRent()))*(Integer.parseInt(item.getDayss()));
                    Locale locale=new Locale("en","IN");
                    NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
                    txtTotalRent.setText(fmt.format(total));
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}

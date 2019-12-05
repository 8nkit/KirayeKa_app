package com.example.kirayeka;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.kirayeka.Database.Database;
import com.example.kirayeka.DemoStructure.OfferAD;
import com.example.kirayeka.DemoStructure.RentReq;
import com.example.kirayeka.ViewHolder.MenuViewHolder;
import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.Interface.ItemClickListener;
import com.example.kirayeka.DemoStructure.Grade;
import com.example.kirayeka.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean doubleBackToExitPressedOnce = false;

    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Grade, MenuViewHolder> adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    CounterFab fab;
    HashMap<String,String> image_list;
    SliderLayout mSlider;
    ImageButton bu1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addListenerOnButton();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Checker.isConnectedToInterner(getBaseContext())){
                    loadMenu();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(Checker.isConnectedToInterner(getBaseContext())){
                    loadMenu();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Menu1");


        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this,RentCart.class);
                startActivity(cartIntent);

            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Set Name for user
        View headerView = navigationView.getHeaderView(0);
       // Checker.currentUser.setName("Ram");
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Checker.currentUser.getName());

        //Load menu
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
        //      recycler_menu.setLayoutManager(layoutManager);
        recycler_menu.setLayoutManager(new GridLayoutManager(this,2));
        if(Checker.isConnectedToInterner(this))
            loadMenu();
        else
        {
            Toast.makeText(Home.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;

        }


        setupSlider();
    }

    private void addListenerOnButton() {
        bu1=(ImageButton)findViewById(R.id.bu11);
        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this,SearchActivity.class);
                startActivity(intent);

            }
        });

    }

    private void setupSlider() {
        mSlider =(SliderLayout)findViewById(R.id.slider);
        image_list=new HashMap<>();
        final DatabaseReference offerad=database.getReference("OfferAD");
        offerad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    OfferAD offerad=postSnapShot.getValue(OfferAD.class);
                    image_list.put(offerad.getName()+"@@@"+offerad.getId(),offerad.getImage());
                }
                for(String key:image_list.keySet())
                {
                    String[] keySplit=key.split("@@@");
                    String nameOfItem=keySplit[0];
                    String idOfItem=keySplit[1];
                    final TextSliderView textSliderView=new TextSliderView(getBaseContext());
                    textSliderView
                            .description(nameOfItem)
                            .image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(Home.this,RentProduct.class);
                                    intent.putExtras(textSliderView.getBundle());
                                    startActivity(intent);
                                }
                            });
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("ItemId",idOfItem);
                    mSlider.addSlider(textSliderView);
                    offerad.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(5000);

    }
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
        fab.setCount(new Database(this).getCountCart(Checker.currentUser.getPhone()));
        if(adapter!=null)
            adapter.startListening();
    }


    private void loadMenu() {
        FirebaseRecyclerOptions<Grade> options=new FirebaseRecyclerOptions.Builder<Grade>()
                .setQuery(category,Grade.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Grade, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, int position, @NonNull Grade model) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                final Grade clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get CategoryId and send to new Activity
                        Intent itemList = new Intent(Home.this, ItemList.class);
                        //Because CategoryId is key, so we just get the key of this item
                        itemList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(itemList);
                    }
                });

            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item,parent,false);
                return  new MenuViewHolder(itemView);
            }
        };
        adapter.startListening();
        recycler_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click back again to exit", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.menu_search)
            Toast.makeText(this, "Search working fine", Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            return true;
        }else if(id == R.id.nav_cart){
            Intent cartIntent = new Intent(Home.this, RentCart.class);
            startActivity(cartIntent);

        }else if(id == R.id.nav_orders){
            Intent orderIntent = new Intent(Home.this, RentQuo.class);
            startActivity(orderIntent);

        }else if(id == R.id.nav_log_out){
            Intent signIn = new Intent(Home.this, SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(signIn);
        }
        else if (id==R.id.nav_change_pwd){
            pwdchngedialog();
        }

        else if (id==R.id.nav_favorites){
            startActivity(new Intent(Home.this,WishlistActivity.class));
        }
        else if(id==R.id.nav_ret_req){
            startActivity(new Intent(Home.this,ReturnItem.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void pwdchngedialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Change Password");
        alertDialog.setMessage("Please fill all information ");
        LayoutInflater inflater=LayoutInflater.from(this);
        View layout_pwd=inflater.inflate(R.layout.change_password_layout,null);
        final MaterialEditText edtps=(MaterialEditText)layout_pwd.findViewById(R.id.edtps);
        final MaterialEditText edtnps=(MaterialEditText)layout_pwd.findViewById(R.id.edtnps);
        final MaterialEditText edtrnps=(MaterialEditText)layout_pwd.findViewById(R.id.edtrnps);
        alertDialog.setView(layout_pwd);
        alertDialog.setPositiveButton("Change ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final android.app.AlertDialog waitingDialog=new SpotsDialog(Home.this);
                waitingDialog.show();
                if(edtps.getText().toString().equals(Checker.currentUser.getPassword())){
                    if(edtnps.getText().toString().equals(edtrnps.getText().toString()))
                    {
                        Map<String,Object> pwdupdate=new HashMap<>();
                        pwdupdate.put("Password",edtnps.getText().toString());
                        DatabaseReference user=FirebaseDatabase.getInstance().getReference("User");
                        user.child(Checker.currentUser.getPhone()).updateChildren(pwdupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                waitingDialog.dismiss();
                                Toast.makeText(Home.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        waitingDialog.dismiss();
                        Toast.makeText(Home.this, "New Password not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    waitingDialog.dismiss();
                    Toast.makeText(Home.this, "Incorrect Old Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

}
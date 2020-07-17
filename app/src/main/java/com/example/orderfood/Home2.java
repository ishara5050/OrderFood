package com.example.orderfood;

import android.content.Intent;
import android.os.Bundle;

import com.example.orderfood.Common.Common;
import com.example.orderfood.Interface.ItemClickListner;
import com.example.orderfood.Model.Calls;
import com.example.orderfood.Model.Catagory;
import com.example.orderfood.Model.EmployeeRating;
import com.example.orderfood.Model.Rating;
import com.example.orderfood.Service.ListenOrder;
import com.example.orderfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class Home2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener {

    FirebaseDatabase database;
    DatabaseReference catagory;
    DatabaseReference call;
    DatabaseReference empratingTbl;
    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Catagory, MenuViewHolder> adapter;
    FloatingActionButton fabcall;
    FloatingActionButton btneEmpRating;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        database=FirebaseDatabase.getInstance();
        catagory=database.getReference("Catagory");
        call=database.getReference("Calls");
        empratingTbl=database.getReference("EmployeeRating");
        recycler_menu=(RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        fabcall=(FloatingActionButton)findViewById(R.id.fabcall);
        btneEmpRating=(FloatingActionButton)findViewById(R.id.fabemprating);


        fabcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calls calls=new Calls(Common.currentUser.getName()); // get current user
                call.child(String.valueOf(System.currentTimeMillis())) //set new child in Calls
                        .setValue(calls);

                Toast.makeText(Home2.this, "Please Wait..", Toast.LENGTH_SHORT).show();


            }
        });

        btneEmpRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmpRatingDialog();
            }
        });



        loadMenu();

      Intent service=new Intent(Home2.this, ListenOrder.class);
        startService(service);

//import service class eke comment eka ain karanna


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
               Intent cartIntent=new Intent(Home2.this,Cart.class);
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
        txtFullName = headerView.findViewById(R.id.txtFulName);
        txtFullName.setText(Common.currentUser.getName());
    }

    private void showEmpRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Rate Waiter")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Bad Service","Not Friendly","Normal","Good Service","Excellent Service"))
                .setDefaultRating(1)
                .setTitle("Rate Employee")
                .setDescription("Please select rating")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Add Waiter ID Here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(Home2.this)
                .show();
    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Catagory, MenuViewHolder>(Catagory.class,R.layout.menu_item,MenuViewHolder.class,catagory) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Catagory catagory, int i) {

                menuViewHolder.txtMenuName.setText(catagory.getName());
                Picasso.with(getBaseContext()).load(catagory.getImage())
                        .into(menuViewHolder.imageView);

                final Catagory clickItem = catagory;
                menuViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(Home.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        Intent foodList=new Intent(Home2.this,FoodList.class);
                        String CatagoryID=adapter.getRef(position).getKey().toString();
                        //Log.i(TAG,""+CatagoryID);
                        //foodList.putExtra("CatagoryId",adapter.getRef(position).getKey());
                        foodList.putExtra("CatagoryId",CatagoryID);

                        //Log.i(TAG,"A : "+adapter.getRef(position).getKey().toString());


                        startActivity(foodList);

                    }
                });
            }
        };

        recycler_menu.setAdapter(adapter);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
            //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            Intent cartIntent=new Intent(Home2.this,Cart.class);
            startActivity(cartIntent);

        }else if(id == R.id.nav_orders){
            //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            Intent OrderIntent=new Intent(Home2.this,OrderStatus.class);
            startActivity(OrderIntent);

        }else if(id == R.id.nav_logout){
            //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            Intent signin=new Intent(Home2.this,SignIn.class);
            signin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signin);
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String empID) {

        final EmployeeRating rating = new EmployeeRating(Common.currentUser.getPhone(),
                empID,

                String.valueOf(value)
                );
        empratingTbl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                empratingTbl.child(UUID.randomUUID().toString()).setValue(rating);




                Toast.makeText(Home2.this, "Thank You for the feedback !!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

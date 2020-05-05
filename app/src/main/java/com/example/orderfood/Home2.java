package com.example.orderfood;

import android.content.Intent;
import android.os.Bundle;

import com.example.orderfood.Common.Common;
import com.example.orderfood.Interface.ItemClickListner;
import com.example.orderfood.Model.Catagory;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Home2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseDatabase database;
    DatabaseReference catagory;
    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Catagory, MenuViewHolder> adapter;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        database=FirebaseDatabase.getInstance();
        catagory=database.getReference("Catagory");
        recycler_menu=(RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);



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



}

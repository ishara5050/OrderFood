package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.orderfood.Common.Common;
import com.example.orderfood.Interface.ItemClickListner;
import com.example.orderfood.Model.Calls;
import com.example.orderfood.Model.Food;
import com.example.orderfood.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FoodList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference foodList;
    DatabaseReference call;
    public static final String TAG="test";
    CoordinatorLayout rootLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton cartbutton;
    FloatingActionButton fabcall;


    String catagoryId="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");
        call=database.getReference("Calls");
        cartbutton=(FloatingActionButton) findViewById(R.id.fabfoods);
        fabcall=(FloatingActionButton)findViewById(R.id.fabcall);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout=(CoordinatorLayout) findViewById(R.id.rootLayout);

        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent cartIntent=new Intent(FoodList.this,Cart.class);
                startActivity(cartIntent);

            }
        });

        fabcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calls calls=new Calls(Common.currentUser.getName()); // get current user
                call.child(String.valueOf(System.currentTimeMillis())) //set new child in Calls
                        .setValue(calls);

                Toast.makeText(FoodList.this, "Please Wait..", Toast.LENGTH_SHORT).show();


            }
        });




       calendar=Calendar.getInstance();
        //SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:MM:SS");
        //String dateTime=simpleDateFormat.format(calendar.getTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >=18  && hour < 22)
        {
            Log.i(TAG,"FoodList : Time between 18-22");
        }
        else
        {
            Log.i(TAG,"FoodList : Time between 9-17"); // 16/04/2020 Stop --> set this condition to load dinner and lunch items
        }




        //pause



        if(getIntent() != null)
            catagoryId = getIntent().getStringExtra("CatagoryId");

        //catagoryId = getIntent().getStringExtra("CatagoryId");
        //catagoryId ="1";
        //Log.i(TAG,"FoodList : "+catagoryId);
        if(!catagoryId.isEmpty() && catagoryId !=null){

            loadListFood(catagoryId);
        }


    }

    private void loadListFood(String catagoryId) {
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                //foodList.orderByChild("menuId").equalTo(catagoryId) //Food list load wenne na MenuId-->menuId
                foodList.orderByChild("menuId_default_status").equalTo(catagoryId+"_All_Yes")


        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodViewHolder.food_image);

                final Food local=food;
                foodViewHolder.setItemClickListener(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(FoodList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        String FoodID=adapter.getRef(position).getKey().toString();
                        foodDetail.putExtra("FoodId",FoodID);
                        startActivity(foodDetail);


                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);
    }



}


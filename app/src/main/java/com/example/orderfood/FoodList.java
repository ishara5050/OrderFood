package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.orderfood.Interface.ItemClickListner;
import com.example.orderfood.Model.Food;
import com.example.orderfood.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FoodList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference foodList;
    public static final String TAG="test";
    RelativeLayout rootLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String catagoryId="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");


        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);

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
                foodList.orderByChild("menuId").equalTo(catagoryId) //Food list load wenne na MenuId-->menuId


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

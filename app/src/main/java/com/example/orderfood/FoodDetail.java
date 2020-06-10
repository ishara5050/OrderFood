package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.orderfood.Common.Common;
import com.example.orderfood.Database.Database;
import com.example.orderfood.Model.Calls;
import com.example.orderfood.Model.Food;
import com.example.orderfood.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";

    FirebaseDatabase database;
    DatabaseReference food;
    DatabaseReference call;
    Food currentFood;

    FloatingActionButton cartbutton;
    FloatingActionButton fabcall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database=FirebaseDatabase.getInstance();
        food=database.getReference("Food");
        call=database.getReference("Calls");

        numberButton=(ElegantNumberButton)findViewById(R.id.number_button);
        btnCart=(FloatingActionButton)findViewById(R.id.btnCart);
        cartbutton=(FloatingActionButton) findViewById(R.id.fabfoods);
        fabcall=(FloatingActionButton)findViewById(R.id.fabcall);

        //add to cart Function

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()


                ));

                Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent cartIntent=new Intent(FoodDetail.this,Cart.class);
                startActivity(cartIntent);

            }
        });

        fabcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calls calls=new Calls(Common.currentUser.getName()); // get current user
                call.child(String.valueOf(System.currentTimeMillis())) //set new child in Calls
                        .setValue(calls);

                Toast.makeText(FoodDetail.this, "Please Wait..", Toast.LENGTH_SHORT).show();


            }
        });

        food_description=(TextView)findViewById(R.id.food_description);
        food_name=(TextView)findViewById(R.id.food_name);
        food_price=(TextView)findViewById(R.id.food_price);
        food_image=(ImageView)findViewById(R.id.img_food);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if(getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty() && foodId !=null)
        {
            getFoodDetail(foodId);
        }


    }

    private void getFoodDetail(String foodId) {

        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood=dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);

                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

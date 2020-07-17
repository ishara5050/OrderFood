package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.orderfood.Interface.ItemClickListner;
import com.example.orderfood.Model.Catagory;
import com.example.orderfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import android.util.Log;
public class Home extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference catagory;
    public static final String TAG="test";

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Catagory,MenuViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database=FirebaseDatabase.getInstance();
        catagory=database.getReference("Catagory");

        recycler_menu=(RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);


        loadMenu();





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
                            Intent foodList=new Intent(Home.this,FoodList.class);
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
}

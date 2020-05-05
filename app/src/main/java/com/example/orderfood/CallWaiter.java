package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.orderfood.Common.Common;
import com.example.orderfood.Model.Calls;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CallWaiter extends AppCompatActivity {

    Button btncall;
    Button btnonline;
    FirebaseDatabase database;
    DatabaseReference call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_waiter);

        database=FirebaseDatabase.getInstance();
        call=database.getReference("Calls");

        btncall=(Button)findViewById(R.id.btnCall);
        btnonline=(Button)findViewById(R.id.btnOnline);

        btnonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent=new Intent(CallWaiter.this,Home2.class); //go to menu
                startActivity(homeIntent);
                finish();
            }
        });



        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calls calls=new Calls(Common.currentUser.getName()); // get current user
                call.child(String.valueOf(System.currentTimeMillis())) //set new child in Calls
                        .setValue(calls);

                Toast.makeText(CallWaiter.this, "Please Wait..", Toast.LENGTH_SHORT).show();


            }
        });

    }
}

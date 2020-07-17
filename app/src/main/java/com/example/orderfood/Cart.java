package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.Common.Common;
import com.example.orderfood.Common.Config;
import com.example.orderfood.Database.Database;
import com.example.orderfood.Helper.RecyclerItemTouchHelper;
import com.example.orderfood.Interface.RecyclerItemTouchHelperListner;
import com.example.orderfood.Model.Order;
import com.example.orderfood.Model.Request;
import com.example.orderfood.ViewHolder.CartAdapter;
import com.example.orderfood.ViewHolder.CartViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.apprating.AppRatingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListner {

    private static final int PAYPAL_REQUEST_CODE =9999 ;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    FButton btnPlace;
    TextView txtTotalPrice;

    List<Order> cart= new ArrayList<>();
    CartAdapter adapter;

    //paypal payment

    static PayPalConfiguration config=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    String address,comment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent=new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);



        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        //swip to remove
        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnPlace=(FButton)findViewById(R.id.btnPlaceOrder);
        txtTotalPrice=(TextView)findViewById(R.id.total);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.size()>0)
                showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your Cart is empty", Toast.LENGTH_SHORT).show();
            }
        });


        LoadListFood();



    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step..");
        alertDialog.setMessage("Enter your address");

        LayoutInflater inflator = this.getLayoutInflater();
        View order_address_comment = inflator.inflate(R.layout.order_address_comment,null);

        final MaterialEditText edtAddress = (MaterialEditText)order_address_comment.findViewById(R.id.edtAddress);
        final MaterialEditText edtComment = (MaterialEditText)order_address_comment.findViewById(R.id.edtComment);


        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //show paypal to payment


                address=edtAddress.getText().toString();
                comment=edtComment.getText().toString();

                String formatAmount =txtTotalPrice.getText().toString()
                        .replace("$","")
                        .replace(",","");

                PayPalPayment payPalPayment =new PayPalPayment(new BigDecimal(formatAmount),
                        "USD",
                        "Bon Cafe Order",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent=new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
                startActivityForResult(intent,PAYPAL_REQUEST_CODE);


//                Request request=new Request(
//                        Common.currentUser.getPhone(),
//                        Common.currentUser.getName(),
//                        edtAddress.getText().toString(),
//                        txtTotalPrice.getText().toString(),
//                        "0",
//                        edtComment.getText().toString(),
//                        cart
//                );
//                //********SUBMIT FIREBASE***********
//                requests.child(String.valueOf(System.currentTimeMillis()))
//                        .setValue(request);
//
//                new Database(getBaseContext()).cleanCart();
//                Toast.makeText(Cart.this, "Thank You. Order Placed !!", Toast.LENGTH_SHORT).show();
//                finish();


            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetail);

                        Request request = new Request(
                                Common.currentUser.getPhone(),
                                Common.currentUser.getName(),
                                address,
                                txtTotalPrice.getText().toString(),
                                "0",
                                "0",
                                comment,
                                jsonObject.getJSONObject("response").getString("state"),
                                cart
                        );
                        //********SUBMIT FIREBASE***********
                        requests.child(String.valueOf(System.currentTimeMillis()))
                                .setValue(request);

                        new Database(getBaseContext()).cleanCart();
                        Toast.makeText(Cart.this, "Thank You. Order Placed !!", Toast.LENGTH_SHORT).show();
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (requestCode== Activity.RESULT_CANCELED)
            {
                Toast.makeText(this, "Payment Cancaled", Toast.LENGTH_SHORT).show();
            }
            else if (requestCode==PaymentActivity.RESULT_EXTRAS_INVALID)
            {
                Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void LoadListFood() {

        cart=new Database(this).getCarts();
        adapter=new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        int total=0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);   //this works

        txtTotalPrice.setText(fmt.format(total));
//        txtTotalPrice.setText("Rs. "+total);



    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder)
        {
            String name=((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
            Order deleteItem =((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            int deleteIndex =viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId());

            int total=0;
            for (Order order:cart)
                total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

            Locale locale=new Locale("en","US");
            NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);   //this works

            txtTotalPrice.setText(fmt.format(total));
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new Database(this).cleanCart();
        for (Order item:cart)
            new Database(this).addToCart(item);

        LoadListFood();
    }
}

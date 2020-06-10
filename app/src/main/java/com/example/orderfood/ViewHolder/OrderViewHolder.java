package com.example.orderfood.ViewHolder;

import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.Interface.ItemClickListner;
import com.example.orderfood.R;


public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress,txtOrdetTime;
    private ItemClickListner itemClickListner;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress=(TextView)itemView.findViewById(R.id.order_address);
        txtOrderId=(TextView)itemView.findViewById(R.id.order_id);
        txtOrderPhone=(TextView)itemView.findViewById(R.id.order_phone);
        txtOrderStatus=(TextView)itemView.findViewById(R.id.order_status);
        txtOrdetTime=(TextView)itemView.findViewById(R.id.order_time) ;

        itemView.setOnClickListener(this);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {

        itemClickListner.onClick(v,getAdapterPosition(),false);




    }
}

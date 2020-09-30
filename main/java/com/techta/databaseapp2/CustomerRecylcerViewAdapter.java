package com.techta.databaseapp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerRecylcerViewAdapter extends RecyclerView.Adapter<CustomerRecylcerViewAdapter.ViewHolder>{

    private ArrayList<CustomerModel> customers;
    private Context context;

    public CustomerRecylcerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cm_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.customerName.setText(customers.get(position).getName());
        holder.customerAge.setText(customers.get(position).getAge());
        holder.customerAge.setText(customers.get(position).getAge());
        holder.customerInfoCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, customers.get(position).getName() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView customerName, customerAge, customerID;
        private CardView customerInfoCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customerAge = itemView.findViewById(R.id.customerAge);
            customerID = itemView.findViewById(R.id.customerID);
            customerName = itemView.findViewById(R.id.customerName);

        }
    }
}
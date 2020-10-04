package com.techta.databaseapp2;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
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

public class CustomerRecyclerViewAdapter extends RecyclerView.Adapter<CustomerRecyclerViewAdapter.ViewHolder>{

    private ArrayList<CustomerModel> customers;
    private Context context;

    public CustomerRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cm_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.customerName.setText(customers.get(position).getName());
        holder.customerAge.setText("Age: " + customers.get(position).getAge());
        holder.customerID.setText("ID: " + customers.get(position).getId());
        if (customers.get(position).isActive()) {
            holder.isActive.setText("Active");
        } else {
            holder.isActive.setText("Not Active");
        }
        holder.customerInfoCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerIsActive;

                if (customers.get(position).isActive()) {
                    customerIsActive = "Active";
                } else {
                    customerIsActive = "Not Active";
                }

                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String text = customers.get(position).getName() +
                        "\n" + "Age: " + customers.get(position).getAge() +
                        "\n" + "ID: " + customers.get(position).getId() +
                        "\n" + customerIsActive;
                ClipData clip = ClipData.newPlainText("customerInfo", text);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Customer Info copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCustomers(ArrayList<CustomerModel> customers) {
        this.customers = customers;
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView customerName, customerAge, customerID, isActive;
        private CardView customerInfoCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customerInfoCV = itemView.findViewById(R.id.customerCV);
            customerAge = itemView.findViewById(R.id.customerAge);
            customerID = itemView.findViewById(R.id.customerID);
            customerName = itemView.findViewById(R.id.customerName);
            isActive = itemView.findViewById(R.id.customerIsActive);

        }
    }

}
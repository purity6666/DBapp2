package com.techta.databaseapp2;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerRecyclerViewAdapter extends RecyclerView.Adapter<CustomerRecyclerViewAdapter.ViewHolder>{

    private ArrayList<CustomerModel> customers;
    private Context context;
    private Button button;

    public CustomerRecyclerViewAdapter(Context context, Button button) {
        this.button = button;
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
        holder.customerPurchased.setText("Purchased Goods: \n" + customers.get(position).getPurchasedGoods());
        holder.customerID.setText("ID: " + customers.get(position).getId());
        holder.isActive.setText(checkIfActive(position));

        holder.customerInfoCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int x = 0;

                CustomerModel customerModel = customers.get(holder.getAdapterPosition());
                customerModel.setSelected(!customerModel.isSelected());
                if (customerModel.isSelected()) {
                    button.setVisibility(View.VISIBLE);
                    holder.customerInfoCV.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardViewSelected));
                }

                for (CustomerModel customerModel1 : customers) {
                    if (customerModel1.isSelected()) {
                        x++;
                    }
                }

                if (x == 0) {
                    button.setVisibility(View.GONE);
                } else {
                    button.setVisibility(View.VISIBLE);
                }
            }
        });


        holder.customerInfoCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                String customerFormat =
                        customers.get(position).getName() +
                        "\n" + "Purchased Goods: " + customers.get(position).getPurchasedGoods() +
                        "\n" + "ID: " + customers.get(position).getId() +
                        "\n" + checkIfActive(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog)
                        .setCancelable(true)
                        .setTitle("Customer info")
                        .setMessage(customerFormat)
                        .setIcon(R.drawable.ic_person)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                String text =
                                        customers.get(position).getName() +
                                        "\n" + "Purchased Goods: " + customers.get(position).getPurchasedGoods() +
                                        "\n" + "ID: " + customers.get(position).getId() +
                                        "\n" + checkIfActive(position);

                                ClipData clip = ClipData.newPlainText("customerInfo", text);
                                clipboard.setPrimaryClip(clip);

                                Toast.makeText(context, "Customer Info copied to Clipboard", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();

                return true;
            }
        });
    }

    private String checkIfActive(int position) {

        String customerIsActive;

        if (customers.get(position).isActive()) {
            customerIsActive = "Active";
        } else {
            customerIsActive = "Not Active";
        }

        return customerIsActive;
    }

    public void setCustomers(ArrayList<CustomerModel> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView customerName, customerPurchased, customerID, isActive;
        private CardView customerInfoCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customerPurchased = itemView.findViewById(R.id.purchasedGoodsText);
            customerInfoCV = itemView.findViewById(R.id.customerCV);
            customerID = itemView.findViewById(R.id.customerID);
            customerName = itemView.findViewById(R.id.customerName);
            isActive = itemView.findViewById(R.id.customerIsActive);

        }
    }

}
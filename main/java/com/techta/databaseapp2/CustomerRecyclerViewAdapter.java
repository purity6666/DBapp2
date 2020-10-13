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
    private TextView textView;

    public CustomerRecyclerViewAdapter(Context context, Button button, TextView textView) {
        this.button = button;
        this.context = context;
        this.textView = textView;
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
        holder.dateAdded.setText(customers.get(position).getDateAdded());
        holder.customerName.setText(customers.get(position).getName());
        holder.customerID.setText("ID: " + customers.get(position).getId());
        holder.isActive.setText(checkIfActive(position));

        holder.customerInfoCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int selectedCount = 0;

                CustomerModel customerModel = customers.get(holder.getAdapterPosition());
                customerModel.setSelected(!customerModel.isSelected());

                if (customerModel.isSelected()) {
                    button.setVisibility(View.VISIBLE);
                    holder.customerInfoCV.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardViewSelected));
                } else if (!customerModel.isSelected()) {
                    holder.customerInfoCV.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardViewBackgroundColor));
                }

                for (CustomerModel customerModel1 : customers) {
                    if (customerModel1.isSelected()) {
                        selectedCount++;
                    }
                }

                if (selectedCount == 0) {
                    button.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                } else {
                    button.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(selectedCount + " selected");
                }
            }
        });


        holder.customerInfoCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                String customerFormat =
                        "\n" + "ID: " + customers.get(position).getId() +
                        "\n" + "Name: " + customers.get(position).getName() +
                        "\n" + "Purchased Goods: " + customers.get(position).getPurchasedGoods() +
                        "\n" + "Date Added: " + customers.get(position).getDateAdded() +
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
                                        "\n" + "ID: " + customers.get(position).getId() +
                                        "\n" + "Name: " + customers.get(position).getName() +
                                        "\n" + "Purchased Goods: " + customers.get(position).getPurchasedGoods() +
                                        "\n" + "Date Added: " + customers.get(position).getDateAdded() +
                                        "\n" + checkIfActive(position);

                                ClipData clip = ClipData.newPlainText("customerInfo", text);
                                clipboard.setPrimaryClip(clip);

                                Toast.makeText(context, "Customer Info copied to Clipboard", Toast.LENGTH_SHORT).show();
                            }
                        });

                final AlertDialog dialog = builder.create();

                if (dialog.getWindow() != null)
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

                dialog.show();
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

        private TextView customerName, customerID, isActive, dateAdded;
        private CardView customerInfoCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateAdded = itemView.findViewById(R.id.dateAdded);
            customerInfoCV = itemView.findViewById(R.id.customerCV);
            customerID = itemView.findViewById(R.id.customerID);
            customerName = itemView.findViewById(R.id.customerName);
            isActive = itemView.findViewById(R.id.customerIsActive);

        }
    }

}
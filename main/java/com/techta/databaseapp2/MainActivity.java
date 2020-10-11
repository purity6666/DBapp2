package com.techta.databaseapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<CustomerModel> customers;
    private RecyclerView recyclerView;
    private Button addBtn, getCustomerCountBtn, deleteAllBtn, deleteSelectedButton;
    private EditText customerNameET, customerPGET;
    private CheckBox isActiveCheck;
    private Animation btnClick;
    private CustomerRecyclerViewAdapter adapter;
    private DatabaseHelper databaseHelper;
    private TextView customerCount, customerActiveCount;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView
        recyclerView = findViewById(R.id.itemRV);

        //Buttons
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);
        getCustomerCountBtn = findViewById(R.id.getCustomerCountButton);
        getCustomerCountBtn.setOnClickListener(this);
        deleteAllBtn = findViewById(R.id.deleteAllBtn);
        deleteAllBtn.setOnClickListener(this);
        deleteSelectedButton = findViewById(R.id.deleteSelected);
        deleteSelectedButton.setOnClickListener(this);


        //EditTexts
        customerNameET = findViewById(R.id.nameEdit);
        customerPGET = findViewById(R.id.purchasedGoodsEdit);

        //Switch
        isActiveCheck = findViewById(R.id.checkCustomer);

        //Animation
        btnClick = AnimationUtils.loadAnimation(this, R.anim.button_click);

        //TextView
        customerCount = findViewById(R.id.customerCount);
        customerActiveCount = findViewById(R.id.activeCustomerCount);

        databaseHelper = new DatabaseHelper(this);

        customers = databaseHelper.getEveryone();

        showRecyclerView(this);

        if (customers.size() == 0) {
            deleteAllBtn.setVisibility(View.GONE);
        } else {
            deleteAllBtn.setVisibility(View.VISIBLE);
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                CustomerModel customerModel = customers.get(viewHolder.getAdapterPosition());

                Toast.makeText(MainActivity.this, customerModel.getName() + " removed", Toast.LENGTH_SHORT).show();

                customers.remove(viewHolder.getAdapterPosition());
                databaseHelper.deleteItem(customerModel);

                showRecyclerView(getApplicationContext());

                customerCountTV();
                customerActiveCountTV();

                adapter.notifyDataSetChanged();

                if (customers.size() == 0) {
                    deleteAllBtn.setVisibility(View.GONE);
                }
            }

        }).attachToRecyclerView(recyclerView);

        customerCountTV();
        customerActiveCountTV();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBtn:
                addBtn.startAnimation(btnClick);

                if (customers.size() == 0) {
                    deleteAllBtn.setVisibility(View.VISIBLE);
                }

                CustomerModel customerModel;

                //check if input is empty
                try {
                    customerModel = new CustomerModel(-1, customerNameET.getText().toString(), customerPGET.getText().toString(), isActiveCheck.isChecked());
                    Toast.makeText(this, customerModel.getName() + " successfully added as customer", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Input invalid", Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1, "error", "error", false);
                }

                //if it doesn't result in an error it gets added into the database
                if (!customerModel.getName().equals("error")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    databaseHelper.addItem(customerModel);
                }

                customerPGET.getText().clear();
                customerNameET.getText().clear();

                customers = databaseHelper.getEveryone();

                showRecyclerView(this);

                customerCountTV();
                customerActiveCountTV();

                deleteSelectedButton.setVisibility(View.GONE);

                break;
            case R.id.getCustomerCountButton:
                getCustomerCountBtn.startAnimation(btnClick);

                //check if the list is null and then returns a Toast
                if (customers.size() != 0) {
                    Toast.makeText(this, "Customer Count: " + customers.size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "There are no customers", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.deleteAllBtn:
                deleteAllBtn.startAnimation(btnClick);

                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog)
                        .setCancelable(true)
                        .setTitle("Delete all")
                        .setMessage("Are you sure you want to delete everything?")
                        .setIcon(R.drawable.ic_delete)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //calling the function that deletes everything
                                databaseHelper.deleteEverything();
                                customers = databaseHelper.getEveryone();

                                Toast.makeText(getApplicationContext(), "All customers deleted", Toast.LENGTH_SHORT).show();

                                showRecyclerView(getApplicationContext());

                                customerCountTV();
                                customerActiveCountTV();

                                deleteAllBtn.setVisibility(View.GONE);
                            }
                        });

                builder.show();
                break;

            case R.id.deleteSelected:
                deleteSelectedButton.startAnimation(btnClick);

                for (CustomerModel customerModel1 : customers) {
                    if (customerModel1.isSelected()) {
                        databaseHelper.deleteItem(customerModel1);
                    }
                }

                customers = databaseHelper.getEveryone();

                if (customers.size() == 0) {
                    deleteAllBtn.setVisibility(View.GONE);
                }

                deleteSelectedButton.setVisibility(View.GONE);

                showRecyclerView(this);

                customerCountTV();
                customerActiveCountTV();

                Toast.makeText(this, "Deleted selected items", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRecyclerView(Context context) {
        adapter = new CustomerRecyclerViewAdapter(context, deleteSelectedButton);
        adapter.setCustomers(customers);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    @SuppressLint("SetTextI18n")
    private void customerActiveCountTV() {
        int activeCustomers = 0;

        if (customers.size() == 0) {
            customerActiveCount.setVisibility(View.GONE);
        } else {
            customerActiveCount.setVisibility(View.VISIBLE);
            for (CustomerModel customerModel : customers) {
                if (customerModel.isActive()) {
                    activeCustomers++;
                }
            }
            if (activeCustomers == 0) {
                customerActiveCount.setText("No active customers...");
            } else if (activeCustomers == 1) {
                customerActiveCount.setText("1 Active Customer");
            } else {
                customerActiveCount.setText(activeCustomers + " Active Customers");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void customerCountTV() {
        if (customers.size() > 1) {
            customerCount.setText(customers.size() + " Customers");
        } else if (customers.size() == 1) {
            customerCount.setText("1 Customer");
        } else {
            customerCount.setText("No customers...");
        }
    }
}
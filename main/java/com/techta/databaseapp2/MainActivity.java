package com.techta.databaseapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<CustomerModel> customers;
    private RecyclerView recyclerView;
    private Button addBtn, getCustomerCountBtn;
    private EditText customerNameET, customerAgeET;
    private SwitchCompat isActiveSwitch;
    private Animation btnClick;
    private CustomerRecyclerViewAdapter adapter;
    private DatabaseHelper databaseHelper;


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

        //EditTexts
        customerNameET = findViewById(R.id.nameEdit);
        customerAgeET = findViewById(R.id.ageEdit);

        //Switch
        isActiveSwitch = findViewById(R.id.switchCustomer);

        //Animation
        btnClick = AnimationUtils.loadAnimation(this, R.anim.button_click);

        databaseHelper = new DatabaseHelper(this);

        customers = databaseHelper.getEveryone();

        adapter = new CustomerRecyclerViewAdapter(this);
        adapter.setCustomers(customers);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem();
            }
        }).attachToRecyclerView(recyclerView);

    }

    private void removeItem() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        CustomerModel customerModel = customers.get(position);
        databaseHelper.deleteItem(customerModel);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBtn:
                addBtn.startAnimation(btnClick);

                CustomerModel customerModel;

                //check if input is empty
                try {
                    customerModel = new CustomerModel(-1, customerNameET.getText().toString(), Integer.parseInt(customerAgeET.getText().toString()), isActiveSwitch.isChecked());
                    Toast.makeText(this, customerModel.getName() + " successfully added as customer", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Input invalid", Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1, "error", 0, false);
                }

                //if it doesn't result in an error it gets added into the database
                if (!customerModel.getName().equals("error")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    databaseHelper.addItem(customerModel);
                }

                customerAgeET.getText().clear();
                customerNameET.getText().clear();

                customers = databaseHelper.getEveryone();

                adapter = new CustomerRecyclerViewAdapter(this);
                adapter.setCustomers(customers);

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                break;
            case R.id.getCustomerCountButton:
                getCustomerCountBtn.startAnimation(btnClick);


                //check if the list is null and then returns a Toast
                if (customers != null) {
                    Toast.makeText(this, "Customer Count: " + customers.size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "There are no customers", Toast.LENGTH_SHORT).show();
                }
        }
    }


}
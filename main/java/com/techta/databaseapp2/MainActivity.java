package com.techta.databaseapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<CustomerModel> customers = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button addBtn, viewAllBtn;
    private EditText customerNameET, customerAgeET;
    private SwitchCompat isActiveSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView
        recyclerView = findViewById(R.id.itemRV);

        //Buttons
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);
        viewAllBtn = findViewById(R.id.viewAllBtn);
        viewAllBtn.setOnClickListener(this);

        //EditTexts
        customerNameET = findViewById(R.id.nameEdit);
        customerAgeET = findViewById(R.id.ageEdit);

        //Switch
        isActiveSwitch = findViewById(R.id.switchCustomer);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBtn:
                CustomerModel customerModel;

                try {
                    customerModel = new CustomerModel(-1, customerNameET.getText().toString(), Integer.parseInt(customerAgeET.getText().toString()), isActiveSwitch.isChecked());
                    Toast.makeText(this, customerModel.getName() + " successfully added as customer", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Input invalid", Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1, "error", 0, false);
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.addItem(customerModel);
                break;
            case R.id.viewAllBtn:

                DatabaseHelper databaseHelper1 = new DatabaseHelper(getApplicationContext());
                ArrayList<CustomerModel> customerModels = databaseHelper1.getEveryone();

                if (customerModels != null) {
                    Toast.makeText(this, "Customer Count: " + customerModels.size(), Toast.LENGTH_SHORT).show();

                    CustomerRecyclerViewAdapter adapter = new CustomerRecyclerViewAdapter(this);
                    adapter.setCustomers(customerModels);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                } else {
                    Toast.makeText(this, "There are no customers", Toast.LENGTH_SHORT).show();
                }



        }
    }
}
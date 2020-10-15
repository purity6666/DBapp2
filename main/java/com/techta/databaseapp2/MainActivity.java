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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<CustomerModel> customers;
    private ArrayList<CustomerModel> customers2;
    private RecyclerView recyclerView;
    private Button addBtn, getCustomerCountBtn, deleteAllBtn, deleteSelectedButton;
    private EditText customerNameET, customerPGET;
    private CheckBox isActiveCheck;
    private Animation btnClick;
    private CustomerRecyclerViewAdapter adapter;
    private DatabaseHelper databaseHelper;
    private TextView customerCount, customerActiveCount, selectedCount;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

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
        selectedCount = findViewById(R.id.countSelected);

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

                showToast(customerModel.getName() + " Removed", R.drawable.ic_person);

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

                Calendar calendar = Calendar.getInstance();
                String calendarFormat = DateFormat.getDateInstance().format(calendar.getTime());

                CustomerModel customerModel = new CustomerModel(-1, customerNameET.getText().toString(), customerPGET.getText().toString(), isActiveCheck.isChecked(), calendarFormat);

                if (customerModel.getName().matches("") || customerModel.getPurchasedGoods().matches("")) {

                    showToast("Invalid Input", R.drawable.ic_close);

                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    databaseHelper.addItem(customerModel);

                    showToast(customerNameET.getText().toString() + " Successfully Added", R.drawable.ic_check);

                    customerPGET.getText().clear();
                    customerNameET.getText().clear();

                    customers = databaseHelper.getEveryone();

                    showRecyclerView(this);

                    customerCountTV();
                    customerActiveCountTV();

                    deleteSelectedButton.setVisibility(View.GONE);

                    if (deleteAllBtn.getVisibility() == View.GONE) {
                        deleteAllBtn.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.getCustomerCountButton:
                getCustomerCountBtn.startAnimation(btnClick);

                //check if the list is null and then returns a Toast
                if (customers.size() != 0) {
                    showToast("Customer Count: " + customers.size(), R.drawable.ic_person);
                } else {
                    showToast("There are no Customers", R.drawable.ic_person);
                }
                break;
            case R.id.deleteAllBtn:
                deleteAllBtn.startAnimation(btnClick);

                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog)
                        .setCancelable(true)
                        .setTitle("Delete all")
                        .setMessage("Are you sure you want to delete everything?")
                        .setIcon(R.drawable.ic_invalid)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //calling the function that deletes everything
                                databaseHelper.deleteEverything();
                                customers.clear();

                                showToast("All Customers Deleted", R.drawable.ic_delete);

                                showRecyclerView(getApplicationContext());

                                customerCountTV();
                                customerActiveCountTV();

                                deleteAllBtn.setVisibility(View.GONE);
                                selectedCount.setVisibility(View.GONE);
                                deleteSelectedButton.setVisibility(View.GONE);
                            }
                        });

                final AlertDialog dialog = builder.create();

                if (dialog.getWindow() != null)
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

                dialog.show();
                break;
            case R.id.deleteSelected:
                deleteSelectedButton.startAnimation(btnClick);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog)
                        .setCancelable(true)
                        .setTitle("Delete Selected Items")
                        .setMessage("Are you sure you want to delete selected items?")
                        .setIcon(R.drawable.ic_invalid)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

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

                                showRecyclerView(getApplicationContext());

                                customerCountTV();
                                customerActiveCountTV();

                                showToast("Deleted Selected Items", R.drawable.ic_delete);

                                selectedCount.setVisibility(View.GONE);
                            }
                        });

                final AlertDialog dialog1 = builder1.create();

                if (dialog1.getWindow() != null)
                    dialog1.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

                dialog1.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortAlphabet:
                Collections.sort(customers, new Comparator<CustomerModel>() {
                    @Override
                    public int compare(CustomerModel customerModel, CustomerModel t1) {
                        return customerModel.getName().compareTo(t1.getName());
                    }
                });

                showRecyclerView(getApplicationContext());
                break;
            case R.id.sortID:
                Collections.sort(customers, new Comparator<CustomerModel>() {
                    @Override
                    public int compare(CustomerModel customerModel, CustomerModel t1) {
                        return Integer.valueOf(customerModel.getId()).compareTo(Integer.valueOf(t1.getId()));
                    }
                });

                showRecyclerView(getApplicationContext());
                break;
            case R.id.sortActive:
                customers2 = new ArrayList<>();

                for (CustomerModel customerModel : customers) {
                    if (customerModel.isActive()) {
                        customers2.add(customerModel);
                    }
                }

                adapter.setCustomers(customers2);
                showToast(customers2.size() + " Active Customers", R.drawable.ic_person);

                break;
            case R.id.sortNonActive:
                customers2 = new ArrayList<>();

                for (CustomerModel customerModel : customers) {
                    if (!customerModel.isActive()) {
                        customers2.add(customerModel);
                    }
                }

                adapter.setCustomers(customers2);
                showToast(customers2.size() + " Non-Active Customers", R.drawable.ic_person);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRecyclerView(Context context) {
        adapter = new CustomerRecyclerViewAdapter(context, deleteSelectedButton, selectedCount);
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

    private void showToast(String toastString, int resId) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toastRoot));

        TextView toastText = layout.findViewById(R.id.toastText);
        ImageView toastImage =  layout.findViewById(R.id.toastImage);

        toastText.setText(toastString);
        toastImage.setImageResource(resId);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);

        toast.show();
    }
}
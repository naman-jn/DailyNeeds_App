package com.njain.dailyneeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.njain.dailyneeds.adapter.RecyclerViewAdapter;
import com.njain.dailyneeds.data.DatabaseHandler;
import com.njain.dailyneeds.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText itemName;
    private EditText itemQty;
    private EditText itemDescription;
    private Dialog infoDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);

        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        //Get items from db
        itemList = databaseHandler.getAllItems();

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

          fab = findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialog();
                saveButton.setEnabled(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.app_info){
            MainActivity.appInfo(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void createPopupDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_item, null);

        itemName = view.findViewById(R.id.itemName);
        itemQty = view.findViewById(R.id.itemQty);
        itemDescription = view.findViewById(R.id.itemDescription);

        saveButton = view.findViewById(R.id.saveButton);

        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!itemName.getText().toString().isEmpty()
                        && !itemQty.getText().toString().isEmpty()) {
                    saveItem(v);
                    saveButton.setEnabled(false);
                }else {
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });

    }

    private void saveItem(View view) {

        Item newItem=new Item();

        newItem.setName(itemName.getText().toString());
        newItem.setQty(Integer.parseInt(itemQty.getText().toString()));
        newItem.setDescription((itemDescription.getText().toString()));
        databaseHandler.addItem(newItem);

        Snackbar.make(view, "Item Saved", Snackbar.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to be run
                alertDialog.dismiss();
                //Todo: move to next screen - details screen

                startActivity(new Intent(ListActivity.this, MainActivity.class));
                finish();

            }
        }, 700);
    }

    private void shareAllItem(){

    }


}
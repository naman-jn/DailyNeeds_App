package com.njain.dailyneeds;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.njain.dailyneeds.data.DatabaseHandler;
import com.njain.dailyneeds.model.Item;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static Dialog infoDialog;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText itemName;
    private EditText itemQty;
    private EditText itemDescription;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler=new DatabaseHandler(this);

        byPassActivity();

        List<Item> items = databaseHandler.getAllItems();
        for (Item item : items) {
            Log.d("Main", "onCreate: " + item);
        }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }

    private void byPassActivity() {
        if (databaseHandler.getItemsCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_item, null);

        itemName = view.findViewById(R.id.itemName);
        itemQty = view.findViewById(R.id.itemQty);
        itemDescription = view.findViewById(R.id.itemDescription);

        saveButton = view.findViewById(R.id.saveButton);

        builder.setView(view);
        dialog=builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!itemName.getText().toString().isEmpty()
                        && !itemQty.getText().toString().isEmpty()) {
                    saveItem(v);
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

        Log.d("MainActivity","On Save: "+newItem);
        Snackbar.make(view, "Item Saved", Snackbar.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to be run
                dialog.dismiss();
                //Todo: move to next screen - details screen
                startActivity(new Intent(MainActivity.this, ListActivity.class));

            }
        }, 700);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_info) {
            appInfo(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void appInfo(Context context) {
        infoDialog=new Dialog(context);
        infoDialog.setContentView(R.layout.popup_info);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.show();
    }
}
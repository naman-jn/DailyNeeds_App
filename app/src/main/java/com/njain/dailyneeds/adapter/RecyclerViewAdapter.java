package com.njain.dailyneeds.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.njain.dailyneeds.ListActivity;
import com.njain.dailyneeds.MainActivity;
import com.njain.dailyneeds.R;
import com.njain.dailyneeds.data.DatabaseHandler;
import com.njain.dailyneeds.model.Item;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private DatabaseHandler databaseHandler;


    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row,viewGroup,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Item item=itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.qty.setText(String.valueOf(item.getQty()));
        holder.description.setText(item.getDescription());
        holder.date.setText(item.getDateItemAdded());
        databaseHandler = new DatabaseHandler(context);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView qty;
        public TextView description;
        public TextView date;
        public Button edit;
        public Button delete;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;

            itemName=itemView.findViewById(R.id.name);
            qty=itemView.findViewById(R.id.quantity);
            description=itemView.findViewById((R.id.description));
            date=itemView.findViewById(R.id.date);

            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position;
            position = getAdapterPosition();
            Item item = itemList.get(position);

            switch (v.getId()) {
                case R.id.edit:
                    //edit item
                    editItem(item);
                    break;
                case R.id.delete:
                    //delete item
                    deleteItem(item.getId());
                    break;
            }
        }





    private void deleteItem(final int id) {

        builder = new AlertDialog.Builder(context);

        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.confirmation_pop, null);

        Button noButton = view.findViewById(R.id.conf_no_button);
        Button yesButton = view.findViewById(R.id.conf_yes_button);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler db = new DatabaseHandler(context);
                db.deleteItem(id);
                itemList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                dialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    private void editItem(final Item newItem) {

        builder = new AlertDialog.Builder(context);
        inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.popup, null);

        Button saveButton;
        final EditText itemName;
        final EditText itemQty;
        final EditText itemDescription;
        TextView title;

        itemName = view.findViewById(R.id.itemName);
        itemQty = view.findViewById(R.id.itemQty);
        itemDescription = view.findViewById(R.id.itemDescription);
        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setText(R.string.update_text);
        title = view.findViewById(R.id.title);

        title.setText(R.string.edit_item);
        itemName.setText(newItem.getName());
        itemQty.setText(String.valueOf(newItem.getQty()));

        itemDescription.setText(newItem.getDescription());


        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update our item
                DatabaseHandler databaseHandler = new DatabaseHandler(context);

                //update items
                newItem.setName(itemName.getText().toString());
                newItem.setQty(Integer.parseInt(itemQty.getText().toString()));
                newItem.setDescription(itemDescription.getText().toString());

                if (!itemName.getText().toString().isEmpty()
                        && !itemQty.getText().toString().isEmpty()) {

                    databaseHandler.updateItem(newItem);
                    notifyItemChanged(getAdapterPosition(),newItem); //important!


                }else {
                    Snackbar.make(view, "Fields Empty",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }

                dialog.dismiss();

            }
        });
    }
    }
}

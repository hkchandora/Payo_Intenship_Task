package com.himanshu.payo.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.himanshu.payo.R;
import com.himanshu.payo.model.Data;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Data> listItems;
    private Context context;

    public DataAdapter(List<Data> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Data model = listItems.get(position);

        holder.name.setText(model.getFirst_name() + " " + model.getLast_name());
        holder.email.setText(model.getEmail());
        Glide.with(context).load(model.getAvatar()).into(holder.profile);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Vibrator vibrator = (Vibrator) holder.itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(40);

                AlertDialog.Builder alertBox = new AlertDialog.Builder(v.getRootView().getContext());
                alertBox.setTitle("Delete Item");
                alertBox.setMessage("Are you sure you want to delete " + model.getFirst_name() + "'s data?");
                alertBox.setNegativeButton("Cancel", null);
                alertBox.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listItems.size());
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                alertBox.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView name, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.item_list_profile);
            name = itemView.findViewById(R.id.item_list_name);
            email = itemView.findViewById(R.id.item_list_email);
        }
    }
}

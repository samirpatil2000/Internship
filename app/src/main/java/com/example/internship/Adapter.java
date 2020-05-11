package com.example.internship;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    ListActivity listActivity;
    List<Model> modelList;
    Context gContext;

    public Adapter(ListActivity listActivity, List<Model> modelList, Context gContext) {
        this.listActivity = listActivity;
        this.modelList = modelList;
        this.gContext = gContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String title = modelList.get(position).getTitle();
                String desc = modelList.get(position).getDesc();

                Toast.makeText(listActivity,title,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
                String[] options ={"Edit","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            // update
                            // get data
                            String id = modelList.get(position).getId();
                            String title = modelList.get(position).getTitle();
                            String desc = modelList.get(position).getDesc();
                            String address=modelList.get(position).getAddress();
                            Integer timestamp = (Integer) modelList.get(position).getTimeStamp();

                            Intent intent= new Intent(listActivity,MainActivity.class);
                            intent.putExtra("putId",id);
                            intent.putExtra("putTitle",title);
                            intent.putExtra("putDesc",desc);
                            intent.putExtra("putAddress",address);
                            intent.putExtra("putTimestamp",timestamp);

                            listActivity.startActivity(intent);
                        }
                        else if(which==1){
                            // delete is clicked
                            listActivity.deleteData(position);
                        }
                    }
                }).create().show();

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(modelList.get(position).getTitle());
        holder.desc.setText(modelList.get(position).getDesc());
        holder.address.setText(modelList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}

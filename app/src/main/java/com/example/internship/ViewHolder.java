package com.example.internship;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView title,desc;
    View view;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        view =itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gClickListener.onItemClick(v,getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                gClickListener.onItemLongClick(v,getAdapterPosition());
                return true;
            }
        });

        // init view with
        title = itemView.findViewById(R.id.titleTv);
        desc=itemView.findViewById(R.id.descTv);
    }
    private ViewHolder.ClickListener gClickListener;

    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view , int position);

    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        gClickListener=clickListener;

    }
}

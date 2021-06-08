package com.example.myrun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrun.model.Run;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Run> mList;
    Context context;

    public MyAdapter(Context context, ArrayList<Run> mList){
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Run model = mList.get(position);
        holder.distance.setText(model.getDistance());
        holder.note.setText(model.getNote());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView distance , note;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            distance = itemView.findViewById(R.id.distanceText);
            note = itemView.findViewById(R.id.noteText);
        }
    }
}

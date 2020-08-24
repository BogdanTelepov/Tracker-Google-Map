package com.inventory.traсker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inventory.traсker.data.Path;
import com.inventory.treaker.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<Path> pathList;

    public RecyclerAdapter() {
        pathList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_path, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Path currentPath = pathList.get(position);
        try {
            holder.textView_time.setText(String.valueOf(currentPath.getTime()));
            holder.textView_speed.setText(String.valueOf(currentPath.getSpeed()));
            holder.textView_length.setText(String.valueOf(currentPath.getLength()));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_length;
        private TextView textView_time;
        private TextView textView_speed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_length = itemView.findViewById(R.id.list_runLength);
            textView_speed = itemView.findViewById(R.id.list_runSpeed);
            textView_time = itemView.findViewById(R.id.list_runTime);
        }
    }
}

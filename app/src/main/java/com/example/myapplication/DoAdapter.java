package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoAdapter extends RecyclerView.Adapter<DoAdapter.ViewHolder> {

    private final PopupDialogClickListener clickListener;
    private List<DoModel> data;
    
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public Button btnDelete;
        public RelativeLayout relativeLayout;
        public TextView tanggal,waktu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTask);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            tanggal = itemView.findViewById(R.id.dateTitle);
            waktu = itemView.findViewById(R.id.timeTitle);
        }
    }

    public DoAdapter(List<DoModel> data,PopupDialogClickListener clickListener){
        this.data = data;
        this.clickListener =clickListener;

    }

    public void updateAndRefreshData(List<DoModel> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View theView = layoutInflater.inflate(R.layout.task_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(theView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DoModel selectedTask = data.get(position);
        holder.tvName.setText(selectedTask.getName());
        holder.tanggal.setText(selectedTask.getDate());
        holder.waktu.setText(selectedTask.getTime());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            clickListener.onDelete(position);
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onUpdate(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        else return data.size();
    }
    public void removeItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }
}
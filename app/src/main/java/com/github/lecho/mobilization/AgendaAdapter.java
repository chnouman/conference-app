package com.github.lecho.mobilization;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder> {

    private String[] dataset;

    public AgendaAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    @Override
    public AgendaAdapter.AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda, parent, false);
        AgendaViewHolder viewHolder = new AgendaViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder holder, int position) {
        holder.bindView(dataset[position]);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    public static class AgendaViewHolder extends RecyclerView.ViewHolder {


        public AgendaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(String text) {
        }
    }
}
package com.example.admin.Task.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.Task.R;
import com.example.admin.Task.apimodel.Result;

import java.util.List;

/**
 * Created by ADMIN on 08-08-2016.
 */
public class RestrosAdapter extends RecyclerView.Adapter<RestrosAdapter.RestroViewHolder> {
    private List<Result> restros;
    private int rowLayout;
    private Context context;


    public RestrosAdapter(List<Result> restros, int rowLayout, Context context) {
        this.restros = restros;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public RestrosAdapter.RestroViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new RestroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestroViewHolder holder, final int position) {
        holder.restroTitle.setText(restros.get(position).getName());
        holder.data.setText(Long.toString(restros.get(position).getPriceLevel()));
        holder.restroDescription.setText(restros.get(position).getVicinity());
        holder.rating.setText(Double.toString(restros.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return restros.size();
    }

    public static class RestroViewHolder extends RecyclerView.ViewHolder {
        LinearLayout restroLayout;
        TextView restroTitle;
        TextView data;
        TextView restroDescription;
        TextView rating;


        public RestroViewHolder(View v) {
            super(v);
            restroLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            restroTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            restroDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
        }
    }
}

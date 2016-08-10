package com.example.admin.Task.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.Task.R;
import com.example.admin.Task.activities.DrawPath;
import com.example.admin.Task.apimodel.Result;

import java.util.List;

/**
 * Created by ADMIN on 08-08-2016.
 */
public class RestrosAdapter extends RecyclerView.Adapter<RestrosAdapter.RestroViewHolder> {
    private static List<Result> restros;
    private static int rowLayout;
    private static Context context;
    private static double currntLatitude;
    private static double currntLongitude;


    public RestrosAdapter(List<Result> restros, double currntLatitude, double currntLongitude, int rowLayout, Context context) {

        this.restros = restros;
        this.rowLayout = rowLayout;
        this.context = context;
        this.currntLatitude = currntLatitude;
        this.currntLongitude = currntLongitude;
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

    public static class RestroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, DrawPath.class);
            String source = currntLatitude + "," + currntLongitude;
            String destn = restros.get(getPosition()).getGeometry().getLocation().getLat() + "," + restros.get(getPosition()).getGeometry().getLocation().getLng();
            intent.putExtra("source", source);
            intent.putExtra("destn", destn);
            context.startActivity(intent);
        }
    }
}

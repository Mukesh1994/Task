package com.example.admin.Task.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.Task.R;
import com.example.admin.Task.activities.DrawPath;
import com.example.admin.Task.dbmodel.Place;

import java.util.List;

/**
 * Created by ADMIN on 08-08-2016.
 */
public class RestrosAdapter extends RecyclerView.Adapter<RestrosAdapter.RestroViewHolder> {
    private static List<Place> restros;
    private static int rowLayout;
    private static Context context;

    public RestrosAdapter(List<Place> restros, int rowLayout, Context context) {

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
        holder.restroDescription.setText(restros.get(position).getAddress());
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
            v.setOnClickListener(this);
            restroLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            restroTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            restroDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, DrawPath.class);
            String destination = restros.get(getPosition()).getLatitude() + "," + restros.get(getPosition()).getLongitude();
            Log.d("onClicked:", destination);
            // intent.putExtra("source", source);
            intent.putExtra("destn: ", destination);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}

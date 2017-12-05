package com.backbase.backbasereader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backbase.backbasereader.R;
import com.backbase.backbasereader.model.City;

import java.util.List;

/**
 * Adapter to display cities in the RecyclerView
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private List<City> cities;

    public CityAdapter(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cityView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_items,parent,false);
        return new CityAdapter.ViewHolder(cityView);
    }

    @Override
    public void onBindViewHolder(CityAdapter.ViewHolder holder, int position) {
        City city = cities.get(position);
        holder.textView.setText(city.getCountry() + " - " + city.getName());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public ViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.city_name);
        }
    }
}

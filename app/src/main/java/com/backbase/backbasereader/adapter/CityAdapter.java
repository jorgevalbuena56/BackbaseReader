package com.backbase.backbasereader.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backbase.backbasereader.R;
import com.backbase.backbasereader.listener.ListEvents;
import com.backbase.backbasereader.model.City;

import java.util.List;

/**
 * Adapter to display cities in the RecyclerView
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private ListEvents cityClick;

    public CityAdapter(ListEvents cityClick) {
        this.cityClick = cityClick;
    }

    private final SortedList.Callback<City> mCallback = new SortedList.Callback<City>() {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(City cityOne, City cityTwo) {
            return cityOne.compareTo(cityTwo);
        }

        @Override
        public boolean areContentsTheSame(City oldItem, City newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(City item1, City item2) {
            return item1.getId().equals(item2.getId());
        }
    };

    private final SortedList<City> citySearchList = new SortedList<>(City.class, mCallback);

    public void add(List<City> cities) {
        citySearchList.addAll(cities);
    }

    public void replaceAll(List<City> cities) {
        citySearchList.beginBatchedUpdates();
        for (int i = citySearchList.size() - 1; i >= 0; i--) {
            final City city = citySearchList.get(i);
            if (!cities.contains(city)) {
                citySearchList.remove(city);
            }
        }
        citySearchList.addAll(cities);
        citySearchList.endBatchedUpdates();
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cityView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.city_items,parent,false);
        return new CityAdapter.ViewHolder(cityView);
    }

    @Override
    public void onBindViewHolder(CityAdapter.ViewHolder holder, int position) {
        final City city = citySearchList.get(position);
        StringBuilder builder = new StringBuilder(city.getCountry())
                                                .append(" - ").append(city.getName());
        holder.textView.setText(builder);
        holder.itemView.setOnClickListener(v ->
                cityClick.onCityClicked(city.getName(), city.getCoordinates()));
    }

    @Override
    public int getItemCount() {
        return citySearchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public ViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.city_name);
        }
    }
}

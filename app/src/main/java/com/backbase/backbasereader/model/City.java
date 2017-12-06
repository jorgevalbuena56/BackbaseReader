package com.backbase.backbasereader.model;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * City Model to store the information read from the Json File
 */
public class City implements Serializable, Comparable<City> {
    @JsonProperty("_id")
    private String id;
    private String name;
    private String country;
    @JsonProperty("coords")
    private LatLng coordinates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    @JsonAnySetter
    @SuppressWarnings("unchecked, unused")
    public void setAnyValues(String propertyName, Object value) {
        if (propertyName.startsWith("coord")) {
            Map<String, ?> coords = (LinkedHashMap<String, ?>) value;
            Double lat = 0.0;
            Double lon = 0.0;
            //Sometime the value comes as an Integer, prevent casting errors
            if (coords.get("lat") instanceof Integer) {
                lat = Double.valueOf((Integer)coords.get("lat"));
            }
            if (coords.get("lat") instanceof Double) {
                lat = (Double) coords.get("lat");
            }
            if (coords.get("lon") instanceof Integer) {
                lon = Double.valueOf((Integer)coords.get("lon"));
            }
            if (coords.get("lon") instanceof Double) {
                lon = (Double) coords.get("lon");
            }
            setCoordinates(new LatLng(lat, lon));
        }
    }

    @Override
    public int compareTo(@NonNull City city) {
        if (getName() == null || city.getName() == null ) {
            return 0;
        }
        return getName().compareToIgnoreCase(city.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (!getId().equals(city.getId())) return false;
        if (!getName().equals(city.getName())) return false;
        return getCountry().equals(city.getCountry());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getCountry().hashCode();
        return result;
    }
}

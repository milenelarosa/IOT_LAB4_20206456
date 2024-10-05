package com.example.lab4_20206456.RecyclerView;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaguesResponse {

    @SerializedName("countries")
    private List<League> countries;

    @SerializedName("leagues")
    private List<League> leagues;

    public List<League> getCountries() {
        return countries;
    }

    public void setCountries(List<League> countries) {
        this.countries = countries;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }
}

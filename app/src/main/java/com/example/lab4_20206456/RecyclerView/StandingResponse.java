package com.example.lab4_20206456.RecyclerView;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StandingResponse {
    @SerializedName("table")
    private List<Standing> table;

    public List<Standing> getStandings() {
        return table;
    }

    public void setStandings(List<Standing> table) {
        this.table = table;
    }
}

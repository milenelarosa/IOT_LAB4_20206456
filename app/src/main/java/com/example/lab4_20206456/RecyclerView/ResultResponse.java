package com.example.lab4_20206456.RecyclerView;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultResponse {
    @SerializedName("events")
    private List<Result> events;

    public List<Result> getEvents() {
        return events;
    }

    public void setEvents(List<Result> events) {
        this.events = events;
    }
}

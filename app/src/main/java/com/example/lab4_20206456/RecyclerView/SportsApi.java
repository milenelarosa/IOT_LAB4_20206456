package com.example.lab4_20206456.RecyclerView;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface SportsApi {
    @GET("api/v1/json/3/all_leagues.php")
    Call<LeaguesResponse> getAllLeagues();

    @GET("api/v1/json/3/search_all_leagues.php")
    Call<LeaguesResponse> getLeaguesByCountry(@Query("c") String country);

    @GET("api/v1/json/3/lookuptable.php")
    Call<StandingResponse> getLeagueStandings(@Query("l") String leagueId, @Query("s") String season);
}

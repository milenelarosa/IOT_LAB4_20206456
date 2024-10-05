package com.example.lab4_20206456;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab4_20206456.RecyclerView.League;
import com.example.lab4_20206456.RecyclerView.LeagueAdapter;
import com.example.lab4_20206456.RecyclerView.LeaguesResponse;
import com.example.lab4_20206456.RecyclerView.SportsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LigasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LigasFragment extends Fragment {

    private RecyclerView leaguesRecyclerView;
    private LeagueAdapter leagueAdapter;
    private EditText searchEditText;
    private Button searchButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LigasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LigasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LigasFragment newInstance(String param1, String param2) {
        LigasFragment fragment = new LigasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ligas, container, false);

        leaguesRecyclerView = view.findViewById(R.id.recyclerViewLigas);
        searchEditText = view.findViewById(R.id.buscarPais);
        searchButton = view.findViewById(R.id.btnBuscar);

        leaguesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        leagueAdapter = new LeagueAdapter(new ArrayList<>());
        leaguesRecyclerView.setAdapter(leagueAdapter);

        searchButton.setOnClickListener(v -> searchLeagues());

        return view;
    }

    private void searchLeagues() {
        String country = searchEditText.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.thesportsdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SportsApi sportsApi = retrofit.create(SportsApi.class);

        Call<LeaguesResponse> call;

        if (country.isEmpty()) {
            // Place Holder vacío = Mostrar todas las ligas
            call = sportsApi.getAllLeagues();
        } else {
            // Buscar ligas por país
            call = sportsApi.getLeaguesByCountry(country);
        }

        call.enqueue(new Callback<LeaguesResponse>() {
            @Override
            public void onResponse(Call<LeaguesResponse> call, Response<LeaguesResponse> response) {
                if (response.isSuccessful()) {
                    LeaguesResponse leaguesResponse = response.body();
                    List<League> leagues = null;

                    if (leaguesResponse.getLeagues() != null) {
                        leagues = leaguesResponse.getLeagues();
                    }

                    else if (leaguesResponse.getCountries() != null) {
                        leagues = leaguesResponse.getCountries();
                    }

                    if (leagues != null && !leagues.isEmpty()) {
                        Log.d("APIResponse", "Leagues received: " + leagues.toString());
                        leagueAdapter.updateLeagues(leagues);
                    } else {
                        Toast.makeText(getContext(), "No se encontraron resultados.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
                    Log.e("APIResponse", "Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LeaguesResponse> call, Throwable t) {
                Log.e("APIResponse", "La consulta a la API falló", t);
            }
        });
    }
}
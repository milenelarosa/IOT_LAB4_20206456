package com.example.lab4_20206456;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab4_20206456.RecyclerView.SportsApi;
import com.example.lab4_20206456.RecyclerView.StandingAdapter;
import com.example.lab4_20206456.RecyclerView.StandingResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PosicionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PosicionesFragment extends Fragment {

    private RecyclerView standingsRecyclerView;
    private StandingAdapter standingAdapter;
    private EditText leagueIdEditText, seasonEditText;
    private Button searchButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PosicionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PosicionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PosicionesFragment newInstance(String param1, String param2) {
        PosicionesFragment fragment = new PosicionesFragment();
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
        View view = inflater.inflate(R.layout.fragment_posiciones, container, false);

        standingsRecyclerView = view.findViewById(R.id.recyclerViewPosiciones);
        leagueIdEditText = view.findViewById(R.id.idLiga);
        seasonEditText = view.findViewById(R.id.temporada);
        searchButton = view.findViewById(R.id.btnBuscar);

        standingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        standingAdapter = new StandingAdapter(new ArrayList<>());
        standingsRecyclerView.setAdapter(standingAdapter);

        searchButton.setOnClickListener(v -> searchStandings());

        return view;
    }

    private void searchStandings() {
        String leagueId = leagueIdEditText.getText().toString().trim();
        String season = seasonEditText.getText().toString().trim();

        if (leagueId.isEmpty() || season.isEmpty()) {
            StringBuilder message = new StringBuilder("Por favor ingrese: ");

            if (leagueId.isEmpty()) {
                message.append("el ID de la liga");
            }
            if (season.isEmpty()) {
                if (leagueId.isEmpty()) {
                    message.append(" y ");
                }
                message.append("la temporada");
            }
            message.append(".");
            Toast.makeText(getContext(), message.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.thesportsdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SportsApi sportsApi = retrofit.create(SportsApi.class);
        Call<StandingResponse> call = sportsApi.getLeagueStandings(leagueId, season);

        call.enqueue(new Callback<StandingResponse>() {
            @Override
            public void onResponse(Call<StandingResponse> call, Response<StandingResponse> response) {
                if (response.isSuccessful()) {
                    StandingResponse standingsResponse = response.body();
                    if (standingsResponse != null && standingsResponse.getStandings() != null) {
                        standingAdapter.updateStandings(standingsResponse.getStandings());
                    } else {
                        Toast.makeText(getContext(), "No se encontraron resultados.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StandingResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
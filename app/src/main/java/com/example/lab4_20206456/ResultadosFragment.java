package com.example.lab4_20206456;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab4_20206456.RecyclerView.Result;
import com.example.lab4_20206456.RecyclerView.ResultAdapter;
import com.example.lab4_20206456.RecyclerView.ResultResponse;
import com.example.lab4_20206456.RecyclerView.SportsApi;
import com.example.lab4_20206456.RecyclerView.StandingResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultadosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultadosFragment extends Fragment implements SensorEventListener {

    private RecyclerView recyclerViewResultados;
    private ResultAdapter resultadosAdapter;
    private List<Result> resultadosList = new ArrayList<>();
    private EditText edtIdLiga, edtTemporada, edtRonda;
    private Button btnBuscarResultados;


    // Parte SENSOR
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isDialogOpen = false;
    private static final float SHAKE_THRESHOLD = 20.0f;
    private static final int SHAKE_WAIT_TIME_MS = 500;
    private long mShakeTime = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResultadosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultadosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultadosFragment newInstance(String param1, String param2) {
        ResultadosFragment fragment = new ResultadosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultados, container, false);

        recyclerViewResultados = view.findViewById(R.id.recyclerViewResultados);
        edtIdLiga = view.findViewById(R.id.idLiga);
        edtTemporada = view.findViewById(R.id.season);
        edtRonda = view.findViewById(R.id.ronda);
        btnBuscarResultados = view.findViewById(R.id.btnBuscar);

        resultadosAdapter = new ResultAdapter(resultadosList);
        recyclerViewResultados.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewResultados.setAdapter(resultadosAdapter);

        btnBuscarResultados.setOnClickListener(v -> buscarResultados());

        return view;
    }

    private void buscarResultados() {
        String idLiga = edtIdLiga.getText().toString().trim();
        String temporada = edtTemporada.getText().toString().trim();
        String ronda = edtRonda.getText().toString().trim();

        if (idLiga.isEmpty() || temporada.isEmpty() || ronda.isEmpty()) {
            StringBuilder message = new StringBuilder("Por favor ingrese: ");
            boolean isFirst = true;
            if (idLiga.isEmpty()) {
                message.append("el ID de la liga");
                isFirst = false;
            }
            if (temporada.isEmpty()) {
                if (!isFirst) {
                    message.append(", ");
                }
                message.append("la temporada");
                isFirst = false;
            }
            if (ronda.isEmpty()) {
                if (!isFirst) {
                    message.append(", ");
                }
                message.append("la ronda");
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
        Call<ResultResponse> call = sportsApi.getLeagueResults(idLiga, ronda, temporada);

        call.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
                    ResultResponse resultResponse = response.body();
                    if (resultResponse != null && resultResponse.getEvents() != null) {
                        resultadosAdapter.updateResults(resultResponse.getEvents());
                    } else {
                        Toast.makeText(getContext(), "No se encontraron resultados.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar el sensor de acelerómetro
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

        if (acceleration > SHAKE_THRESHOLD) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mShakeTime > SHAKE_WAIT_TIME_MS && !isDialogOpen) {
                mShakeTime = currentTime;
                onShakeDetected();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // Al sacudir
    private void onShakeDetected() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        mostrarDialogoConfirmacion();
    }

    private void mostrarDialogoConfirmacion() {
        isDialogOpen = true;
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación")
                .setMessage("¿Desea eliminar el último resultado obtenido?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    if (!resultadosList.isEmpty()) {
                        int lastIndex = resultadosList.size() - 1;
                        resultadosList.remove(lastIndex);
                        resultadosAdapter.notifyItemRemoved(lastIndex);
                        resultadosAdapter.notifyItemRangeChanged(lastIndex, resultadosList.size());
                        Toast.makeText(getContext(), "Último resultado eliminado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No hay resultados para eliminar", Toast.LENGTH_SHORT).show();
                    }

                    isDialogOpen = false;
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    isDialogOpen = false;
                })
                .setOnCancelListener(dialog -> isDialogOpen = false)
                .show();
    }
}
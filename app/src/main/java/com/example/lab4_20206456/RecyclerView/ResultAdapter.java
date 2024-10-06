package com.example.lab4_20206456.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab4_20206456.R;

import java.util.List;
import java.util.Objects;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultadosViewHolder> {

    private List<Result> resultadosList;

    public ResultAdapter(List<Result> resultadosList) {
        this.resultadosList = resultadosList;
    }

    @NonNull
    @Override
    public ResultadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resultado, parent, false);
        return new ResultadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadosViewHolder holder, int position) {
        Result resultado = resultadosList.get(position);
        holder.bind(resultado);
    }

    @Override
    public int getItemCount() {
        if(resultadosList == null) {
            return 0;
        }
        return resultadosList.size();
    }

    public void updateResults(List<Result> newResults) {
        resultadosList.clear();
        resultadosList.addAll(newResults);
        notifyDataSetChanged();
    }


    public static class ResultadosViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreCompetencia, txtRonda, txtEquipoLocal, txtEquipoVisitante, txtResultado, txtFecha, txtEspectadores;
        ImageView imgLogoCompetencia;

        public ResultadosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreCompetencia = itemView.findViewById(R.id.txtNombreCompetencia);
            txtRonda = itemView.findViewById(R.id.txtRonda);
            txtEquipoLocal = itemView.findViewById(R.id.txtEquipoLocal);
            txtEquipoVisitante = itemView.findViewById(R.id.txtEquipoVisitante);
            txtResultado = itemView.findViewById(R.id.txtResultado);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtEspectadores = itemView.findViewById(R.id.txtEspectadores);
            imgLogoCompetencia = itemView.findViewById(R.id.imgLogoCompetencia);
        }

        public void bind(Result result) {
            txtNombreCompetencia.setText(result.getStrLeague());
            txtRonda.setText("Ronda: " +result.getIntRound());
            txtEquipoLocal.setText("Local: " + result.getStrHomeTeam());
            txtEquipoVisitante.setText("Visitante: " + result.getStrAwayTeam());

            String resultado = Objects.equals(result.getStrResult(), "") ? "Por definir" : result.getStrResult();
            txtResultado.setText("Resultado: " + resultado);
            txtFecha.setText("Fecha: " +result.getDateEvent());

            String espectadores = result.getIntSpectators() != null ? result.getIntSpectators() : "0";
            txtEspectadores.setText(espectadores + " espectadores");

            // Para cargar la imagen del badge
            Glide.with(imgLogoCompetencia.getContext())
                    .load(result.getStrLeagueBadge())
                    .into(imgLogoCompetencia);
        }
    }
}

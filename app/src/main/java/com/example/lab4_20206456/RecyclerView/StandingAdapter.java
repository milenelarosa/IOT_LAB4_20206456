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

public class StandingAdapter extends RecyclerView.Adapter<StandingAdapter.StandingViewHolder> {

    private List<Standing> standingList;

    public StandingAdapter(List<Standing> standingList) {
        this.standingList = standingList;
    }

    @NonNull
    @Override
    public StandingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_posicion, parent, false);
        return new StandingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StandingViewHolder holder, int position) {
        Standing standing = standingList.get(position);
        holder.bind(standing);
    }

    @Override
    public int getItemCount() {
        if (standingList == null) {
            return 0;
        }
        return standingList.size();
    }

    public void updateStandings(List<Standing> newStandings) {
        standingList = newStandings;
        notifyDataSetChanged();
    }

    class StandingViewHolder extends RecyclerView.ViewHolder {
        TextView teamNameTextView, rankTextView, recordTextView, goalsTextView;
        ImageView badgeImageView;

        public StandingViewHolder(@NonNull View itemView) {
            super(itemView);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            recordTextView = itemView.findViewById(R.id.recordTextView);
            goalsTextView = itemView.findViewById(R.id.goalsTextView);
            badgeImageView = itemView.findViewById(R.id.badgeImageView);
        }

        public void bind(Standing standing) {
            teamNameTextView.setText(standing.getStrTeam());
            rankTextView.setText("Rank: " + standing.getIntRank());
            recordTextView.setText("W/D/L: " + standing.getIntWin() + "/" + standing.getIntDraw() + "/" + standing.getIntLoss());
            goalsTextView.setText("GF/GA/GD: " + standing.getIntGoalsFor() + "/" + standing.getIntGoalsAgainst() + "/" + standing.getIntGoalDifference());

            // Para cargar la imagen del badge
            Glide.with(badgeImageView.getContext())
                    .load(standing.getStrBadge())
                    .into(badgeImageView);
        }
    }
}

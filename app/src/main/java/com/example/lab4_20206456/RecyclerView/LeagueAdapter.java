package com.example.lab4_20206456.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20206456.R;

import java.util.List;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder> {

    private List<League> leagueList;

    public LeagueAdapter(List<League> leagueList) {
        this.leagueList = leagueList;
    }

    @NonNull
    @Override
    public LeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_league, parent, false);
        return new LeagueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueViewHolder holder, int position) {
        League league = leagueList.get(position);
        holder.bind(league);
    }

    @Override
    public int getItemCount() {
        if (leagueList == null) {
            return 0;
        }
        return leagueList.size();
    }

    public void updateLeagues(List<League> newLeagues) {
        leagueList = newLeagues;
        notifyDataSetChanged();
    }

    class LeagueViewHolder extends RecyclerView.ViewHolder {
        TextView leagueNameTextView, alternateNameTextView;

        public LeagueViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueNameTextView = itemView.findViewById(R.id.leagueNameTextView);
            alternateNameTextView = itemView.findViewById(R.id.alternateNameTextView);
        }

        public void bind(League league) {
            leagueNameTextView.setText(league.getStrLeague());
            alternateNameTextView.setText(league.getStrLeagueAlternate());
        }
    }
}

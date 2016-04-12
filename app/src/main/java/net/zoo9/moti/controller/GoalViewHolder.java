package net.zoo9.moti.controller;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.zoo9.moti.R;

/**
 * Created by sheldon on 16. 4. 3..
 */
public class GoalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView label;
    GoalAdapter goalAdapter = null;

    public GoalViewHolder(View itemView, GoalAdapter goalAdapter) {
        super(itemView);
        label = (TextView) itemView.findViewById(R.id.text_goal_desc);
        itemView.setOnClickListener(this);
        this.goalAdapter = goalAdapter;
    }

    @Override
    public void onClick(View v) {
        goalAdapter.toggleSelected(getAdapterPosition());
    }
}

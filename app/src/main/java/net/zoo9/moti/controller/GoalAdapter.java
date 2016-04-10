package net.zoo9.moti.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.zoo9.moti.R;
import net.zoo9.moti.model.Goal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sheldon on 16. 4. 3..
 */

public class GoalAdapter extends RecyclerView.Adapter<GoalViewHolder>{
    List<Goal> goalList = null;
    Set<Integer> selected = null;

    private int itemLayout;

    public GoalAdapter (List<Goal> goalList, int itemLayout) {
        this.goalList = goalList;
        this.itemLayout = itemLayout;
        this.selected = new HashSet<Integer>();
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View clickedView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
                return new GoalViewHolder(clickedView);
            case 1:
                View unclickedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item_layout_clicked, parent, false);
                return new GoalViewHolder(unclickedView);
        }

        View defaultView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new GoalViewHolder(defaultView);
    }

    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        Goal item = goalList.get(position);
        holder.label.setText(item.goal_desc);
    }

    @Override
    public int getItemViewType(int position) {
        if (isSelected(position)) {
            return 1;
        }
        return 0;
    }

    private boolean isSelected(int position) {
        if (position == 1) return true;
        Integer selectedPos = new Integer(position);
        return this.selected.contains(selectedPos);
    }

    public void setClicked(int position) {
        this.selected.add(new Integer(position));
    }

    @Override
    public int getItemCount() {
        if (goalList != null) {
            return goalList.size();
        }
        return 0;
    }
}

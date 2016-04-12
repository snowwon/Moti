package net.zoo9.moti.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.zoo9.moti.R;
import net.zoo9.moti.model.Goal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sheldon on 16. 4. 3..
 */

public class GoalAdapter extends RecyclerView.Adapter<GoalViewHolder>{
    List<Goal> goalList = null;
    Set<Integer> selected = null;
    final static int LIMIT_SELECTED_GOALS = 3;

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
                return new GoalViewHolder(clickedView, this);
            case 1:
                View unclickedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item_layout_clicked, parent, false);
                return new GoalViewHolder(unclickedView, this);
        }

        View defaultView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new GoalViewHolder(defaultView, this);
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
        Integer selectedPos = new Integer(position);
        return this.selected.contains(selectedPos);
    }

    public int getSizeOfSelectedGoals() {
        return this.selected.size();
    }

    public void setClicked(int position) {
        this.selected.add(new Integer(position));
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (goalList != null) {
            return goalList.size();
        }
        return 0;
    }

    public void addNewGoalWithSelected(Goal newGoal) {
        goalList.add(newGoal);
        int position = goalList.indexOf(newGoal);
        this.selected.add(position);
    }

    public ArrayList<String> getSelectedGoalList() {
        ArrayList<String> selectedGoalList = new ArrayList<String>();

        for (Integer selectedPos : selected) {
            selectedGoalList.add(goalList.get(selectedPos).goal_desc);
        }

        return selectedGoalList;
    }

    public void toggleSelected(int position) {
        Integer selectedPosition = new Integer(position);
        if (selected.contains(selectedPosition)) {
            selected.remove(selectedPosition);
        } else {
            selected.add(selectedPosition);
        }
//        this.selected.add(new Integer(position));
        this.notifyDataSetChanged();
    }
}

package net.zoo9.moti.controller;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.zoo9.moti.R;

/**
 * Created by sheldon on 16. 4. 3..
 */
public class GoalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView label;
    public TextView goal_id;
    public ImageButton delete_image_view;
    public boolean removable = false;
    GoalAdapter goalAdapter = null;

    public GoalViewHolder(View itemView, GoalAdapter goalAdapter) {
        super(itemView);
        label = (TextView) itemView.findViewById(R.id.text_goal_desc);
        goal_id = (TextView) itemView.findViewById(R.id.goal_id);
        delete_image_view = (ImageButton) itemView.findViewById(R.id.delete_button);
        itemView.setOnClickListener(this);
        delete_image_view.setOnClickListener(this);

        this.goalAdapter = goalAdapter;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete_button) {
//            Log.d("unja", "click : delete button");
            goalAdapter.removeGoalItem(Integer.parseInt(goal_id.getText().toString()), getAdapterPosition());
        } else {
            goalAdapter.toggleSelected(getAdapterPosition());
        }
    }

    public void hideTrashBin(boolean is_default) {
        if (is_default == true) {
            ((ImageButton) itemView.findViewById(R.id.delete_button)).setVisibility(View.INVISIBLE);
        } else {
            ((ImageButton) itemView.findViewById(R.id.delete_button)).setVisibility(View.VISIBLE);
        }
    }
}

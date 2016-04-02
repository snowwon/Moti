package net.zoo9.moti.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.zoo9.moti.R;

/**
 * Created by sheldon on 16. 4. 3..
 */
public class GoalViewHolder extends RecyclerView.ViewHolder {
    public TextView label;

    public GoalViewHolder(View itemView) {
        super(itemView);
        label = (TextView) itemView.findViewById(R.id.text_goal_desc);
    }
}

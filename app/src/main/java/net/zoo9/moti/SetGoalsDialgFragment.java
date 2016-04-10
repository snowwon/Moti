package net.zoo9.moti;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.zoo9.moti.controller.GoalAdapter;
import net.zoo9.moti.model.Goal;
import net.zoo9.moti.model.GoalManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheldon on 16. 4. 3..
 */
public class SetGoalsDialgFragment extends DialogFragment {
    private SetGoalDialogFragmentInterface mListener;

    public SetGoalsDialgFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dialogView = inflater.inflate(R.layout.fragment_set_goals, container, false);


        RecyclerView goalsRecyclerView = (RecyclerView)dialogView.findViewById(R.id.recycle_goals);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(goalsRecyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        goalsRecyclerView.setLayoutManager(linearLayoutManager);



        List<Goal> goals = getGoalsFromDatabase();
        GoalAdapter goalAdapter = new GoalAdapter(goals, R.layout.goal_item_layout);

        goalsRecyclerView.setAdapter(goalAdapter);




        // just close this dialog without any action.
        ((Button) dialogView.findViewById(R.id.btn_close_goals_dialog)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });


        // I want to reflect the user's choices into the main activity in order for user to finish their board set-up.
        ((Button) dialogView.findViewById(R.id.btn_update_parent_window)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> selectedGoals = getSelectedGoals();
                mListener.setGoals(selectedGoals);
                dismiss();
            }
        });

        return dialogView;

    }

    private List<Goal> getGoalsFromDatabase() {
        return GoalManager.getInstance(this.getContext()).getDefaultGoals();
    }

    private ArrayList<String> getSelectedGoals() {
        ArrayList<String> testSets = new ArrayList<>();
        testSets.add("test 1");
        testSets.add("test 1");
        testSets.add("test 1");
        testSets.add("test 1");
        testSets.add("test 1");
        testSets.add("test 1");
        testSets.add("test 1");
        testSets.add("test 1");

        return testSets;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SetGoalDialogFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SetGoalDialogFragmentInterface {
        public void setGoals(List<String> goals);
    }
}

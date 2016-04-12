package net.zoo9.moti;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private GoalAdapter goalAdapter = null;

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
        final View dialogView = inflater.inflate(R.layout.fragment_set_goals, container, false);


        RecyclerView goalsRecyclerView = (RecyclerView)dialogView.findViewById(R.id.recycle_goals);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(goalsRecyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        goalsRecyclerView.setLayoutManager(linearLayoutManager);



        List<Goal> goals = getGoalsFromDatabase();
        final GoalAdapter goalAdapter = new GoalAdapter(goals, R.layout.goal_item_layout);
        this.goalAdapter = goalAdapter;

        goalsRecyclerView.setAdapter(goalAdapter);


        ((Button)dialogView.findViewById(R.id.btn_add_new_goal)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newGoalText = ((EditText) dialogView.findViewById(R.id.text_new_goal)).getText().toString();
                if (newGoalText == null) newGoalText = "";
                ((EditText) dialogView.findViewById(R.id.text_new_goal)).setText("");

                if (newGoalText.isEmpty() == true) {
                    Toast.makeText(dialogView.getContext(), "칭찬 주제를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Goal newGoal = GoalManager.getInstance(dialogView.getContext()).addNewGoal(newGoalText);
                    goalAdapter.addNewGoalWithSelected(newGoal);
                    goalAdapter.notifyDataSetChanged();
                    linearLayoutManager.scrollToPosition(goalAdapter.getItemCount() - 1);
                }

            }
        });


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

                if (selectedGoals.size() > 3) {
                    AlertDialog alertDialog = new AlertDialog.Builder(dialogView.getContext()).create();
                    alertDialog.setTitle("알림");
                    int targetSizeOfDeSelect = selectedGoals.size() - 3;
                    alertDialog.setMessage("목표를 3개 이하 선택해 주세요.\n"+targetSizeOfDeSelect+" 개를 선택 취소해주세요.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    mListener.setGoals(selectedGoals);
                    dismiss();
                }
            }
        });

        return dialogView;

    }

    private List<Goal> getGoalsFromDatabase() {
        return GoalManager.getInstance(this.getContext()).getDefaultGoals();
    }

    private ArrayList<String> getSelectedGoals() {
        return goalAdapter.getSelectedGoalList();
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

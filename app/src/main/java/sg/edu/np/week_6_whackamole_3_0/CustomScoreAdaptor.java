package sg.edu.np.week_6_whackamole_3_0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    UserData user;
    Context c;
    private CustomScoreAdaptor.OnItemClickListener mListener;
    ArrayList<Integer> levelList;
    ArrayList<Integer> scoreList;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CustomScoreAdaptor.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public CustomScoreAdaptor(Context c, UserData userdata){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        this.user = userdata;
        this.c = c;
        this.levelList = userdata.getLevels();
        this.scoreList = userdata.getScores();
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select, parent, false);
        CustomScoreViewHolder scoreView = new CustomScoreViewHolder(v, mListener);
        return scoreView;
        /* Hint:
        This method dictates how the viewholder layuout is to be once the viewholder is created.
         */
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){
        String score = String.valueOf(scoreList.get(position));
        String level = String.valueOf(levelList.get(position));

        holder.mLevelNo.setText(level);
        holder.mScore.setText(score);

        Log.v(TAG, FILENAME + " Showing level " + levelList.get(position) + " with highest score: " + scoreList.get(position));

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */
    }

    public int getItemCount(){
        return levelList.size();
        /* Hint:
        This method returns the the size of the overall data.
         */
    }
}
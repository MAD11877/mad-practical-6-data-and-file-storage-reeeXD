package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreViewHolder extends RecyclerView.ViewHolder {
    /* Hint:
        1. This is a customised view holder for the recyclerView list @ levels selection page
     */
    public View v;
    public TextView mLevelNo, mScore;
    private static final String FILENAME = "CustomScoreViewHolder.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public CustomScoreViewHolder(final View itemView, final CustomScoreAdaptor.OnItemClickListener listener){
        super(itemView);
        v = itemView;
        mLevelNo = itemView.findViewById(R.id.levelNo);
        mScore = itemView.findViewById(R.id.scoreDisplay);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });

        /* Hint:
        This method dictates the viewholder contents and links the widget to the objects for manipulation.
         */
    }


}

package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main3Activity extends AppCompatActivity {
    /* Hint:
        1. This displays the available levels from 1 to 10 to the user.
        2. The different levels makes use of the recyclerView and displays the highest score
           that corresponds to the different levels.
        3. Selection of the levels will load relevant Whack-A-Mole game.
        4. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        5. There is an option return to the login page.
     */
    String username;
    RecyclerView mLevelList;
    CustomScoreAdaptor mAdapter;
    LinearLayoutManager mLayoutManager;
    Button mLogout;
    MyDBHandler handler;
    private static final String FILENAME = "Main3Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        handler = new MyDBHandler(this);
        Bundle b = getIntent().getExtras();
        username = b.getString("Username");
        mLevelList = findViewById(R.id.levelList);

        Log.v(TAG, FILENAME + ": Show Level for User: "+ username);
        mAdapter = new CustomScoreAdaptor(this, handler.findUser(username));
        mLayoutManager = new LinearLayoutManager(this);
        mLevelList.setLayoutManager(mLayoutManager);
        mLevelList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CustomScoreAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int levelNo = position + 1;
                Log.v(TAG, FILENAME + ": Challenge Level " + levelNo);
                nextLevel(username, levelNo);
            }
        });

        mLogout = findViewById(R.id.logoutButton);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
                builder.setTitle("Logout");
                builder.setMessage("Logout of Whack A Mole?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Intent intent = new Intent(Main3Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){ }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        /* Hint:
        This method receives the username account data and looks up the database for find the
        corresponding information to display in the recyclerView for the level selections page.

        Log.v(TAG, FILENAME + ": Show level for User: "+ userName);
         */
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void nextLevel(String username, int levelNo) {
        Intent intent = new Intent(Main3Activity.this, Main4Activity.class);
        Bundle extras = new Bundle();
        extras.putString("Username", username);
        extras.putInt("LevelNo", levelNo);
        intent.putExtras(extras);
        startActivity(intent);
    }
}

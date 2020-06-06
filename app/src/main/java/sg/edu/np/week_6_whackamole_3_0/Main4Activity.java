package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.String.format;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private TextView scoretext;
    private Button setButton, returnButton;
    private String username;
    private int Score = 0;
    private int delay, levelNo;
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    Bundle extra;
    private MyDBHandler handler;
    private final Handler mhandler = new Handler();
    private final Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
            Log.v(TAG, "New Mole Location!");
            assignNewMoles();
            mhandler.postDelayed(this, delay*1000);
        }
    };
    private void readyTimer(){
        readyTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                final Toast countDownToast = Toast.makeText(getApplicationContext(), format("Get Ready in %d seconds",millisUntilFinished/1000), Toast.LENGTH_SHORT);
                countDownToast.show();
                Handler timerHandler = new Handler();
                timerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countDownToast.cancel();
                    }
                }, 1000);
            }

            @Override
            public void onFinish() {
                readyTimer.cancel();
                assignNewMoles();
                placeMoleTimer();
                Toast.makeText(getApplicationContext(), "Go!", Toast.LENGTH_SHORT).show();
            }
        };
        readyTimer.start();
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */

    }
    private void placeMoleTimer(){

        mhandler.postDelayed(mrunnable, delay*1000);

        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
    }
    private static final int[] BUTTON_IDS = {R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("Username");
        levelNo = extras.getInt("LevelNo");
        handler = new MyDBHandler(this);

        scoretext = findViewById(R.id.scoretext2);

        delay = Math.abs(levelNo - 11);

        readyTimer();


        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */


        for(final int id : BUTTON_IDS){
            setButton = findViewById(id);
            setButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedId = view.getId();
                    Button clickedButton = findViewById(clickedId);
                    mhandler.removeCallbacks(mrunnable);
                    doCheck(clickedButton);
                    setNewMole(clickedButton);
                    placeMoleTimer();
                }
            });
        }

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main4Activity.this);
                builder.setTitle("Exit Game");
                builder.setMessage("Are you sure you wish to exit the level?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        updateUserScore();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) { }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    private void doCheck(Button checkButton)
    {
        if (checkButton.getText() == "*"){
            Score++;
            Log.v(TAG, "Hit, score added!");
        }
        else{
            Score--;
            Log.v(TAG, "Missed, score deducted!");
        }
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        scoretext.setText(String.valueOf(Score));
    }

    public void setNewMole(Button b)
    {
        if (levelNo < 6) {
            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            Button new_button = findViewById(BUTTON_IDS[randomLocation]);
            for (int id : BUTTON_IDS)
            {
                Button previous_button = (Button)findViewById(id);
                if (previous_button.getText().toString() == "*"){
                    previous_button.setText("O");
                }
            }
            new_button.setText("*");
        }
        else {
            if (b.getText().toString().equals("O")) {
                assignNewMoles();
            }
            else{
                Random ran = new Random();
                int randomLocation = ran.nextInt(9);
                b.setText("O");
                Button selectedButton = findViewById(BUTTON_IDS[randomLocation]);
                while (selectedButton.getText().toString().equals("*")) {
                    randomLocation = ran.nextInt(9);
                    selectedButton = findViewById(BUTTON_IDS[randomLocation]);
                }
                selectedButton.setText("*");
            }
        }
    }

    private void updateUserScore()
    {
        readyTimer.cancel();
        UserData user = handler.findUser(username);

        mhandler.removeCallbacks(mrunnable);

        int highest_score = user.getScores().get(levelNo-1);

        String newUsername = user.getMyUserName();
        String newPassword = user.getMyPassword();
        ArrayList<Integer> newLevels = user.getLevels();
        ArrayList<Integer> newScores = user.getScores();

        if (Score > highest_score) {
            Log.v(TAG, FILENAME + ": Update User Score...");
            newScores.set(levelNo-1, Score);
            handler.deleteAccount(username);
            handler.addUser(new UserData(newUsername, newPassword, newLevels, newScores));
        }
        Log.v(TAG, String.valueOf(handler.findUser("Create").getScores()));
        Log.v(TAG, FILENAME + ": Redirecting to level select");
        Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
        Bundle b = new Bundle();
        b.putString("Username", username);
        intent.putExtras(b);
        startActivity(intent);

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
    }

    private void assignNewMoles() {
        for (int id : BUTTON_IDS){
            Button clearButton = findViewById(id);
            clearButton.setText("O");
        }
        if (levelNo < 6){
            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            Button new_button = findViewById(BUTTON_IDS[randomLocation]);
            new_button.setText("*");
        }
        else {
            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            int randomLocation2 = ran.nextInt(9);
            Button new_button = findViewById(BUTTON_IDS[randomLocation]);
            new_button.setText("*");
            while (randomLocation2 == randomLocation) {
                randomLocation2 = ran.nextInt(9);
            }
            Button sec_button = findViewById(BUTTON_IDS[randomLocation2]);
            sec_button.setText("*");
        }

    }
}

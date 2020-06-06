package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */

    private EditText nUsername, nPassword;
    private Button nCancel, nCreate;
    private MyDBHandler handler;
    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        handler = new MyDBHandler(this);
        nUsername = findViewById(R.id.enterCreateUsername);
        nPassword = findViewById(R.id.enterCreatePassword);
        nCancel = findViewById(R.id.cancelButton);
        nCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        nCreate = findViewById(R.id.createButton);
        nCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = nUsername.getText().toString().trim();
                final String password = nPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username)) {
                    nUsername.setError("Enter A Username");
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    nPassword.setError("Enter A Password");
                    return;
                }

                if(isExistingUser(username)) {
                    Log.v(TAG, "User already exists during new user creation!");
                    Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Integer> newLevelList = new ArrayList<>();
                ArrayList<Integer> newScoreList = new ArrayList<>();

                for (int i = 0; i < 10; i++){
                    newLevelList.add(i+1);
                    newScoreList.add(0);
                }

                UserData newUser = new UserData(username, password, newLevelList, newScoreList);
                handler.addUser(newUser);
                Log.v(TAG, FILENAME + ": New user created successfully!");
                nUsername.setText("");
                nPassword.setText("");
                Intent returnActivity = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(returnActivity);
            }
        });

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */
    }

    protected void onStop() {
        super.onStop();
        finish();
    }

    private boolean isExistingUser(String userName) {
        UserData user = handler.findUser(userName);


        if(user == null){
            Log.v(TAG, "Username available!");
            return false;
        }
        Log.v(TAG, FILENAME + ": Running Checks..." + user.getMyUserName() + "  <--> " + userName);
        Log.v(TAG, "Username taken");
        return true;
    }
}

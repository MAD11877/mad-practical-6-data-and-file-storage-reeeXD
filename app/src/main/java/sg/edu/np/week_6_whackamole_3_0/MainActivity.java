package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private EditText mUsername, mPassword;
    private Button mLoginButton;
    private TextView mCreateUser;
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    MyDBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new MyDBHandler(this);

        mUsername = findViewById(R.id.enterUsername);
        mPassword = findViewById(R.id.enterPassword);
        mLoginButton = findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsername.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(username)) {
                    mUsername.setError("Enter Username!");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Enter Password!");
                    return;
                }
                if(!isValidUser(username, password)){
                    Log.v(TAG, "Invalid User!");
                    mUsername.setText("");
                    mPassword.setText("");
                    return;
                }
                Log.v(TAG, FILENAME + ": Valid User! Logging In");
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                Bundle extras = new Bundle();
                extras.putString("Username", username);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        mCreateUser = findViewById(R.id.newUser);
        mCreateUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
                return true;
            }
        });
        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */


    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        UserData user = handler.findUser(userName);

        if(user == null){
            Log.v(TAG, "User does not exist!");
            Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        Log.v(TAG, FILENAME + ": Running Checks..." + user.getMyUserName() + ": " + user.getMyPassword() +" <--> "+ userName + " " + password);

        return true;
    }

}

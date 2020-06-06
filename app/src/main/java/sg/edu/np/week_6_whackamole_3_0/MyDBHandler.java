package sg.edu.np.week_6_whackamole_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    /*
        The Database has the following properties:
        1. Database name is WhackAMole.db
        2. The Columns consist of
            a. Username
            b. Password
            c. Level
            d. Score
        3. Add user method for adding user into the Database.
        4. Find user method that finds the current position of the user and his corresponding
           data information - username, password, level highest score for each level
        5. Delete user method that deletes based on the username
        6. To replace the data in the database, we would make use of find user, delete user and add user

        The database shall look like the following:

        Username | Password | Level | Score
        --------------------------------------
        User A   | XXX      | 1     |    0
        User A   | XXX      | 2     |    0
        User A   | XXX      | 3     |    0
        User A   | XXX      | 4     |    0
        User A   | XXX      | 5     |    0
        User A   | XXX      | 6     |    0
        User A   | XXX      | 7     |    0
        User A   | XXX      | 8     |    0
        User A   | XXX      | 9     |    0
        User A   | XXX      | 10    |    0
        User B   | YYY      | 1     |    0
        User B   | YYY      | 2     |    0

     */
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "WhackAMole.db";
    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    private static final String TABLE_NAME = "Users";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_USER_PASSWORD = "Password";
    private static final String COLUMN_USER_SCORES = "Score";
    private static final String COLUMN_USER_LEVELS = "Level";

    public MyDBHandler(Context context)
    {
        /* HINT:
            This is used to init the database.
         */
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USERS_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_USERNAME + " TEXT," +
                        COLUMN_USER_PASSWORD + " TEXT," +
                        COLUMN_USER_LEVELS  + " INTEGER," +
                        COLUMN_USER_SCORES + " INTEGER," +
                        "PRIMARY KEY (" + COLUMN_USERNAME + "," +COLUMN_USER_LEVELS + ")" + ");";
        db.execSQL(CREATE_USERS_TABLE);
        /* HINT:
            This is triggered on DB creation. */
        Log.v(TAG, "DB Created: " + CREATE_USERS_TABLE);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        /* HINT:
            This is triggered if there is a new version found. ALL DATA are replaced and irreversible.
         */
    }

    public void addUser(UserData userData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < 10; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, userData.getMyUserName());
            values.put(COLUMN_USER_PASSWORD, userData.getMyPassword());
            values.put(COLUMN_USER_LEVELS, userData.getLevels().get(i));
            values.put(COLUMN_USER_SCORES, userData.getScores().get(i));
            Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
        /* HINT:
                This adds the user to the database based on the information given.
                Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
             */
    }

    public UserData findUser(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_USERNAME + " = \"" + username + "\"";

        Cursor cursor = db.rawQuery(query, null);

        UserData user = new UserData();

        if(cursor.moveToFirst()) {
            user.setMyPassword(cursor.getString(1));
            do{
                user.getLevels().add(cursor.getInt(2));
                user.getScores().add(cursor.getInt(3));
            }while(cursor.moveToNext());
        }
        else {
            Log.v(TAG, FILENAME+ ": No data found!");
            cursor.close();
            db.close();
            return null;
        }

        Log.v(TAG, FILENAME + ": QueryData: " + user.getLevels().toString() + user.getScores().toString());
        cursor.close();
        db.close();

        return user;
        /* HINT:
            This finds the user that is specified and returns the data information if it is found.
            If not found, it will return a null.
            Log.v(TAG, FILENAME +": Find user form database: " + query);

            The following should be used in getting the query data.
            you may modify the code to suit your design.

            if(cursor.moveToFirst()){
                do{
                    ...
                    .....
                    ...
                }while(cursor.moveToNext());
                Log.v(TAG, FILENAME + ": QueryData: " + queryData.getLevels().toString() + queryData.getScores().toString());
            }
            else{
                Log.v(TAG, FILENAME+ ": No data found!");
            }
         */
    }

    public boolean deleteAccount(String username) {

        Log.v(TAG, FILENAME + "Deleting Account: " + username);

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = \""
                + username + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        if(cursor.moveToFirst()) {
            do {
                db.delete(TABLE_NAME, COLUMN_USERNAME + " = ?",
                        new String[] { username });
            }while(cursor.moveToNext());
        }
        else {
            Log.v(TAG, FILENAME+ ":No data found!");
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
        /* HINT:
            This finds and delete the user data in the database.
            This is not reversible.
            Log.v(TAG, FILENAME + ": Database delete user: " + query);
         */
    }
}

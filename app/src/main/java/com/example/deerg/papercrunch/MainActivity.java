package com.example.deerg.papercrunch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pg;
    Handler mHandler=new Handler();
    private int mprogressbar=0;
    SQLiteDatabase db;
    SQLiteDatabase datavase;//Making a Database object of SQLiteDatabase.
    LevelDbHelper levelDbHelper;//Calling the object of LevelDbHelper to access its methods.
    DataDbHelper dataDbHelper;//Calling the object of DataDbHelper to access its methods.
    public static int avid;//Contains the integer position of the avatar.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        sp.getInt("id_avatar",0);

        levelDbHelper=new LevelDbHelper(this);//Initializing variables
        levelDbHelper.checktable(datavase);//Checks if tables already exist.

        dataDbHelper=new DataDbHelper(this);//Initializing variables
        dataDbHelper.checktable(datavase);//Checks if tables already exist.

        Ans_Popup.stars5=dataDbHelper.getStars(db);
        pg=(ProgressBar)findViewById(R.id.progressBar);
        final Intent i = new Intent(this,login.class);
        final Intent ii = new Intent(this,Main2Activity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mprogressbar<100)
                {
                    mprogressbar++;
                    android.os.SystemClock.sleep(30);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            pg.setProgress(mprogressbar);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(sp.getInt("successfullogin",0)==0)
                            startActivity(i);
                        else
                            startActivity(ii);
                    }
                });
                finish();
            }
        }).start();


    }

}




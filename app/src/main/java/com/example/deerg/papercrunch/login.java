package com.example.deerg.papercrunch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;


public class login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    MainActivity one;
    Button skip;
    SignInButton signInButton;
    Button login;
    private GoogleApiClient googleApiClient;
    EditText username,password;
    LevelDbHelper levelDbHelper;
    AlertDialog.Builder builder;
    String reg_url="https://papercrunch-1.herokuapp.com/api/reset-password/";
    String email;
    CheckBox chk2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.editText7);
        password=(EditText)findViewById(R.id.editText6);
        builder = new AlertDialog.Builder(login.this);
        levelDbHelper=new LevelDbHelper(this);
        login = (Button)findViewById(R.id.button);
        one=new MainActivity();
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        final Intent i=new Intent(getApplicationContext(),CodedBefore.class);
        final Intent ii=new Intent(getApplicationContext(),Main2Activity.class);
        chk2=(CheckBox)findViewById(R.id.check_2);
        chk2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                }
                else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        skip=(Button)findViewById(R.id.button3);
        if(sp.getInt("coded",0)==0)
            skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(i);
            }
        });
        else
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ii);
                    finish();
                }
            });
        final Intent inte = new Intent((getApplicationContext()),Registeration.class);
        Button but = (Button)findViewById(R.id.button2);
        but.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(inte);
                finish();
            }
        } );

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//http://192.168.43.29:8000/
                //https://papercrunch-1.herokuapp.com/
                if (!isNetworkConnected()) {
                    new AlertDialog.Builder(login.this)
                            .setMessage("Please check your internet connection")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                } else {
                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://papercrunchapp.herokuapp.com/").addConverterFactory(GsonConverterFactory.create())
                            .build();
                    final getdataApi gda = retrofit.create(getdataApi.class);

                    Call<Post> call = gda.createPost(username.getText().toString(), password.getText().toString());

                    call.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Code: " + response.code(), "lol");
                                Toast.makeText(login.this, "Please check your credentials", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Post posts = response.body();
                            //Toast.makeText(login.this,one.token,Toast.LENGTH_SHORT).show();
                            Log.d("token ", posts.getToken());
                            levelDbHelper.updatecurrlev(one.datavase, posts.getCurrentLevel());

                            SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("email", username.getText().toString());
                            editor.putInt("id_avatar", posts.getID());
                            editor.putInt("totalstars", posts.getTotalstars());
                            editor.putString("token", posts.getToken());
                            editor.putString("fname", posts.getFname());
                            editor.putString("lname", posts.getLname());
                            //editor.putString("phoneno",posts.getPno());
                            editor.putInt("successfullogin", 1);
                            editor.commit();

                            SharedPreferences sp1 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                            final String token = sp1.getString("token", "");
                            Call<user> call1 = gda.getUserDetails("Token " + token);
                            call1.enqueue(new Callback<user>() {
                                @Override
                                public void onResponse(Call<user> call, Response<user> response) {
                                    if (!response.isSuccessful()) {
                                        Log.d("Code level: " + response.code(), token);
                                        return;
                                    }
                                    user userd = response.body();
                                    int prog[] = new int[9];
                                    prog[0] = userd.getLevelOne();
                                    prog[1] = userd.getLevelTwo();
                                    prog[2] = userd.getLevelThree();
                                    prog[3] = userd.getLevelFour();
                                    prog[4] = userd.getLevelFive();
                                    prog[5] = userd.getLevelSix();
                                    prog[6] = userd.getLevelSeven();
                                    prog[7] = userd.getLevelEight();
                                    prog[8] = userd.getLevelNine();
                                    for (int i = 0; i < 9; i++)
                                        levelDbHelper.changeprogress(i + 1, one.datavase, prog[i]);

                                    Toast.makeText(login.this, "hi", Toast.LENGTH_SHORT).show();


                                    Call<subbool> call2 = gda.getbool("Token " + token);
                                    call2.enqueue(new Callback<subbool>() {
                                        @Override
                                        public void onResponse(Call<subbool> call, Response<subbool> response) {
                                            if (!response.isSuccessful()) {
                                                Log.d("Code bool: " + response.code(), token);
                                                return;
                                            }
                                            subbool sb = response.body();
                                            int sbool[] = new int[27];
                                            sbool[0] = sb.getSublevel1();
                                            sbool[1] = sb.getSublevel2();
                                            sbool[2] = sb.getSublevel3();
                                            sbool[3] = sb.getSublevel4();
                                            sbool[4] = sb.getSublevel5();
                                            sbool[5] = sb.getSublevel6();
                                            sbool[6] = sb.getSublevel7();
                                            sbool[7] = sb.getSublevel8();
                                            sbool[8] = sb.getSublevel9();
                                            sbool[9] = sb.getSublevel10();
                                            sbool[10] = sb.getSublevel11();
                                            sbool[11] = sb.getSublevel12();
                                            sbool[12] = sb.getSublevel13();
                                            sbool[13] = sb.getSublevel14();
                                            sbool[14] = sb.getSublevel5();
                                            sbool[15] = sb.getSublevel16();
                                            sbool[16] = sb.getSublevel17();
                                            sbool[17] = sb.getSublevel18();
                                            sbool[18] = sb.getSublevel19();
                                            sbool[19] = sb.getSublevel20();
                                            sbool[20] = sb.getSublevel21();
                                            sbool[21] = sb.getSublevel22();
                                            sbool[22] = sb.getSublevel23();
                                            sbool[23] = sb.getSublevel24();
                                            sbool[24] = sb.getSublevel25();
                                            sbool[25] = sb.getSublevel26();
                                            sbool[26] = sb.getSublevel27();

                                            for (int i = 0; i < 27; i++)
                                                levelDbHelper.updatebool(i + 1, one.datavase, sbool[i]);
                                            Toast.makeText(login.this, "Welcome", Toast.LENGTH_SHORT).show();
                                            int check = 0;
                                            for (int i = 0; i < 9; i++) {
                                                int prog = levelDbHelper.getprogress(one.datavase, i+1);
                                                if (prog != 0) {
                                                    check++;
                                                }
                                            }
                                            if (check != 0) {
                                                startActivity(ii);
                                                finish();
                                            } else {
                                                Intent blah = new Intent(login.this, CodedBefore.class);
                                                startActivity(blah);
                                                finish();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<subbool> call, Throwable t) {
                                            Log.d("failed bool ", t.getMessage());
                                        }
                                    });

                                    startActivity(ii);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<user> call, Throwable t) {
                                    Log.d("failed level ", t.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {
                            Log.d("failed: ", t.getMessage());
                            Toast.makeText(login.this, "Please check your credentials and make sure you are connected to the internet", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent7 = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent7,1);


            }
        });



        TextView omg = (TextView)findViewById(R.id.forgot);
        omg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this,Forgot.class);
                startActivity(i);
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            gotoProfile();
        } else {
            Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile() {
        Intent intent8 = new Intent(login.this, ProfileActivity.class);
        startActivity(intent8);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return cm.getActiveNetworkInfo() != null && networkInfo.isConnected();
    }

}

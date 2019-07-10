package com.example.deerg.papercrunch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Button logoutBtn;
    TextView userName,userEmail,userId;
    ImageView profileImage;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;LevelDbHelper levelDbHelper;
    MainActivity one;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userName=(TextView)findViewById(R.id.name);
        userEmail=(TextView)findViewById(R.id.email);
        userId=(TextView)findViewById(R.id.userId);
        profileImage=(ImageView)findViewById(R.id.profileImage);
        one=new MainActivity();
        levelDbHelper=new LevelDbHelper(this);

        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        final Intent i=new Intent(getApplicationContext(),CodedBefore.class);
        final Intent ii=new Intent(getApplicationContext(),Main2Activity.class);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            final GoogleSignInAccount account=result.getSignInAccount();
            userName.setText(account.getGivenName());
            userEmail.setText(account.getEmail());
            userId.setText(account.getFamilyName());

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://papercrunchapp.herokuapp.com/").addConverterFactory(GsonConverterFactory.create())
                    .build();
            final getdataApi gda = retrofit.create(getdataApi.class);

            Call<Post> call = gda.createPost(account.getEmail(), "googlenull");

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Code: " + response.code(), "lol");
                        return;
                    }
                    Post posts = response.body();
                    Toast.makeText(ProfileActivity.this,"Please Hold on",Toast.LENGTH_SHORT).show();
                    Log.d("token ", posts.getToken());
                    levelDbHelper.updatecurrlev(one.datavase, posts.getCurrentLevel());
                    SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                    final Intent i=new Intent(getApplicationContext(),CodedBefore.class);
                    final Intent ii=new Intent(getApplicationContext(),Main2Activity.class);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("email", account.getEmail());
                    editor.putInt("id_avatar", posts.getID());
                    editor.putInt("totalstars", posts.getTotalstars());
                    editor.putString("token", posts.getToken());
                    editor.putString("fname", posts.getFname());
                    editor.putString("lname", posts.getLname());
                    editor.putInt("gsign",1);
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

                        //Toast.makeText(ProfileActivity.this, "hi", Toast.LENGTH_SHORT).show();


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
                                Toast.makeText(ProfileActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
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
                                    Intent blah = new Intent(ProfileActivity.this, CodedBefore.class);
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
                    Toast.makeText(ProfileActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();

                }
            });


                    try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        }
        else{
            gotoMainActivity();
        }
    }
    private void gotoMainActivity(){
        Intent intent9=new Intent(this,login.class);
        startActivity(intent9);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,CodedBefore.class);
        startActivity(i);

    }
    public void Skip2(View view){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://papercrunchapp.herokuapp.com/").addConverterFactory(GsonConverterFactory.create())
                .build();
        final getdataApi gda = retrofit.create(getdataApi.class);

        Call<Register> cal = gda.createReg(userEmail.getText().toString(),"googlenull",userName.getText().toString(),userId.getText().toString(),0,true);


        cal.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {

                if (!response.isSuccessful()) {
                    Log.d("Code: " + response.code(), "lol");
                    return;
                }
                Toast.makeText(ProfileActivity.this, "Registered!\nPlease Login again.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {

            }
        });

    }
}



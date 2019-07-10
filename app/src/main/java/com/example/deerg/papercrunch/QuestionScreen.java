package com.example.deerg.papercrunch;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.deerg.papercrunch.ConceptScreen.levid;
import static com.example.deerg.papercrunch.Main2Activity.card1;

public class QuestionScreen extends AppCompatActivity {

    MainActivity one;
    Main2Activity two;
    Context mContext;
    private ViewPager vp1;
    private QuetionFragmentAdapter adapter1;
    android.support.v7.widget.Toolbar custom_toolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    NavigationView navigationView;
    ExpandableListView mExpandableListView;
    android.widget.ExpandableListAdapter mExpandableListAdapter;
    List<String> listheader;
    HashMap<String, List<String>> listchild;
    public static String subname;
    public static List<String> c1,c2,c3;
    SubLevel three;
    LevelDbHelper levelDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);
        setuptoolbar();
        Intent getinfo = getIntent();
        subname = getinfo.getExtras().getString("subname");

        listheader = new ArrayList<String>();
        listchild = new HashMap<String, List<String>>();
        listheader.add("View All Sub Levels");
        listheader.add("View Previous Level");
        listheader.add("View Progress Cycle");
        listheader.add("");
        listheader.add("");
        listheader.add("Playground");
        listheader.add("Settings");
        listheader.add("Rate us");
        listheader.add("About us");

        one=new MainActivity();
        two=new Main2Activity();
        three=new SubLevel();
        levelDbHelper=new LevelDbHelper(this);
        mContext=this;

        c1=SubLevel.concept1;
        c2=SubLevel.concept2;
        c3=SubLevel.concept3;

        listchild.put(listheader.get(0),three.lev);
        listchild.put(listheader.get(1), three.head2);


        mExpandableListView = (ExpandableListView) findViewById(R.id.navmenu);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mExpandableListAdapter = new com.example.deerg.papercrunch.ExpandableListAdapter(this, listheader, listchild, mExpandableListView);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        getSupportActionBar().setIcon(R.drawable.logo1);
        getSupportActionBar().setTitle("");

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, custom_toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        navigationView.setItemIconTintList(null);

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id1) {
                if(groupPosition==0)
                {
                    TextView textView = (TextView) findViewById(R.id.subt);
                    Intent intent;
                    intent = new Intent(QuestionScreen.this,ConceptScreen.class);
                    intent.putExtra("con1",c1.get(childPosition));
                    intent.putExtra("con2",c2.get(childPosition));
                    intent.putExtra("con3",c3.get(childPosition));
                    intent.putExtra("subname",three.lev.get(childPosition));
                    intent.putExtra("levelid",three.id);
                    intent.putExtra("levelname",three.levelnam);
                    startActivity(intent);
                }
                else if(groupPosition==1)
                {
                    Intent intent = new Intent(mContext,SubLevel.class);
                    intent.putExtra("Level1",two.card1.get(childPosition).getlevelnum());
                    intent.putExtra("Levelname",two.card1.get(childPosition).getlevelname());
                    intent.putExtra("img",two.card1.get(childPosition).getimg());
                    intent.putExtra("id",childPosition+1);
                    mContext.startActivity(intent);
                }
                finish();
                return true;
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int lastExpandedPosition=-1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    mExpandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });



        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(groupPosition == 0 && mExpandableListView.isGroupExpanded(0)){
                    mExpandableListView.collapseGroup(groupPosition);
                }

                else if(groupPosition == 1 && mExpandableListView.isGroupExpanded(1)){
                    mExpandableListView.collapseGroup(groupPosition);
                }
                else if(groupPosition==0 || groupPosition==1) {
                    mExpandableListView.expandGroup(groupPosition);

                }
                else if(groupPosition==5)
                {if (!isNetworkConnected()) {
                    new AlertDialog.Builder(QuestionScreen.this)
                            .setMessage("Please check your internet connection")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                } else {
                    Intent i=new Intent(mContext,Playground.class);
                    startActivity(i);}
                } else if (groupPosition == 6) {
                    Intent i = new Intent(mContext, settings.class);
                    startActivity(i);
                } else if (groupPosition == 8) {
                    if (!isNetworkConnected()) {
                        new AlertDialog.Builder(QuestionScreen.this)
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
                    DataDbHelper dataDbHelper = new DataDbHelper(QuestionScreen.this);

                    SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    final String token = sp.getString("token", "");
                    //Toast.makeText(Main2Activity.this,token,Toast.LENGTH_SHORT).show();
                    Call<Void> call = gda.sync(levelDbHelper.getcurrlev(one.datavase), dataDbHelper.getStars(one.datavase), sp.getInt("id_avatar", 0), "Token " + token);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Code: " + response.code(), "lol");
                                //Toast.makeText(Main2Activity.this, "Failed Please try again!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("failed: ", t.getMessage());
                            //Toast.makeText(Main2Activity.this,"Failed Please try again",Toast.LENGTH_SHORT).show();
                        }
                    });

                    int prog[] = new int[9];
                    for (int i = 0; i < 9; i++)
                        prog[i] = levelDbHelper.getprogress(one.datavase, i + 1);

                    Call<Void> call1 = gda.setUserDetails("Token " + token, prog[0], prog[1], prog[2], prog[3], prog[4], prog[5], prog[6], prog[7], prog[8]);
                    call1.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {

                                //Toast.makeText(Main2Activity.this, "Sync Successful!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                            Log.d("failed poke: ", t.getMessage());
                            //Toast.makeText(Main2Activity.this, "Failed Please try again!!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    int sbb[] = new int[27];
                    for (int i = 0; i < 27; i++)
                        sbb[i] = levelDbHelper.getbool(i + 1, one.datavase);

                    Call<Void> call2 = gda.setbool("Token " + token, sbb[0], sbb[1], sbb[2], sbb[3], sbb[4], sbb[5], sbb[6], sbb[7], sbb[8],
                            sbb[9], sbb[10], sbb[11], sbb[12], sbb[13], sbb[14], sbb[15], sbb[16], sbb[17], sbb[18], sbb[19], sbb[20], sbb[21], sbb[22], sbb[23], sbb[24], sbb[25], sbb[26]);
                    call2.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (!response.isSuccessful()) {

                                Log.d("Code poker: " + response.code(), token);
                                //Toast.makeText(Main2Activity.this, "Failed Please try again!!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (response.code() == 200) {

                                //Toast.makeText(Main2Activity.this, "Sync Successful!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                            Log.d("failed poke: ", t.getMessage());
                            //Toast.makeText(Main2Activity.this, "Failed Please try again!!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

                return true;
            }
        });

        vp1=findViewById(R.id.vp_question);
        adapter1=new QuetionFragmentAdapter(getSupportFragmentManager());
        vp1.setAdapter(adapter1);

        TextView tv = (TextView)findViewById(R.id.tv_que_subname);
        tv.setText(subname);

        Button btn1=(Button)findViewById(R.id.btn_hint);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent2=new Intent(getApplicationContext(),PopupActivity.class);
                startActivity(intent2);
            }
        });
    }
    public void setuptoolbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            custom_toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.custom_toolbar);
        }
        setSupportActionBar(custom_toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.onerflow_menu, menu);
        ImageeAdapter imageeAdapter=new ImageeAdapter(this);
        menu.findItem(R.id.avatar).setIcon(imageeAdapter.image_id2[one.avid]);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i=new Intent(this,IdScreen.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        if(levid==9) {

            Intent intent1 = new Intent(QuestionScreen.this, SubLevel.class);
            intent1.putExtra("Level1","Level 9");
            intent1.putExtra("Levelname","Arrays and Strings");
            intent1.putExtra("img",R.drawable.ic_view_array_black_24dp);
            intent1.putExtra("prog",levelDbHelper.getprogress(one.datavase,9));
            intent1.putExtra("id",levid);
            startActivity(intent1);

            finish();
        }
        if(levid<9){
            Intent intent1 = new Intent(QuestionScreen.this, SubLevel.class);
            intent1.putExtra("Level1",two.card1.get(levid).getlevelnum());
            intent1.putExtra("Levelname",two.card1.get(levid).getlevelname());
            intent1.putExtra("img",two.card1.get(levid).getimg());
            intent1.putExtra("prog",two.card1.get(levid).geprog());
            intent1.putExtra("id",levid);
            startActivity(intent1);}
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return cm.getActiveNetworkInfo() != null && networkInfo.isConnected();
    }
}


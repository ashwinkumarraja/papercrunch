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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Playground extends AppCompatActivity {

    EditText editText;
    Editable tocompile;
    String compile1,compile2;
    TextView result;
    Button comp;

    MainActivity one;

    Context mContext;
    android.support.v7.widget.Toolbar custom_toolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    NavigationView navigationView;
    ExpandableListView mExpandableListView;
    android.widget.ExpandableListAdapter mExpandableListAdapter;
    List<String> listheader;
    HashMap<String, List<String>> listchild;

    public static List<String> lev;//Will contain all the sublevels of particular level.
    public static List<String> head2;
    public static List<CardData> card1;//Contains CardData objects of different levels.
    public static List<String> c1,c2,c3;//Contains concpets of the levels.


    LevelDbHelper levelDbHelper;//Object to access all methods in class.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);


        setuptoolbar();

        card1 = new ArrayList<>();//Initializing variables

        one=new MainActivity();//Initializing variables
        final LevelDbHelper levelDbHelper = new LevelDbHelper(this);//Initializing variables

        //Hamburger menu Tiles.
        listheader = new ArrayList<String>();
        listchild = new HashMap<String, List<String>>();
        listheader.add("View All Sub Levels");
        listheader.add("View Previous Level");
        listheader.add("View Progress Cycle");
        listheader.add("");
        listheader.add("");
        listheader.add("Code Playground");
        listheader.add("Settings");
        listheader.add("Rate us");
        listheader.add("Save Your Progress");

        one=new MainActivity();//Initialising the variables
        mContext=this;//Initialising the variables

        final int sid=levelDbHelper.getcurrlev(one.datavase);//Returns Current Level.
        c1=levelDbHelper.getconcept1(one.datavase,sid);//Returns Concept 1 of all sublevels of that Level.
        c2=levelDbHelper.getconcept2(one.datavase,sid);//Returns Concept 2 of all sublevels of that Level.
        c3=levelDbHelper.getconcept3(one.datavase,sid);//Returns Concept 3 of all sublevels of that Level.

        lev = levelDbHelper.readSubLevel(one.datavase,sid);//Returns the sublevels of that Level.
        head2=levelDbHelper.getprev(one.datavase,sid);//Returns previous Levels before that level.

        listchild.put(listheader.get(0),lev);//Hamburger Menu Tile 1.
        listchild.put(listheader.get(1), head2);//Hamburger Menu Tile 2.

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
                    intent = new Intent(Playground.this,ConceptScreen.class);
                    //One must pass all the intents required to infiltrate the content in the Activity.
                    intent.putExtra("con1",c1.get(childPosition));
                    intent.putExtra("con2",c2.get(childPosition));
                    intent.putExtra("con3",c3.get(childPosition));
                    intent.putExtra("subname",lev.get(childPosition));
                    intent.putExtra("levelid",sid);
                    intent.putExtra("levelname",card1.get(childPosition).getlevelname());
                    startActivity(intent);
                }
                else if(groupPosition==1)
                {
                    Intent intent = new Intent(mContext,SubLevel.class);
                    //One must pass all the intents required to infiltrate the content in the Activity.
                    intent.putExtra("Level1",card1.get(childPosition).getlevelnum());
                    intent.putExtra("Levelname",card1.get(childPosition).getlevelname());
                    intent.putExtra("img",card1.get(childPosition).getimg());
                    intent.putExtra("prog",card1.get(childPosition).geprog());
                    intent.putExtra("id",childPosition+1);
                    mContext.startActivity(intent);
                }
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
                else if(groupPosition==2 ){
                    Intent i = new Intent(mContext,TreeFlow.class);
                    startActivity(i);
                }
                else if(groupPosition==5)
                {if (!isNetworkConnected()) {
                    new AlertDialog.Builder(Playground.this)
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
                        new AlertDialog.Builder(Playground.this)
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
                    DataDbHelper dataDbHelper = new DataDbHelper(Playground.this);

                    SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    final String token = sp.getString("token", "");
                    Toast.makeText(Playground.this, token, Toast.LENGTH_SHORT).show();
                    Call<Void> call = gda.sync(levelDbHelper.getcurrlev(one.datavase), dataDbHelper.getStars(one.datavase)+sp.getInt("totalstars", 0), sp.getInt("id_avatar", 0), "Token " + token);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Code: " + response.code(), "lol");
                                Toast.makeText(Playground.this, "Failed Please try again!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("failed: ", t.getMessage());
                            Toast.makeText(Playground.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(Playground.this, "Sync Successful!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                            Log.d("failed poke: ", t.getMessage());
                            Toast.makeText(Playground.this, "Failed Please try again!!!!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Playground.this, "Failed Please try again!!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (response.code() == 200) {

                                Toast.makeText(Playground.this, "Sync Successful!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                            Log.d("failed poke: ", t.getMessage());
                            // Toast.makeText(Main2Activity.this, "Failed Please try again!!!!", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(Playground.this,"Please make sure you are connected to the internet",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

                return true;
            }
        });


        editText = (EditText)findViewById(R.id.EnterCode);
        editText.setText("#include <stdio.h>\n\nint main(){\n\tprintf(\"Hello world\");\n}");//Sets text of editText as this code initially.
        tocompile = editText.getText();//toCompile contains the content of the EditText to compile.
        result = (TextView)findViewById(R.id.compiled);//Contains the content of the end output.

        comp=(Button)findViewById(R.id.compilebutt);

        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("https://papercrunchapp.herokuapp.com/").addConverterFactory(GsonConverterFactory.create())
                        .build();
                final getdataApi gda=retrofit.create(getdataApi.class);
                SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                final String token=sp.getString("token","");


                Call<Code> call2 = (Call<Code>) gda.sendcode("Token " + token,tocompile);
                call2.enqueue(new Callback<Code>() {
                    @Override
                    public void onResponse(Call<Code> call, Response<Code> response) {
                        if (!response.isSuccessful()) {

                            Log.d("Code poker: " + response.code(), "Anything");
                            return;
                        }
                        Code code = response.body();
                        compile1 = code.getCompiled_result();
                        compile2 = code.getRun_result();
                        if(compile1==null)
                            result.setText("Compile Result : Success"+"\n"+"Output"+compile2);
                        else
                            result.setText("Compile Result : "+compile1+"\n"+"Output : "+compile2);
                    }

                    @Override
                    public void onFailure(Call<Code> call, Throwable t) {

                        Log.d("failed poke: ", t.getMessage());
                        //Toast.makeText(Main2Activity.this, "Failed Please try again!!!!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Playground.this,"Please make sure you are connected to the internet",Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }


    @Override
    public void onBackPressed(){
        Intent omg = new Intent(Playground.this,Main2Activity.class);//Redirects to Main2Activity.
        startActivity(omg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        ImageeAdapter imageeAdapter=new ImageeAdapter(this);
        menuInflater.inflate(R.menu.onerflow_menu, menu);
        final SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        int asd=sp.getInt("id_avatar",0);
        menu.findItem(R.id.avatar).setIcon(imageeAdapter.image_id2[asd]);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Intent i=new Intent(Playground.this,IdScreen.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    public void setuptoolbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            custom_toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.custom_toolbar);
        }
        setSupportActionBar(custom_toolbar);

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return cm.getActiveNetworkInfo() != null && networkInfo.isConnected();
        //Returns true if connected to the internet.
    }
}

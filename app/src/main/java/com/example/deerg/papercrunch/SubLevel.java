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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class SubLevel extends AppCompatActivity {
    //SQLiteDatabase datavase;
    MainActivity one;
    Main2Activity two;
    android.support.v7.widget.Toolbar custom_toolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    NavigationView navigationView;
    ExpandableListView mExpandableListView;
    android.widget.ExpandableListAdapter mExpandableListAdapter;
    List<String> listheader;
    HashMap<String, List<String>> listchild;
    public static String c1,c2,c3;
    public static List<String> lev;
    public static List<String> head2;
    public static List<String> concept1;
    public static List<String> concept2;
    public static List<String> concept3;
    public static String levelnam;
    public static int id;//LEvel id
    public static int SizeOfLevel;
    public static int progress;
    public static int sid;
    int x;
    Context mContext;
    com.example.deerg.papercrunch.LevelDbHelper levelDbHelper;
    int idiot;
    int k=0;
    int levid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_level);
        mContext=this;
        setuptoolbar();

        Intent intet = getIntent();
        id = intet.getExtras().getInt("id");
        levid = id-1;
        idiot = id;
        one=new MainActivity();
        two=new Main2Activity();
        levelDbHelper = new LevelDbHelper(this);
        sid=levelDbHelper.getcurrlev(one.datavase);



        progress=levelDbHelper.getprogress(one.datavase,sid);
        if(progress==100&&sid%2!=0) {
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            boolean firstStart = prefs.getBoolean("firstStart", true);
            if (firstStart) {
                showStartDialog();
            }
        }







        //lev = new ArrayList();
        ListView list = (ListView)findViewById(R.id.sublist);

        lev = levelDbHelper.readSubLevel(one.datavase,id);


        CardData cardDatasub = levelDbHelper.readLevel(levid+1,one.datavase);


        levelnam =  cardDatasub.getlevelname();
        String levelnum = cardDatasub.getlevelnum();
        int imga = cardDatasub.getimg();
        int progr = cardDatasub.geprog();

        TextView lvln =(TextView)findViewById(R.id.textcard1);
        TextView lvlnn =(TextView)findViewById(R.id.textlvl1);
        ImageView img = (ImageView)findViewById(R.id.card1pic);
        TextView progress = (TextView)findViewById(R.id.prog);

        lvln.setText(levelnum);
        lvlnn.setText(levelnam);
        img.setImageResource(imga);
        progress.setText(Integer.toString(progr));



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
        listheader.add("Save your Progress");

        head2=levelDbHelper.getprev(one.datavase,id);

        listchild.put(listheader.get(0), lev);
        listchild.put(listheader.get(1), head2);

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

        concept1=levelDbHelper.getconcept1(one.datavase,id);
        concept2=levelDbHelper.getconcept2(one.datavase,id);
        concept3=levelDbHelper.getconcept3(one.datavase,id);

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition==0)
                {
                    TextView textView = (TextView) findViewById(R.id.subt);
                    Intent intent;
                    c1=concept1.get(childPosition);
                    c2=concept2.get(childPosition);
                    c3=concept3.get(childPosition);
                    intent = new Intent(SubLevel.this,ConceptScreen.class);
                    intent.putExtra("con1",c1);
                    intent.putExtra("con2",c2);
                    intent.putExtra("con3",c3);
                    intent.putExtra("subname",textView.getText());
                    intent.putExtra("levelid",idiot);
                    intent.putExtra("levelname",levelnam);

                    startActivity(intent);
                }
                else if(groupPosition==1)
                {
                    Intent intent = new Intent(mContext,SubLevel.class);
                    intent.putExtra("Level1",two.card1.get(childPosition).getlevelnum());
                    intent.putExtra("Levelname",two.card1.get(childPosition).getlevelname());
                    intent.putExtra("img",two.card1.get(childPosition).getimg());
                    intent.putExtra("prog",two.card1.get(childPosition).geprog());
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
                    new AlertDialog.Builder(SubLevel.this)
                            .setMessage("Please check your internet connection")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                } else {
                    Intent i=new Intent(mContext,Playground.class);
                    startActivity(i);}
                }
                else if (groupPosition == 6) {
                    Intent i = new Intent(mContext, settings.class);
                    startActivity(i);
                } else if (groupPosition == 8) {
                    if (!isNetworkConnected()) {
                        new AlertDialog.Builder(SubLevel.this)
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
                    DataDbHelper dataDbHelper = new DataDbHelper(SubLevel.this);

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
                            Toast.makeText(SubLevel.this, "Please make sure you are connected to the internet", Toast.LENGTH_SHORT).show();
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

        ArrayAdapter<String> adap = new ArrayAdapter<String>(this,R.layout.sublevel_text,lev);
        list.setAdapter(adap);
        SizeOfLevel = lev.size();
        /*for(int i=0;i<SizeOfLevel;i++){
        String sub1 = lev.get(1);
        int sub1id = levelDbHelper.readSubid(sub1,one.datavase);
        int subool = levelDbHelper.getbool(sub1id,one.datavase);
        }*/


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TextView textView = (TextView) findViewById(R.id.subt);
                String sub = lev.get(position);
                Intent intent;
                c1=concept1.get(position);
                c2=concept2.get(position);
                c3=concept3.get(position);
                intent = new Intent(SubLevel.this,ConceptScreen.class);
                intent.putExtra("con1",c1);
                intent.putExtra("con2",c2);
                intent.putExtra("con3",c3);
                intent.putExtra("subname",sub);
                intent.putExtra("levelname",levelnam);
                intent.putExtra("levelid",idiot);//levlid
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,Main2Activity.class);
        startActivity(i);
    }
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

    public void setuptoolbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            custom_toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.custom_toolbar);
        }
        setSupportActionBar(custom_toolbar);
    }
    private void showStartDialog(){
        new AlertDialog.Builder(this).setTitle("Congratulations").setMessage("You have unlocked a new medal , tap your avatar to view!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } )
                .create().show();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();



}
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return cm.getActiveNetworkInfo() != null && networkInfo.isConnected();
    }
}

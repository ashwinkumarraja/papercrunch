package com.example.deerg.papercrunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.deerg.papercrunch.R;

import java.util.ArrayList;
import java.util.List;

public class TreeFlow extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    public static List<String> lld;
    MainActivity one;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_layout);


        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        one=new MainActivity();
        LevelDbHelper levelDbHelper=new LevelDbHelper(this);
        lld=levelDbHelper.getsubname(one.datavase);
        for(int i=0,j=0;i<25 && j<13; i=i+2, j++){
            MyData.nameArray[j]=lld.get(i);
            MyData.nameArrays[j]=lld.get(i+1);
        }
        MyData.nameArray[13]="Strings";
        MyData.nameArrays[13]="The End";

        data = new ArrayList<>();
        for (int i=0,j=0;i<14 && j<25; i++, j+=2) {
            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.id_[i],
                    MyData.drawableArray[j],
                    MyData.drawableArray[j+1],
                    MyData.nameArrays[i]
            ));
        }
        data.add(new DataModel(
                MyData.nameArray[13],
                MyData.id_[13],
                MyData.drawableArray[25],
                R.drawable.medal4,
                MyData.nameArrays[13]
        ));

        removedItems = new ArrayList<>();

        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);




    }









}
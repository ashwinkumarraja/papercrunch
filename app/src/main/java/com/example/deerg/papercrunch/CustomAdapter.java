package com.example.deerg.papercrunch;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deerg.papercrunch.LevelDbHelper;
import com.example.deerg.papercrunch.MainActivity;
import com.example.deerg.papercrunch.R;


import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.deerg.papercrunch.ConceptScreen.lev;
import static com.example.deerg.papercrunch.ConceptScreen.levid;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;
   // OnCardClickListner onCardClickListner;

    Context context;

    MainActivity one;
    LevelDbHelper levelDbHelper;
    SQLiteDatabase datavase;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        ImageView imageViewIcons;
        TextView textView_new;

        CardView cardView,cardView_new;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);

            this.imageViewIcons = (ImageView) itemView.findViewById(R.id.imagesss);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.cardView_new = (CardView) itemView.findViewById(R.id.card_view_new);
            this.textView_new = (TextView) itemView.findViewById(R.id.text_new);
        }
    }

    public CustomAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tree_view_layout, parent, false);

        context = parent.getContext();

        one =new MainActivity();

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        TextView textView_new = holder.textView_new;

        final CardView cardViews = holder.cardView;
        CardView cardViews_new = holder.cardView_new;

        ImageView imageViews = holder.imageViewIcons;
        imageViews.setImageResource(R.drawable.avatar);

        textViewName.setText(dataSet.get(listPosition).getName());

        imageView.setImageResource(dataSet.get(listPosition).getImage());
        imageViews.setImageResource(dataSet.get(listPosition).getImage());


        textView_new.setText(dataSet.get(listPosition).getName_second());
        final LevelDbHelper levelDbHelper = new LevelDbHelper(context);
        DataDbHelper dataDbHelper = new DataDbHelper(context);
        one = new MainActivity();
        int id = levelDbHelper.readSubid(dataSet.get(listPosition).name.toString(),one.datavase);
        if(levelDbHelper.getbool(id,one.datavase)==1)
            holder.cardView.setCardBackgroundColor(Color.GREEN);
        if(levelDbHelper.getbool(id,one.datavase)==1)
            holder.cardView_new.setCardBackgroundColor(Color.GREEN);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = levelDbHelper.readSubid(dataSet.get(listPosition).name.toString(),one.datavase);
                int sid = levelDbHelper.readlvvlid(dataSet.get(listPosition).name.toString(),one.datavase);
                String c1 = levelDbHelper.gc1(dataSet.get(listPosition).name.toString(),one.datavase);
                String c2 = levelDbHelper.gc2(dataSet.get(listPosition).name.toString(),one.datavase);
                String c3 = levelDbHelper.gc3(dataSet.get(listPosition).name.toString(),one.datavase);
                String  lvlname = levelDbHelper.readlvname(sid,one.datavase);
               Intent intent;
                //One must pass all the intents required to infiltrate the content in the Activity.
                intent = new Intent(context,ConceptScreen.class);
                intent.putExtra("con1",c1);//Passes the concept 1 of that Sublevel.
                intent.putExtra("con2",c2);//Passes the concept 2 of that Sublevel.
                intent.putExtra("con3",c3);//Passes the concept 3 of that Sublevel.
                intent.putExtra("subname",dataSet.get(listPosition).name.toString());//Passes Sublevel Name.
                intent.putExtra("levelid",sid);//Passes Level id.
                intent.putExtra("levelname",lvlname);//Passes the Levelname.
                context.startActivity(intent);

            }


        });

        holder.cardView_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sid = levelDbHelper.readlvvlid(dataSet.get(listPosition).name_second.toString(),one.datavase);
                String c1 = levelDbHelper.gc1(dataSet.get(listPosition).name_second.toString(),one.datavase);
                String c2 = levelDbHelper.gc2(dataSet.get(listPosition).name_second.toString(),one.datavase);
                String c3 = levelDbHelper.gc3(dataSet.get(listPosition).name_second.toString(),one.datavase);
                String  lvlname = levelDbHelper.readlvname(sid,one.datavase);
                Intent intent;
                //One must pass all the intents required to infiltrate the content in the Activity.
                intent = new Intent(context,ConceptScreen.class);
                intent.putExtra("con1",c1);//Passes the concept 1 of that Sublevel.
                intent.putExtra("con2",c2);//Passes the concept 2 of that Sublevel.
                intent.putExtra("con3",c3);//Passes the concept 3 of that Sublevel.
                intent.putExtra("subname",dataSet.get(listPosition).name_second.toString());//Passes Sublevel Name.
                intent.putExtra("levelid",sid);//Passes Level id.
                intent.putExtra("levelname",lvlname);//Passes the Levelname.
                context.startActivity(intent);

            }


        });


    }



    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

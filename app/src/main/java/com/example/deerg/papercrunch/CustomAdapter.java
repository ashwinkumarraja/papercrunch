package com.example.deerg.papercrunch;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import static android.content.ContentValues.TAG;
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

        CardView cardViews = holder.cardView;
        CardView cardViews_new = holder.cardView_new;

        ImageView imageViews = holder.imageViewIcons;
        imageViews.setImageResource(R.drawable.avatar);

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getVersion());
        imageView.setImageResource(dataSet.get(listPosition).getImage());


        textView_new.setText(dataSet.get(listPosition).getName_second());




        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 levelDbHelper.getprogress(one.datavase,1);

            //    Log.d(TAG, "newww: " + a);

                if (dataSet.get(listPosition).getName().equals("Level 1")) {

                //    int prog =1;



                   /* Intent intent = new Intent(context, SecondActivity.class);
                    context.startActivity(intent);*/

                } else if (dataSet.get(listPosition).getName().equals("Level 2")) {
                    Toast.makeText(context, "Level 1", Toast.LENGTH_SHORT).show();
                }

                // onCardClickListner.OnCardClicked(v, listPosition);

            }


        });

        holder.cardView_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClickssss: ");

                if (dataSet.get(listPosition).getName().equals("Android Name")) {

                   /* Intent intent = new Intent(context, SecondActivity.class);
                    context.startActivity(intent);*/

                } else if (dataSet.get(listPosition).getName().equals("Level 2")) {
                    Toast.makeText(context, "Level 2", Toast.LENGTH_SHORT).show();
                }

                // onCardClickListner.OnCardClicked(v, listPosition);

            }


        });


    }



    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

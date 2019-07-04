package com.example.deerg.papercrunch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LevelViewHolder> {
    MainActivity one;//Creates MainActivity object to access all variables and data in MainActivity.
    LevelDbHelper levelDbHelper;//Creates object to access all methods in class file.

    private Context mContext;
    public static List<CardData> cardDataList;
    RecyclerAdapter(Context context,List<CardData> cardDataList){
        this.mContext =context;
        this.cardDataList = cardDataList;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mv = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cardview,viewGroup,false);
        return new LevelViewHolder(mv);
    }

    @Override
    public void onBindViewHolder(@NonNull final LevelViewHolder levelViewHolder, final int i) {
        levelViewHolder.mImage.setImageResource(cardDataList.get(i).getimg());
        levelViewHolder.mlevelnum.setText(cardDataList.get(i).getlevelnum());
        levelViewHolder.mlevelnam.setText(cardDataList.get(i).getlevelname());
        levelViewHolder.mprog.setText(Integer.toString(cardDataList.get(i).geprog()));
        levelViewHolder.levback.setImageResource(cardDataList.get(i).getbackid());

       levelViewHolder.carview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelDbHelper=new LevelDbHelper(mContext);
                one =new MainActivity();
                levelDbHelper.updatecurrlev(one.datavase,i+1);
                Intent intent = new Intent(mContext,SubLevel.class);
                intent.putExtra("Level1",cardDataList.get(i).getlevelnum());
                intent.putExtra("Levelname",cardDataList.get(i).getlevelname());
                intent.putExtra("img",cardDataList.get(i).getimg());
                intent.putExtra("id",cardDataList.get(i).getid());
                intent.putExtra("prog",cardDataList.get(i).geprog());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
                //((Activity)mContext).recreate();
            }
        });

    }


    @Override
    public int getItemCount() {
        return cardDataList.size();
    }


    class LevelViewHolder extends RecyclerView.ViewHolder{
        ImageView mImage;
        TextView mlevelnum;
        TextView mlevelnam;
        CardView carview;
        TextView mprog;
        ImageView levback;
        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            carview = itemView.findViewById(R.id.carview);
            mImage = itemView.findViewById(R.id.card1pic);
            mlevelnam = itemView.findViewById(R.id.textlvl1);
            mlevelnum = itemView.findViewById(R.id.textcard1);
            mprog = itemView.findViewById(R.id.prog);
            levback = itemView.findViewById(R.id.background);

        }
    }
}

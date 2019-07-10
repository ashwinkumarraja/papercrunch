package com.example.deerg.papercrunch;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.deerg.papercrunch.ConceptScreen.sid;

public class ImageAdapter extends BaseAdapter {
    public Integer i=0;
    private Context CTX;
int j;
    Ans_Popup is=new Ans_Popup();
    int sid;
    int progress;


    private Integer image_id[] = {R.drawable.medal,R.drawable.medal2,R.drawable.medal3,R.drawable.medal4,R.drawable.medal5,R.drawable.medal6,R.drawable.medal7,R.drawable.medal8,R.drawable.medal9,R.drawable.medal10,R.drawable.medal11,R.drawable.medal12,R.drawable.medal13,R.drawable.medal14,R.drawable.medal15};
    public ImageAdapter(Context CTX){
        this.CTX=CTX;

    LevelDbHelper levelDbHelper = new LevelDbHelper((Activity)CTX);
    MainActivity one = new MainActivity();
    sid=levelDbHelper.getcurrlev(one.datavase);
    for(i=1;i<=9;i++)
    {
        progress += levelDbHelper.getprogress(one.datavase,i);
    }
   }
    @Override
    public int getCount() {
        return image_id.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img;
        if(convertView==null){
            img=new ImageView(CTX);
            img.setLayoutParams(new GridView.LayoutParams(160,160));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setPadding(8,8,8,8);
            //Toast.makeText(CTX,progress + " " + sid + " " + position,Toast.LENGTH_SHORT).show();
        }
        else {
            img=(ImageView) convertView;
        }
        if((position<=is.stars5/6&&position%2==0&&is.stars5>=6)){
            img.setImageResource(image_id[position]);
            img.setVisibility(View.VISIBLE);
            return img;
        }
        if(position<=progress/100 && position%2!=0) {
            img.setImageResource(image_id[position]);
            img.setVisibility(View.VISIBLE);
            return img;
        }
        else {
                img.setImageResource(image_id[position]);
                img.setVisibility(View.GONE);
                return img;
            }
        }

    }



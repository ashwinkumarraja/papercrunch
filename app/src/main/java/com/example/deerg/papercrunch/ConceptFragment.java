package com.example.deerg.papercrunch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ConceptFragment extends Fragment {

    private TextView tv1;

    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private ImageView iv5;
    private ImageView iv6;
    public ConceptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_concept, container, false);

        tv1=view.findViewById(R.id.tv_frag);
        String msg=getArguments().getString("msg");
        tv1.setText(msg);

        iv1=view.findViewById(R.id.dot1_on);
        iv2=view.findViewById(R.id.dot1_off);
        iv3=view.findViewById(R.id.dot2_on);
        iv4=view.findViewById(R.id.dot2_off);
        iv5=view.findViewById(R.id.dot3_on);
        iv6=view.findViewById(R.id.dot3_off);

        int i=getArguments().getInt("coni");

        if(i==1)
        {
            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.INVISIBLE);
            iv3.setVisibility(View.INVISIBLE);
            iv4.setVisibility(View.VISIBLE);
            iv5.setVisibility(View.INVISIBLE);
            iv6.setVisibility(View.VISIBLE);
        }
        else if(i==2)
        {
            iv1.setVisibility(View.INVISIBLE);
            iv2.setVisibility(View.VISIBLE);
            iv3.setVisibility(View.VISIBLE);
            iv4.setVisibility(View.INVISIBLE);
            iv5.setVisibility(View.INVISIBLE);
            iv6.setVisibility(View.VISIBLE);
        }
        else if(i==3) {
            iv1.setVisibility(View.INVISIBLE);
            iv2.setVisibility(View.VISIBLE);
            iv3.setVisibility(View.INVISIBLE);
            iv4.setVisibility(View.VISIBLE);
            iv5.setVisibility(View.VISIBLE);
            iv6.setVisibility(View.INVISIBLE);

            ConceptScreen.quiztime.setVisibility(View.VISIBLE);
        }


        return view;
    }

}

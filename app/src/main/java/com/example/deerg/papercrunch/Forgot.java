package com.example.deerg.papercrunch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Forgot extends AppCompatActivity {
    EditText Email;
    String email,password;
    AlertDialog.Builder builder;
    String reg_url="https://papercrunchapp.herokuapp.com/api/reset-password/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Email = (EditText) findViewById(R.id.email_5);
        builder = new AlertDialog.Builder(Forgot.this);
    }
    public void Forgot(View view){
        email=Email.getText().toString();
        if(email.equals(""))
        {
            builder.setTitle("Something Went Wrong...");
            builder.setMessage("Please Enter your registered Email to retrieve password ");
            displayAlert("input_error");
        }
        else {
            builder.setTitle("NOTE");
            builder.setMessage("Please check your email id to see your new password ");
            displayAlert("input_error");
            view.setClickable(false);
            StringRequest stringRequest= new StringRequest(Request.Method.POST, reg_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONArray jsonArray= new JSONArray(response);
                        JSONObject jsonObject= jsonArray.getJSONObject(0);
                        String code=jsonObject.getString("code");
                        String message=jsonObject.getString("message");
                        builder.setTitle("Server Response...");
                        displayAlert(code);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params= new HashMap<String, String>();
                    params.put("email",email);
                    return params;
                }
            };
            MySingleton.getInstance(Forgot.this).addToRequest(stringRequest);
        }
    }
    public void displayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(Forgot.this,login.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

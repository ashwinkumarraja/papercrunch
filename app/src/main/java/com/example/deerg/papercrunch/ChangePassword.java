package com.example.deerg.papercrunch;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class ChangePassword extends AppCompatActivity {
    EditText Password,New_password;
    String password,new_password;
    AlertDialog.Builder builder;
    String reg_url="https://papercrunchapp.herokuapp.com/api/change-password/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        builder=new AlertDialog.Builder(ChangePassword.this);
        Password=(EditText)findViewById(R.id.current_1);
        New_password=(EditText)findViewById(R.id.new_1);

    }
    public void Ok(View view){
        password=Password.getText().toString();
        new_password=New_password.getText().toString();
        if(password.equals("")||new_password.equals(""))
        {
            builder.setTitle("Something Went Wrong...");
            builder.setMessage("Please Fill All The Fields...");
            displayAlert("input_error");
        }
        else{
            StringRequest stringRequest= new StringRequest(Request.Method.POST, reg_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONArray jsonArray= new JSONArray(response);
                        JSONObject jsonObject= jsonArray.getJSONObject(0);
                        String code=jsonObject.getString("code");
                        String message=jsonObject.getString("message");
                        String non_field_errors=jsonObject.getString("non_field_errors");
                        if(non_field_errors.equals("true"))
                        {
                            builder.setTitle("Something Went Wrong...");
                            builder.setMessage("The password you entered is wrong");
                            displayAlert("input_error");

                        }
                        else if(response==null) {
                            Intent intent35=new Intent(ChangePassword.this,MainActivity.class);
                            startActivity(intent35);
                        }
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
                SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                final String token=sp.getString("token","");
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params= new HashMap<String, String>();

                    //params.put("password",password);
                    //params.put("new_password",new_password);
                    params.put("Authorization","Token "+ token);
                    //params.put("google",google);
                    return params;
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params= new HashMap<String, String>();

                    params.put("password",password);
                    params.put("new_password",new_password);
                   // params.put("Authorization", token);
                    //params.put("google",google);
                    return params;
                }
            };
            MySingleton.getInstance(ChangePassword.this).addToRequest(stringRequest);
            Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
        }
    }
    public void displayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.equals("input_error")){
                    Password.setText("");
                    New_password.setText("");
                }
                else if (code.equals("reg_success")){
                    finish();
                }
                else if(code.equals("reg_failure")){
                    Password.setText("");
                    New_password.setText("");

                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
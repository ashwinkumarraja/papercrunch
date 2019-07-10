package com.example.deerg.papercrunch;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class Registeration extends AppCompatActivity {
    EditText FirstName,Email,Password,LastName,Mobile;
    String first_name,email,password,last_name,phone_number;
    AlertDialog.Builder builder;
    String reg_url="https://papercrunchapp.herokuapp.com/api/register/";
    // public boolean google=false;
    CheckBox chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        FirstName=(EditText)findViewById(R.id.fname_3);
        Email=(EditText)findViewById(R.id.email_3);
        LastName=(EditText)findViewById(R.id.lname_3);
        Password=(EditText)findViewById(R.id.pass_3);
        //Mobile=(EditText)findViewById(R.id.mno_3);
        builder=new AlertDialog.Builder(Registeration.this);

        chk=(CheckBox)findViewById(R.id.check_1);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b){
                    Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                }
                else {
                    Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }
    public void Create(View view){
        first_name=FirstName.getText().toString();
        last_name=LastName.getText().toString();
        email=Email.getText().toString();
        password=Password.getText().toString();
       // phone_number=Mobile.getText().toString();
        if(first_name.equals("")||last_name.equals("")||email.equals("")||password.equals("")){
            builder.setTitle("Something Went Wrong...");
            builder.setMessage("Please Fill All The Fields...");
            displayAlert("input_error");
        }
        else{
            if(password.length()<6)
            {
                builder.setTitle("Something Went Wrong...");
                builder.setMessage("Your password must contain atleast 6 characters");
                displayAlert("input_error");
            }
            else {
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
                        params.put("password",password);
                        params.put("first_name",first_name);
                        params.put("last_name",last_name);
                        //params.put("phone_number",phone_number);
                        //params.put("google",google);
                        return params;
                    }
                };
                MySingleton.getInstance(Registeration.this).addToRequest(stringRequest);
                Intent intent15 = new Intent(this, login.class);
                Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();
                startActivity(intent15);
                finish();

            }
        }
    }
    public void displayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.equals("input_error")){
                    Password.setText("");
                }
                else if (code.equals("reg_success")){
                    finish();
                }
                else if(code.equals("reg_failure")){
                    FirstName.setText("");
                    LastName.setText("");
                    Email.setText("");
                    Password.setText("");
                    //Mobile.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return cm.getActiveNetworkInfo() != null && networkInfo.isConnected();
    }

}
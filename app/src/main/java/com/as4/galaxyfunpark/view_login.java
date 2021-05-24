package com.as4.galaxyfunpark;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by SaMi on 1/8/2018.
 */

public class view_login extends view_main
{

    String usertype;
Button login  ;
    String parameter = "";
    Intent goTo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isLoginPagge = true ;
        setContentView(R.layout.view_login);
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        login = (Button) findViewById(R.id.button);


    }


    public void click_login (View view) throws JSONException {



        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        if (!Validation.hasText(username)) {
            Toast.makeText(this, "Insert username", Toast.LENGTH_LONG).show();

            return;
        }
        if (!Validation.hasText(password)) {
            Toast.makeText(this, "Insert password", Toast.LENGTH_LONG).show();

            return;
        }
        login.setVisibility(View.INVISIBLE);
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        parameter = "?type=LoginSQL&user=" + username.getText().toString() + "&pass=" + password.getText().toString()+
                "&Date=" + formattedDate;

        goTo = new Intent(this, view_home.class);

        String path = session.url + parameter.replace(" ", "%20");;
     //   webservices web = new webservices();
        Log.d("pp",path);

      //  String josn = web.downloadFile(path);
        String UserID = "0";
        String Place = "0";
        String name = "0";

        JSONArray jArr =  getDataArray (path) ;


            for (int i = 0; i < jArr.length(); i++)
            {

                JSONObject obj = jArr.getJSONObject(i);
                UserID = obj.getString("EMPLOYEE_PMK_ID");
                if (!UserID.equals("0")) {
                    Place = obj.getString("EMPLOCATION_FNK_POINT");

                    name = obj.getString("EMPLOYEE_NAME");
                }

            }



                if (!UserID.equals("0")) {
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show();
                    Log.d("Place",Place);
           session.userID = UserID;
                    session.name = name;
                    session.place =Place;
                    startActivity(goTo);


        }


           else {
                    login.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_LONG).show();}



    }





}


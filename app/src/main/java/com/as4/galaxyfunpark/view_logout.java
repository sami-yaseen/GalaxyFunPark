package com.as4.galaxyfunpark;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by SaMi on 1/10/2018.
 */

public class view_logout extends view_main {

    String usertype;
    Button login  ;
    String parameter = "";
    Intent goTo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.view_logout);
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
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df1.format(c1.getTime());
        parameter = "?type=Logout&user=" + username.getText().toString() + "&pass=" + password.getText().toString()+
                "&id=" + session.userID +
                "&Date=" + formattedDate;
        String path = session.url + parameter.replace(" ", "%20");;

        String UserID = "0";



        JSONArray jArr =  getDataArray (path) ;
            for (int i = 0; i < jArr.length(); i++)
            {
                JSONObject obj = jArr.getJSONObject(i);

                    try
                    {
                    UserID = obj.getString("EMPLOYEE_PMK_ID");
                    }
                    catch (Exception ee) {
                    UserID = "0" ;
                    }

            }




        if (!UserID.equals("0")) {
            Toast.makeText(this, "LogOut Successfully", Toast.LENGTH_LONG).show();

            session.userID = "0";
            session.name = "0";
            session.place = "0";
            session.SPID = "0";
            session.SName = "0";
            session.Date="0";

            Intent goTo = new Intent(this, view_login.class);
            startActivity(goTo);

        }
        else {
            login.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_LONG).show();
            }

    }



}

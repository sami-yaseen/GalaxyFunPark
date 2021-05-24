package com.as4.galaxyfunpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by SaMi on 1/10/2018.
 */

public class view_sales extends view_main {

    TextView Date;
    TextView SName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.view_sales);
        super.onCreate(savedInstanceState);

        Date=(TextView)findViewById(R.id.Date);

        SName  =(TextView)findViewById(R.id.Name);


        SName.setText(session.SName);



        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        session.Date=formattedDate;
        Log.d("SD", session.Date);
        java.util.Date Curentdate = null;
        try {
            Curentdate = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date.setText(formattedDate);
    }





    public void Save(View v)  {

        Intent goTo = new Intent(this, view_sales_next.class);
        startActivity(goTo);


    }










}

package com.as4.galaxyfunpark;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by SaMi on 1/10/2018.
 */

public class view_return extends view_main {
    Double PriceTotal =0.0 ;
    Double DiscountT =0.0 ;
    int Time= 0 ;
    Double fixedPrice =0.0 ;
    Double maxPrice =0.0 ;
    Double firstHour=0.0;
    Boolean ISFIXED  =true;
    Boolean ISFIXEDINCREMENTAL=false;
    String formattedDate="" ;
    Double incrementalPrice=0.0 ;
    String TransID= "0" ;
    String DiscountR= "" ;
    String[] hour ;
    String StartT= "0" ;
    String EndT= "0" ;
    String DateT= "0" ;
    Button payB  ;

    String printBill = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setContentView(R.layout.view_return);
        super.onCreate(savedInstanceState);


        payB = (Button) findViewById(R.id.button);
        EditText Cash= (EditText) findViewById(R.id.Cash);
        Cash.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

               calculateChangeBack();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });


        EditText Discount= (EditText) findViewById(R.id.Discount);
        Discount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                calculateDiscount();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        try
        {
            this.GetPriceSiting();
            this.fillReturn();
        } catch(
                JSONException e)

        {
            e.printStackTrace();

        }



    }


    private void GetPriceSiting() throws JSONException {

      //  webservices web= new  webservices();
        String parameter= "?type=PriceSetting&SID="+ session.SPID;
        String path = session.url+parameter;

        Log.d("ddd",path);

        JSONArray jArr =  getDataArray (path) ;


        for (int i=0; i < jArr.length(); i++)
        {
            JSONObject obj = jArr.getJSONObject(i);


try
{
    fixedPrice = Double.parseDouble( obj.getString("PRICE_SETTING_FIXEDPRICE"));
}
catch (Exception e)
{
    fixedPrice = 0.0 ;
}
            try
            {
                maxPrice = Double.parseDouble(obj.getString("PRICE_SETTING_MAXPRICE"));
            }
            catch (Exception e)
            {
                maxPrice = 0.0 ;
            }

            try
            {
                firstHour = Double.parseDouble(obj.getString("PRICE_SETTING_FIRSTHOURPRICE"));
            }
            catch (Exception e)
            {
                firstHour = 0.0 ;
            }








            ISFIXED = false ;
            if(obj.getString("PRICE_SETTING_ISFIXED").equals("1"))
                ISFIXED = true ;

            ISFIXEDINCREMENTAL = false ;
             if(obj.getString("PRICE_SETTING_ISFIXEDINCREMENT").equals("1"))
             ISFIXEDINCREMENTAL = true ;


            try
            {
                incrementalPrice = Double.parseDouble(obj.getString("PRICE_SETTING_INCREMENTALPRICE"));
            }
            catch (Exception e)
            {
                incrementalPrice = 0.0 ;
            }


            try
            {
                hour = obj.getString("PRICE_SETTING_HOURPRICE").toString().split(",");
            }
            catch (Exception e)
            {

            }


        }










    }



    private void fillReturn() throws JSONException {

      //  webservices web= new  webservices();
        String parameter= "?type=GetReturn&SID="+ session.SPID;
        String path = session.url+parameter;

        Log.d("ddd",path);

        JSONArray jArr =  getDataArray (path) ;


        for (int i=0; i < jArr.length(); i++)
        {
            JSONObject obj = jArr.getJSONObject(i);

            final String Id=obj.getString("TRANSACTION_PMK_ID");
            TransID =Id ;
            final String Point=obj.getString("TRANSACTION_FNK_POINT");

            TextView QID = (TextView) findViewById(R.id.QID);
           QID.setText(obj.getString("CUSTOMER_QID"));


            TextView Name= (TextView) findViewById(R.id.Name);
           Name.setText(obj.getString("CUSTOMER_NAME"));


            SimpleDateFormat sd1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sd2 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sd3 = new SimpleDateFormat("HH:mm:ss");
            TextView STime= (TextView) findViewById(R.id.STime);
            String Start= obj.getString("TRANSACTION_DATE");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            STime.setText(Start);
            TextView RTime= (TextView) findViewById(R.id.RTime);
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
             formattedDate = df.format(c.getTime());
            RTime.setText(formattedDate);
            TextView TTime= (TextView) findViewById(R.id.TTime);
boolean isfree =  false   ;
            try {


                Date date2 = sdf.parse(Start);
                Date date1 = sdf.parse(formattedDate);

                DateT =sd1.format(date2.getTime());
                StartT =sd2.format(date2.getTime());
                EndT =sd3.format(date1.getTime());
                long mills = date1.getTime() - date2.getTime();
                long hours = mills/(1000 * 60 * 60);
                long freemins = ((mills/(1000*60)) % 60);
                long mins = ((mills/(1000*60)) % 60)-5;
if(hours==0&&freemins<=10)
{
    isfree =  true ;
    PriceTotal =0.0 ;
}
                if(mins>0) {

                    hours++ ;
                }



                 Time= (int) Math.ceil(hours);

            } catch (ParseException ex) {
                Log.d("hh",ex.toString());
                //Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, ex);
            }

            if(!isfree)
            CalculatePrice(Time) ;
            TTime.setText(Time+"");



            TextView TAmountV= (TextView) findViewById(R.id.AmountV);
            TAmountV.setText(PriceTotal+"");

            TextView TAmount= (TextView) findViewById(R.id.TAmount);
            TAmount.setText(PriceTotal+"");
        }










        }
    public void calculateChangeBack()
    {

        EditText Cash= (EditText) findViewById(R.id.Cash);
        TextView Rest = (TextView) findViewById(R.id.Rest);
        if(!Cash.getText().toString().equals("")) {
            Double temp = Double.parseDouble(Cash.getText().toString());
            Double netAmount = PriceTotal- DiscountT ;
            Rest.setText((temp - netAmount) + "");
        }
        else
        {
            Rest.setText( "");
        }
    }
    public void calculateDiscount()
    {

        EditText Discount= (EditText) findViewById(R.id.Discount);
        TextView TAmount= (TextView) findViewById(R.id.TAmount);
        if(!Discount.getText().toString().equals("")) {
            Double temp = Double.parseDouble(Discount.getText().toString());

            if(temp < 0)
            {
                temp = temp * -1 ;
                Discount.setText(temp+"");
            }
            if(temp > PriceTotal )
            {

                temp = PriceTotal ;
                Discount.setText(temp+"");
            }
            DiscountT = temp ;


            TAmount.setText((PriceTotal-temp) +"");
        }
        else
        {
            TAmount.setText((PriceTotal) +"");
        }

    }
    private void CalculatePrice(int time)
    {
        if (ISFIXED)
        {
            PriceTotal = fixedPrice;

        }else
        {

            int tempTime = time - 1;

            PriceTotal = firstHour;
            if(ISFIXEDINCREMENTAL)
            {
                PriceTotal += (tempTime) * incrementalPrice;
            }
            else
            {

int tempIndex = 0 ;
                for (int i = 0 ; i < tempTime; i++)
                {
                    if(i<hour.length) {
                        if (!hour[i].equals(""))
                            PriceTotal += Double.parseDouble(hour[i]);

                        tempIndex= i ;
                    }
                    else {
                        if (!hour[tempIndex].equals(""))
                            PriceTotal += Double.parseDouble(hour[tempIndex]);
                    }

                }


            }

            if(maxPrice>=0)
            {
                if(PriceTotal > maxPrice)
                {
                    PriceTotal = maxPrice;
                }

            }
        }
    }

    public void printcopy(View v) {
        Button payC = (Button) findViewById(R.id.copy);
        payC.setVisibility(View.INVISIBLE);
        printBill(printBill);
        Intent goTo = new Intent(this, view_home.class);
        startActivity(goTo);
    }
    public void Save(View v) throws JSONException {

        if(!session.mIsConnected)
        {
            Toast.makeText(getApplicationContext(), "printer Not Connected.", Toast.LENGTH_SHORT).show();

            addprinter();
        }
        else
            {
            EditText DiscountRE = (EditText) findViewById(R.id.DiscountR);
            String isApproved = "1";
            if (DiscountT > 0) {
                isApproved = "0";
                if (!Validation.hasText(DiscountRE)) {
                    Toast.makeText(this, "Insert Discount Reason !", Toast.LENGTH_LONG).show();

                    return;
                }
                DiscountR = DiscountRE.getText().toString();
            }

            payB.setVisibility(View.GONE);

            Button payC = (Button) findViewById(R.id.copy);
            payC.setVisibility(View.VISIBLE);
            String parameter = "";

            parameter = "?type=TransactionUpdate2&TTime=" + Time + "&Total=" + PriceTotal + "&BILLNUMBER=" + session.place + "-" + TransID
                    + "&ID=" + TransID + "&EID=" + session.userID +
                    "&Date=" + formattedDate + "&DiscountR=" + DiscountR + "&DiscountT=" + DiscountT + "&isApproved=" + isApproved;
            Database(parameter);
        }


    }


    public void Database(String Par) throws JSONException {

        String path = session.url + Par.replace(" ", "%20");
        Log.d("pp",path);
        String message = "Error";
        JSONArray jArr =  getDataArray (path) ;
            for (int i = 0; i < jArr.length(); i++) {

                JSONObject obj = jArr.getJSONObject(i);
                message = obj.getString("message");
            }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
   //     Intent   goTo = new Intent(this, print.class);
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        EditText Cash= (EditText) findViewById(R.id.Cash);
        TextView Rest = (TextView) findViewById(R.id.Rest);
        String curuntDate = df.format(c.getTime());
        printBill =
                   "  Payment Receipt\n" +
                   "  #"+session.place+"-"+TransID+"\n"+
                   "********************************\n"+
                   " Date       : "+DateT+" \n"+
                   " Start  Time: "+StartT+"\n"+
                   " Return Time: "+EndT+"\n"+
                   " Amount     : "+ (PriceTotal) +"\n"+
                   " Hour       : "+Time+"\n"+
                   " Discount   : "+DiscountT+"\n"+

                   "--------------------------------\n"+
                   " Net Amount: "+ (PriceTotal - DiscountT ) +"\n"+
                   "--------------------------------\n"+
                   " Cash : "+Cash.getText().toString()+"\n"+
                   " Back : "+Rest.getText().toString()+"\n";


        printBill(printBill);




    }
    public void printBill(String billText)
    {


        PrintText(billText)  ;

    }

}



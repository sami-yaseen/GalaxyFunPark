package com.as4.galaxyfunpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by SaMi on 1/10/2018.
 */

public class view_home extends view_main {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.view_home);
        super.onCreate(savedInstanceState);

        try {
            this.fillList();
        } catch (JSONException e) {
            e.printStackTrace();

        }


    }



    private void fillList() throws JSONException {
       session.listBasket= new ArrayList<item_object>();



        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.list);
        View cell;
        TextView text;
        mainLayout.removeAllViews();
       // webservices web= new  webservices();
        String parameter= "?type=GetStroller&PO="+ session.place;
        String path = session.url+parameter;

        Log.d("ddd",path);

       // String josn=web.downloadFile(path) ;
        //JSONObject jObj = new JSONObject(josn);
        JSONArray jArr =  getDataArray (path) ; // jObj.getJSONArray("list");


        for (int i=0; i < jArr.length(); i++)
        {
            JSONObject obj = jArr.getJSONObject(i);

            final String Id=obj.getString("STROLLER_PMK_ID");

            final String Name=obj.getString("STROLLER_NAME");

            final String BarCode =  obj.getString("STROLLER_BARCODE");

            final String Stat = obj.getString("STROLLER_STATUS");



            cell = getLayoutInflater().inflate(R.layout.strole_list_row, null);

            final Button goTo= (Button) cell.findViewById(R.id.stat);

            goTo.setEnabled(false);


            String stat=obj.getString("STROLLER_STATUS");

            if (stat.equals("IN"))

            {

                goTo.setText("Sales");

                cell.setBackgroundColor(0xFF00FF00);
                goTo.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        session.SPID=Id;
                        session.SName=Name;
                        printToken() ;
                        Intent goTo = new Intent(v.getContext(), view_sales_next.class);
                        startActivity(goTo);



                    }

                });



            }



            else {

                cell.setBackgroundColor(0xFF6E170B);
                goTo.setText("Return");

                goTo.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        session.SPID=Id;
                        session.SName=Name;
                        Intent goTo = new Intent(v.getContext(), view_return.class);
                        startActivity(goTo);


                    }

                });



            }

            TextView EName = (TextView) cell.findViewById(R.id.name);
            EName.setText(obj.getString("STROLLER_NAME"));


            TextView barcode = (TextView) cell.findViewById(R.id.barcode);
            barcode.setText(obj.getString("STROLLER_BARCODE"));


            TextView Status = (TextView) cell.findViewById(R.id.status);
            Status.setText(stat);



            GoThere(Id, BarCode, Name, Stat);










            mainLayout.addView(cell);

        }

    }


    public void GoThere(String id, String Barcode, String Name, String Status) {
        item_object obj = new item_object();
        obj.setID(id);
        obj.setBarcode(Barcode);
        obj.setStatus(Status);
        obj.setname(Name);
        session.listBasket.add(obj);

    }

    public void Scan(View v) throws JSONException {
        if(!session.mIsConnected)
        {
            Toast.makeText(getApplicationContext(), "printer Not Connected.", Toast.LENGTH_SHORT).show();

            addprinter();
        }
        else {
            //  new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            //integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setOrientationLocked(false);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        }
    }

    public void searchFun ()
    {
        String Names = "";
        EditText Name = (EditText) findViewById(R.id.Name);

        String Nam = Name.getText().toString();

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.list);
        mainLayout.removeAllViews();
        for (int i = 0; i < session.listBasket.size(); i++) {
            item_object obj = session.listBasket.get(i);

           if (obj.getBarcode().equals(Nam))
            {
               // View cell = getLayoutInflater().inflate(R.layout.strole_list_row, null);

                final String Id = obj.getID();
                final String NameT = obj.getname();
                String stat = obj.getStatus();
             //   final String BarCode =  obj.getBarcode();

               /* final String Stat = obj.getStatus();


                TextView text = (TextView) cell.findViewById(R.id.name);
                text.setText(NameT);


                TextView barcode = (TextView) cell.findViewById(R.id.barcode);
                barcode.setText(BarCode);


                TextView Status = (TextView) cell.findViewById(R.id.status);

                Status.setText(Stat);


                final Button goTo = (Button) cell.findViewById(R.id.stat);
                goTo.setEnabled(false);

                String stat = obj.getStatus();

                if (stat.equals("IN"))

                {

                    goTo.setText("Sales");

                    cell.setBackgroundColor(0xFF00FF00);
                    goTo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            session.SPID = Id;
                            session.SName = NameT;
                            printToken() ;
                            Intent goTo = new Intent(v.getContext(), view_sales_next.class);
                            startActivity(goTo);


                        }

                    });


                } else {

                    cell.setBackgroundColor(0xFF6E170B);
                    goTo.setText("Return");

                    goTo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            session.SPID = Id;
                            session.SName = NameT;
                            Intent goTo = new Intent(v.getContext(), view_return.class);
                            startActivity(goTo);


                        }

                    });


                }


                mainLayout.addView(cell);*/
                afterScan( stat , Id ,  NameT ) ;
              break;

            }


        }

    }
    public void Start(View v) {

        searchFun () ;
    }
    public void afterScan(String stat , String Id , String NameT ) {



        if (stat.equals("IN"))

        {

            session.SPID = Id;
            session.SName = NameT;
            printToken() ;
                    Intent goTo = new Intent(this, view_sales_next.class);
                    startActivity(goTo);



        } else {

            session.SPID = Id;
            session.SName = NameT;

                    Intent goTo = new Intent(this, view_return.class);
                    startActivity(goTo);

        }
    }


    public void Ref(View v) {

        try {
            this.fillList();
        } catch (JSONException e) {
            e.printStackTrace();

        }



    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                EditText ID = (EditText) findViewById(R.id.Name);

                ID.setText(result.getContents());

                    searchFun() ;

                // Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public void printToken()
        {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        String formattedDate1 = df1.format(c.getTime());
        String formattedDate2 = df2.format(c.getTime());
        session.Date=formattedDate;
        Log.d("SD", session.Date);
        java.util.Date Curentdate = null;
        try {
            Curentdate = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String temp ="" +
            //  "********************************\n" +
                " Stroller  :"+session.SName+"\n"+
                " Date      :"+formattedDate2+"\n"+
                " Start Time:"+formattedDate1+"\n" ;


            String temp2 =  "" +
                    "   Terms and Conditions  \n1.Cancellation is never allowed.\n"+
                    "2.Please present valid identification document to be kept In the counter as deposit.\n"+
                    "3.Strictly follow mall management rules and regulations.\n"+
                    "4.Please collect your time token from the agent once you rent a Stroller.\n"+
                    "5.The customer is sole responsible for any accident, damage, injuries and/or any other claims.\n"+
                    "6.Any damage to the stroller and/or any attached equipment must be paid by customer.\n"+
                    "7.Loss of the stroller is the sole responsibility of the customer.\n"+
                    "8.Strictly follow safety features printed on the strollers.\n"+
                    "9.Do not leave waste and food leftovers in strollers basket.\n"+
                    "10.Strollers must be returned before the mall closing.\n********************************\n";


            PrintText2(temp,temp2) ;
    }




}

package com.as4.galaxyfunpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by SaMi on 1/10/2018.
 */

public class view_sales_next extends view_main {

    TextView Date;
    TextView SName;
    String IDS=" ";
    Button nextB  ;
    Button exit  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.view_sales_next);
        super.onCreate(savedInstanceState);


        nextB = (Button) findViewById(R.id.button);
        exit = (Button) findViewById(R.id.button1);


    }





    public void Save(View v) throws JSONException {
        exit.setVisibility(View.INVISIBLE);
        nextB.setVisibility(View.INVISIBLE);
        final EditText ID = (EditText) findViewById(R.id.ID);
        final EditText Name = (EditText) findViewById(R.id.Name);

        final EditText Email = (EditText) findViewById(R.id.Email);
        final EditText Mobile = (EditText) findViewById(R.id.Mob);
        final EditText address = (EditText) findViewById(R.id.address);
        String RName = Name.getText().toString();
        String RID = ID.getText().toString();

        String RMobile = Mobile.getText().toString();

        String REmail = Email.getText().toString();
        String Raddress = address.getText().toString();
        if (ID.length() == 0) {
            exit.setVisibility(View.VISIBLE);
            nextB.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please insert ID Number", Toast.LENGTH_LONG).show();
       return;
        }


            String parameter = "";

            parameter = "?type=ADDC2&QID=" + RID + "&Mobile=" + RMobile + "&Email=" + REmail + "&Name=" + RName +
                    "&EID=" + session.userID + "&Point=" + session.place + "&SID=" + session.SPID +
        "&Date=" + session.Date+
                    "&ADDRESS=" + Raddress;
            Database(parameter);



        }


    public void Skip(View v) throws JSONException {
exit.setVisibility(View.INVISIBLE);
        nextB.setVisibility(View.INVISIBLE);
        String parameter = "";
            parameter = "?type=TransactionADD&EID=" + session.userID + "&Point=" + session.place + "&SID=" + session.SPID + "&Date=" + session.Date;

            Database(parameter);

        }



    public void Database(String Par) throws JSONException {

        String path = session.url + Par.replace(" ", "%20");
        Log.d("pp",path);
        String message = "Error";
       // webservices web = new webservices();
        JSONArray jArr =  getDataArray (path) ;
            for (int i = 0; i < jArr.length(); i++) {

                JSONObject obj = jArr.getJSONObject(i);
                message = obj.getString("message");
            }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent goTo = new Intent(this, view_home.class);
        startActivity(goTo);


    }
    public void Scan(View v) throws JSONException {
        nextB.setVisibility(View.INVISIBLE);
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
    public  void searchFun () throws JSONException
    {
        EditText ID = (EditText) findViewById(R.id.ID);

        IDS=ID.getText().toString();



        if (IDS.trim().equals(""))
        {
            return;
        }




       // webservices web = new webservices();
        String parameter = "?type=GetCustomerByQID&QID="+IDS;
        String path = session.url + parameter;
        JSONArray jArr =  getDataArray (path) ;
        for (int i = 0; i < jArr.length(); i++) {
            JSONObject obj = jArr.getJSONObject(i);
            final String Id = obj.getString("CUSTOMER_PMK_ID");






            if (!Id.equals("0")) {

                final EditText Name = (EditText) findViewById(R.id.Name);

                final EditText Email = (EditText) findViewById(R.id.Email);
                final EditText Mobile = (EditText) findViewById(R.id.Mob);
                final EditText Address = (EditText) findViewById(R.id.address);

                ID.setText(obj.getString("CUSTOMER_QID"));

                Email.setText(obj.getString("CUSTOMER_EMAIL"));

                Mobile.setText(obj.getString("CUSTOMER_MOBILE"));

                Name.setText(obj.getString("CUSTOMER_NAME"));
                Address.setText(obj.getString("CUSTOMER_ADDRESS"));}








        }
        nextB.setVisibility(View.VISIBLE);
    }
    public void Ser(View v) throws JSONException {

        nextB.setVisibility(View.INVISIBLE);
        searchFun() ;
    }



    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                EditText ID = (EditText) findViewById(R.id.ID);

               ID.setText(result.getContents());
                try {
                    searchFun() ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}







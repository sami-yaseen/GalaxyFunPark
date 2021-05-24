package com.as4.galaxyfunpark;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public   class report extends view_main {

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.report);
        super.onCreate(savedInstanceState);
        EditText edittext= (EditText) findViewById(R.id.editdate);

        final   DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(report.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    public void search (View view) throws JSONException {

        fillList() ;

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        EditText edittext= (EditText) findViewById(R.id.editdate);
        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    private void fillList() throws JSONException {
        session.listBasket= new ArrayList<item_object>();

        EditText edittext= (EditText) findViewById(R.id.editdate);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.list);
        View cell;
        TextView text;
        mainLayout.removeAllViews();
        // webservices web= new  webservices();
        String parameter= "?type=GetSearchData&dates="+ edittext.getText().toString()+"&PO="+ session.place;
        String path = session.url+parameter;

        Log.d("ddd",path);

        // String josn=web.downloadFile(path) ;
        //JSONObject jObj = new JSONObject(josn);
        JSONArray jArr =  getDataArray (path) ; // jObj.getJSONArray("list");


        for (int i=0; i < jArr.length(); i++)
        {
            JSONObject obj = jArr.getJSONObject(i);

            final String Id=obj.getString("TRANSACTION_PMK_ID");

            if(Integer.parseInt(Id)<=0)
                continue;

            final String Name="\n Customer :\n"+obj.getString("CUSTOMER_NAME")+"\n"+obj.getString("CUSTOMER_QID")
                    +"\n"+obj.getString("CUSTOMER_MOBILE")+"\n"+obj.getString("CUSTOMER_ADDRESS")

                    ;
            final String Strole ="\n Stroller : \n"+obj.getString("STROLLER_NAME")+" - "+obj.getString("STROLLER_DETAILS")


                    ;
            final String Bill =" Date :"+obj.getString("TRANSACTION_DATE")
                    +"\n Time :" +obj.getString("TRANSACTION_CHECK_IN")  +" - "+obj.getString("TRANSACTION_CHECK_OUT") +"  ( "+obj.getString("TRANSACTION_TIME") +" )"

                    +"\n Amount :" +obj.getString("TRANSACTION_TENDER_AMOUNT")

                    +"\n Discount :"+obj.getString("TRANSACTION_DISCOUNT")
                    +"\n Total :"+(Integer.parseInt(obj.getString("TRANSACTION_TENDER_AMOUNT"))  -   Integer.parseInt( obj.getString("TRANSACTION_DISCOUNT")))
                    ;
//

            final  String printBill =
                    "  Payment Receipt \n" +
                            "  #"+obj.getString("TRANSACTION_BILLNUMBER")+"\n"+
                            "********************************\n"+
                            " Date       : "+obj.getString("TRANSACTION_DATE")+"\n"+
                            " Start  Time: "+obj.getString("TRANSACTION_CHECK_IN")+"\n"+
                            " Return Time: "+obj.getString("TRANSACTION_CHECK_OUT")+"\n"+
                            " Amount     : "+ obj.getString("TRANSACTION_TENDER_AMOUNT") +"\n"+
                            " Hour       : "+obj.getString("TRANSACTION_TIME")+"\n"+
                            " Discount   : "+obj.getString("TRANSACTION_DISCOUNT")+"\n"+
                            "--------------------------------\n"+
                            " Net Amount: "+(Integer.parseInt(obj.getString("TRANSACTION_TENDER_AMOUNT"))  -   Integer.parseInt( obj.getString("TRANSACTION_DISCOUNT")))+"\n"+
                            "--------------------------------\n"+"Customer :"+obj.getString("CUSTOMER_NAME")
                            +"\n QID :"+obj.getString("CUSTOMER_QID")
                            +"\n Mobile :"+obj.getString("CUSTOMER_MOBILE")+"\n";



            cell = getLayoutInflater().inflate(R.layout.report_raw, null);

            final Button goTo= (Button) cell.findViewById(R.id.bttP);

                goTo.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        printBill(printBill);
                    }

                });


            TextView EName = (TextView) cell.findViewById(R.id.name);
            EName.setText(Bill+""+Strole+""+Name);

            mainLayout.addView(cell);

        }

    }


    public void printBill(String billText)
    {

        if(!session.mIsConnected)
        {
            Toast.makeText(getApplicationContext(), "printer Not Connected.", Toast.LENGTH_SHORT).show();

            addprinter();
        }
        else {
            PrintText(billText);
        }

    }

}

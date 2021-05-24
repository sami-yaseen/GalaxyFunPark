package com.as4.galaxyfunpark;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Bankout  extends view_main {

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarTo = Calendar.getInstance();
     String Ctime = "" ;
     String CtimeTo = "";
    String emtyS[]={"" ," ", "  ", "   ","    "} ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.bankout);
        super.onCreate(savedInstanceState);
        EditText edittext= (EditText) findViewById(R.id.editdate);
        EditText edittextTo= (EditText) findViewById(R.id.editdateTo);
//TimePickerDialog
        final   DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(1);
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Bankout.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final   DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarTo.set(Calendar.YEAR, year);
                myCalendarTo.set(Calendar.MONTH, monthOfYear);
                myCalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(2);
            }

        };

        edittextTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Bankout.this, dateTo, myCalendarTo
                        .get(Calendar.YEAR), myCalendarTo.get(Calendar.MONTH),
                        myCalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel(int index) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (index == 1) {
            EditText edittext= (EditText) findViewById(R.id.editdate);
            edittext.setText(sdf.format(myCalendar.getTime()) +" "+Ctime);
        }
        else
        {
            EditText edittext= (EditText) findViewById(R.id.editdateTo);
            edittext.setText(sdf.format(myCalendarTo.getTime())+" " +CtimeTo);
        }

    } public void search (View view) throws JSONException {

        fillList() ;


    }
    public void GetTime (View view)  {

        showDialog(1);

    }

    public void GetTimeTo (View view)  {


        showDialog(2);



    }
    private void fillList() throws JSONException {

        EditText edittext= (EditText) findViewById(R.id.editdate);

        EditText edittextTo= (EditText) findViewById(R.id.editdateTo);
        if (!Validation.hasText(edittext)) {
            Toast.makeText(this, "Insert Date From", Toast.LENGTH_LONG).show();

            return;
        }
        if (!Validation.hasText(edittextTo)) {
            Toast.makeText(this, "Insert Date To", Toast.LENGTH_LONG).show();

            return;
        }

        String parameter = "?type=Bankout&point=" + session.place +
                "&Date=" + edittext.getText().toString()+"&DateTo=" + edittextTo.getText().toString();
        String path = session.url + parameter.replace(" ", "%20");
        String UserID ="";
        String Row ="";
        Double total =0.0 ;
        int tranNum =0 ;
        Log.d("SamiD", path);
        JSONArray jArr =  getDataArray (path) ;
        for (int i = 0; i < jArr.length(); i++)
        {
                JSONObject obj = jArr.getJSONObject(i);

                UserID = obj.getString("TRANSACTION_PMK_ID");

                if(UserID.equals("-2"))
                {

                    Row = "You Don't Do Any Transaction \n" ;
                }

                else
                {
                    tranNum++ ;
                    total += Double.parseDouble(obj.getString("TRANSACTION_TENDER_AMOUNT")) ;

                    String B = obj.getString("TRANSACTION_BILLNUMBER");

                    // "* #SN    * #STR * Time * Total *\n";
                    int T= 7-  B.length();
                    if(T>0 && T<5)
                    {
                        B +=emtyS[T] ;
                    }
                    String S = obj.getString("STROLLER_BARCODE");
                    T=  5 - S.length();
                    if(T>0 && T<5)
                    {
                        S +=emtyS[T] ;
                    }
                    String ti = obj.getString("TRANSACTION_TIME");
                    T=   3  - ti.length();
                    if(T>0 && T<5)
                    {
                        ti +=emtyS[T] ;
                    }
                    String a = obj.getString("TRANSACTION_TENDER_AMOUNT");
                    T= 5 -  a.length();
                    if(T>0 && T<5)
                    {
                        a +=emtyS[T] ;
                    }
                    Row +=
                            "--------------------------------\n"+
                                    //    "********************************\n"+
                                    "| "+B+
                                    "| "+S+

                                    "|  "+ti+
                                    "|  "+a+
                                    "|\n"+

                                    "";//":"


                }






        }

        String Heagder =   "           BANK OUT\n" +
                " Emp  :"+ session.name+"( "+ session.userID+" ) \n"+
                " From :"+edittext.getText().toString()+"\n"+
                " To   :"+edittextTo.getText().toString()+"\n"+
                "--------------------------------\n"+
                //"--------------------------------\n"+
                "| #SN    | #STR | Time | Total |\n";

        String Footer =
                "--------------------------------\n"+
                        // "********************************\n"+
                        " #Transaction : "+tranNum+"\n"+
                        " Total        : "+total+"\n"+
                        "";
        TextView t = (TextView)findViewById(R.id.PrintText) ;
        t.setText(Heagder+Row+Footer);
    }
    public void printbankout (View view)  {

        if(!session.mIsConnected)
        {
            Toast.makeText(getApplicationContext(), "printer Not Connected.", Toast.LENGTH_SHORT).show();

            addprinter();
        }
        else {
            TextView t = (TextView)findViewById(R.id.PrintText) ;
            PrintText(t.getText().toString())  ;
        }

    }

    protected Dialog onCreateDialog(int id) {

        // Get the calander
        Calendar c = Calendar.getInstance();


        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

if(id==1)
                // Open the timepicker dialog
                return new TimePickerDialog(Bankout.this, time_listener, hour,
                        minute, true);
else

    return new TimePickerDialog(Bankout.this, time_listener2, hour,
            minute, true);


    }

    // Date picker dialog

    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String h = String.valueOf(hour) ;
            String m = String.valueOf(minute) ;

            if(hour<=9)
                h="0"+h;
            if(minute<=9)
                m="0"+m;
            String time1 = h + ":" + m;
            Ctime = time1;
            updateLabel(1) ;
        }
    };
    TimePickerDialog.OnTimeSetListener time_listener2 = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String h = String.valueOf(hour) ;
            String m = String.valueOf(minute) ;

            if(hour<=9)
h="0"+h;
            if(minute<=9)
                m="0"+m;
                String time1 = h + ":" + m;

            CtimeTo = time1;
            updateLabel(2) ;
        }
    };
}



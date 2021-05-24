package com.as4.galaxyfunpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class print extends view_main {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isPrintPagge = true ;
        setContentView(R.layout.printarea);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Button Bt = (Button)findViewById(R.id.printBT) ;
        Bt.setVisibility(View.INVISIBLE);

        Button Bt1 = (Button)findViewById(R.id.exit) ;
        Bt1.setVisibility(View.INVISIBLE);
        Bt.setVisibility(View.VISIBLE);
        Bt1.setVisibility(View.VISIBLE);
        String Heagder = intent.getStringExtra("Heagder");
        String Row = intent.getStringExtra("Row");
        String Footer = intent.getStringExtra("Footer");
        TextView t = (TextView)findViewById(R.id.PrintText) ;

        t.setText(Heagder+Row+Footer);
        printBankout();
    }


    public void Gohome(View v)
    {

        Intent goTo = new Intent(this, view_login.class);
        startActivity(goTo);
       // finish();
     //   moveTaskToBack(true);
    }
    public void printText(View v)
    {
        if(!session.mIsConnected)
        {
            Toast.makeText(getApplicationContext(), "printer Not Connected.", Toast.LENGTH_SHORT).show();

            addprinter();
        }
        else {
            printBankout();
        }
    }
    private void printBankout() {


        TextView t = (TextView)findViewById(R.id.PrintText) ;
        PrintText(t.getText().toString())  ;

    }
}

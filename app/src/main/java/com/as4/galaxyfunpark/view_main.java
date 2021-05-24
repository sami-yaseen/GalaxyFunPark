package com.as4.galaxyfunpark;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bixolon.printer.BixolonPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;


/**
 * Created by SaMi on 1/10/2018.
 */

public class view_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public  boolean isLoginPagge =false ;
    public  boolean isPrintPagge =false ;
//printer
//printer
public static final String TAG = "BixolonPrinterSample";

    static final String ACTION_GET_DEFINEED_NV_IMAGE_KEY_CODES = "com.bixolon.anction.GET_DEFINED_NV_IMAGE_KEY_CODES";
    static final String ACTION_COMPLETE_PROCESS_BITMAP = "com.bixolon.anction.COMPLETE_PROCESS_BITMAP";
    static final String ACTION_GET_MSR_TRACK_DATA = "com.bixolon.anction.GET_MSR_TRACK_DATA";
    static final String EXTRA_NAME_NV_KEY_CODES = "NvKeyCodes";
    static final String EXTRA_NAME_MSR_MODE = "MsrMode";
    static final String EXTRA_NAME_MSR_TRACK_DATA = "MsrTrackData";
    static final String EXTRA_NAME_BITMAP_WIDTH = "BitmapWidth";
    static final String EXTRA_NAME_BITMAP_HEIGHT = "BitmapHeight";
    static final String EXTRA_NAME_BITMAP_PIXELS = "BitmapPixels";

    static final int REQUEST_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE;
    static final int RESULT_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE - 1;
    static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
    static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;

    static final String FIRMWARE_FILE_NAME = "FirmwareFileName";

    // Name of the connected device
    private String mConnectedDeviceName = null;

    private ListView mListView;
    private ProgressBar mProgressBar;

    private AlertDialog mPdf417Dialog;
    private AlertDialog mQrCodeDialog;
    private AlertDialog mMaxiCodeDialog;
    private AlertDialog mDataMatrixDialog;
    private AlertDialog mCodePageDialog;
    private AlertDialog mPrinterIdDialog;
    private AlertDialog mPrintSpeedDialog;
    private AlertDialog mPrintDensityDialog;
    private AlertDialog mDirectIoDialog;
    private AlertDialog mPowerSavingModeDialog;
    private AlertDialog mBsCodePageDialog;
    private AlertDialog mPrintColorDialog;

    static BixolonPrinter mBixolonPrinter;




    //end printer
    //end printer
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB || Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB_MR1) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }

        if(!session.mIsConnected)
        mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
            if(!session.userID.equals("0"))
        {

            if(isLoginPagge)
            {
                Intent goTo= new Intent(this,view_home.class);
                startActivity(goTo);
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View header=navigationView.getHeaderView(0);
            TextView UserName = (TextView) header.findViewById(R.id.UserName);
            UserName.setText(session.name);
        }
        else  if(!isLoginPagge  )
        {

               if(!isPrintPagge)
                  {
                Intent goTo= new Intent(this,view_login.class);
                startActivity(goTo);}



        }

    }

    public JSONArray getDataArray (String path) throws JSONException
    {
        JSONArray jArr = new JSONArray() ;
        webservices web= new webservices();
        String josn=web.downloadFile(path) ;

        if(josn.equals("-1"))
        {
            alertDialog("Error" , "No Internet connection!") ;
        }
        else
        {
            try
            {
                JSONObject jObj = new JSONObject(josn);
                jArr = jObj.getJSONArray("list");
            }
            catch ( JSONException JE)
            {
                alertDialog("Error" , "No Data Return!") ;
            }
        }

        return  jArr;
    }


public void alertDialog (String Title , String message   )
{
    // 1. Instantiate an AlertDialog.Builder with its constructor
    AlertDialog.Builder builder = new AlertDialog.Builder(view_main.this);

// 2. Chain together various setter methods to set the dialog characteristics
    builder.setMessage(message)
            .setTitle(Title);
    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // User clicked OK button
            dialog.dismiss();
        }
    });
// 3. Get the AlertDialog from create()
    AlertDialog dialog = builder.create();
    dialog.show();

}
    @Override
    public void onBackPressed() {
        if(!session.userID.equals("0")) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                //  if(session.userID.equals("0"))
                //    super.onBackPressed();
            }
        }
    }

    public void openScan (View v )
    {

    }
    public void PrintText (String Text )
    {

        Text = Text.replaceAll("null" , "") ;
        int alignment = BixolonPrinter.ALIGNMENT_LEFT;
        int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL3;
        int attribute = 0;



        if(session.mIsConnected)
        {
            view_main.mBixolonPrinter.lineFeed(2, false);
            String Temp = session.PHeader + Text+session.PFooter ;
            view_main.mBixolonPrinter.printDotMatrixText(Temp, alignment, attribute, size, false);

            view_main.mBixolonPrinter.lineFeed(2, false);

          //  view_main.mBixolonPrinter.cutPaper(true);
          //  view_main.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);

        }
    }

    public void PrintText2 (String Text ,String temp1 )
    {
        int alignment = BixolonPrinter.ALIGNMENT_LEFT;
        int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL3;
        int attribute = 0;
        Text =  Text.replaceAll("null" , "") ;


        if(session.mIsConnected)
        {
            view_main.mBixolonPrinter.lineFeed(2, false);
            String Temp = session.PHeader + Text+session.PFooter ;
            view_main.mBixolonPrinter.printDotMatrixText(Temp, alignment, attribute, size, false);
            view_main.mBixolonPrinter.printDotMatrixText(temp1, alignment, attribute, size, false);
            view_main.mBixolonPrinter.lineFeed(2, false);



        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(mUsbReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        mBixolonPrinter.disconnect();
    }

    public void addprinter()
    {
        mBixolonPrinter.findBluetoothPrinters();
        mBixolonPrinter.getStatus();
    }
    public void print(View v)
    {
        int alignment = BixolonPrinter.ALIGNMENT_LEFT;
        String text ="";
        String PHeader  = "********************************\n" +
                "*        Galaxy Fun Park       *\n" +
                "********************************\n" ;;
        String PFooter  =
                "\n********************************\n" +

                        " Bil# : 1512 \n" +
                        " Date  : 08-08-2018 \n" +
                        "********************************\n" +

                        "\n********************************\n" +
                        "Thank You For Using Our Services\n" +

                        "********************************\n" +
                        "        Tel : +974 44087338  \n" +
                        "        Fax : +974 44087334  \n" +
                        "***********************************************\n" ;;

        text = PHeader+PFooter ;
        int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL3;
        int attribute = 0;

        view_main.mBixolonPrinter.printDotMatrixText(text, alignment, attribute, size, false);

        view_main.mBixolonPrinter.lineFeed(3, false);

        view_main.mBixolonPrinter.cutPaper(true);
        view_main.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);
    }
    public void disconnect()
    {
        mBixolonPrinter.disconnect();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            Menu menu1 = navigationView.getMenu();


            if (session.mIsConnected) {
                menu1.findItem(R.id.nav_print).setTitle("Printer Connectend");

            } else {
                menu1.findItem(R.id.nav_print).setTitle("Printer Not Connected");
            }
            navigationView.setNavigationItemSelectedListener(this);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent goTo;
        if (id == R.id.nav_main) {
            goTo= new Intent(this,view_home.class);
            startActivity(goTo);
        }else if (id == R.id.nav_report) {
            goTo= new Intent(this,report.class);
            startActivity(goTo);
        } else if (id == R.id.nav_bankout) {
            goTo= new Intent(this,Bankout.class);
            startActivity(goTo);
        }  else if (id == R.id.nav_mainnfc) {
             goTo= new Intent(this,view_homenfc.class);
            startActivity(goTo);
        } else if (id == R.id.nav_print) {
            if (session.mIsConnected) {
                disconnect() ;
            }
            else
            {
                addprinter();
            }


        }
        else if (id == R.id.nav_logout) {

              goTo = new Intent(this, view_logout.class);
              startActivity(goTo);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private final void setStatus(int resId) {
     //   final ActionBar actionBar = getActionBar();
      //  actionBar.setSubtitle(resId);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            Menu menu1 = navigationView.getMenu();


            if (session.mIsConnected) {
                menu1.findItem(R.id.nav_print).setTitle("Connecting");

            } else {
                menu1.findItem(R.id.nav_print).setTitle("Printer Not Connected");
            }
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private final void setStatus(CharSequence subtitle) {
     //   final ActionBar actionBar = getActionBar();
    //    actionBar.setSubtitle(subtitle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            Menu menu1 = navigationView.getMenu();


            if (session.mIsConnected) {
                menu1.findItem(R.id.nav_print).setTitle("Connecting");

            } else {
                menu1.findItem(R.id.nav_print).setTitle("Printer Not Connected");
            }
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void dispatchMessage(Message msg) {
        switch (msg.arg1) {
            case BixolonPrinter.PROCESS_GET_STATUS:
                if (msg.arg2 == BixolonPrinter.STATUS_NORMAL) {
                    Toast.makeText(getApplicationContext(), "No error", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuffer buffer = new StringBuffer();
                    if ((msg.arg2 & BixolonPrinter.STATUS_COVER_OPEN) == BixolonPrinter.STATUS_COVER_OPEN) {
                        buffer.append("Cover is open.\n");
                    }
                    if ((msg.arg2 & BixolonPrinter.STATUS_PAPER_NOT_PRESENT) == BixolonPrinter.STATUS_PAPER_NOT_PRESENT) {
                        buffer.append("Paper end sensor: paper not present.\n");
                    }

                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                }
                break;

            case BixolonPrinter.PROCESS_GET_BATTERY_STATUS:
                StringBuffer buffer = new StringBuffer();
                switch (msg.arg2) {
                    case BixolonPrinter.STATUS_BATTERY_FULL:
                        buffer.append("STATUS_BATTERY_FULL\n");
                        Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case BixolonPrinter.STATUS_BATTERY_HIGH:
                        buffer.append("STATUS_BATTERY_HIGH\n");
                        Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.STATUS_BATTERY_MIDDLE:
                        buffer.append("STATUS_BATTERY_MIDDLE\n");
                        Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case BixolonPrinter.STATUS_BATTERY_LOW:
                        buffer.append("STATUS_BATTERY_LOW\n");
                        Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case BixolonPrinter.PROCESS_GET_PRINTER_ID:
                Bundle data = msg.getData();
                Toast.makeText(getApplicationContext(), data.getString(BixolonPrinter.KEY_STRING_PRINTER_ID), Toast.LENGTH_SHORT).show();
                break;

            case BixolonPrinter.PROCESS_GET_BS_CODE_PAGE:
                data = msg.getData();
                Toast.makeText(getApplicationContext(), data.getString(BixolonPrinter.KEY_STRING_CODE_PAGE), Toast.LENGTH_SHORT).show();
                break;

            case BixolonPrinter.PROCESS_GET_PRINT_SPEED:
                switch (msg.arg2) {
                    case BixolonPrinter.PRINT_SPEED_LOW:
                        Toast.makeText(getApplicationContext(), "Print speed: low", Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.PRINT_SPEED_MEDIUM:
                        Toast.makeText(getApplicationContext(), "Print speed: medium", Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.PRINT_SPEED_HIGH:
                        Toast.makeText(getApplicationContext(), "Print speed: high", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case BixolonPrinter.PROCESS_GET_PRINT_DENSITY:
                switch (msg.arg2) {
                    case BixolonPrinter.PRINT_DENSITY_LIGHT:
                        Toast.makeText(getApplicationContext(), "Print density: light", Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.PRINT_DENSITY_DEFAULT:
                        Toast.makeText(getApplicationContext(), "Print density: default", Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.PRINT_DENSITY_DARK:
                        Toast.makeText(getApplicationContext(), "Print density: dark", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case BixolonPrinter.PROCESS_GET_POWER_SAVING_MODE:
                String text = "Power saving mode: ";
                if (msg.arg2 == 0) {
                    text += false;
                } else {
                    text += true + "\n(Power saving time: " + msg.arg2 + ")";
                }
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                break;

            case BixolonPrinter.PROCESS_AUTO_STATUS_BACK:
                buffer = new StringBuffer(0);
                if ((msg.arg2 & BixolonPrinter.AUTO_STATUS_COVER_OPEN) == BixolonPrinter.AUTO_STATUS_COVER_OPEN) {
                    buffer.append("Cover is open.\n");
                }
                if ((msg.arg2 & BixolonPrinter.AUTO_STATUS_NO_PAPER) == BixolonPrinter.AUTO_STATUS_NO_PAPER) {
                    buffer.append("Paper end sensor: no paper present.\n");
                }

                if (buffer.capacity() > 0) {
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No error.", Toast.LENGTH_SHORT).show();
                }
                break;

            case BixolonPrinter.PROCESS_GET_NV_IMAGE_KEY_CODES:
                data = msg.getData();
                int[] value = data.getIntArray(BixolonPrinter.NV_IMAGE_KEY_CODES);

                Intent intent = new Intent();
                intent.setAction(ACTION_GET_DEFINEED_NV_IMAGE_KEY_CODES);
                intent.putExtra(EXTRA_NAME_NV_KEY_CODES, value);
                sendBroadcast(intent);
                break;

            case BixolonPrinter.PROCESS_EXECUTE_DIRECT_IO:
                buffer = new StringBuffer();
                data = msg.getData();
                byte[] response = data.getByteArray(BixolonPrinter.KEY_STRING_DIRECT_IO);
                for (int i = 0; i < response.length && response[i] != 0; i++) {
                    buffer.append(Integer.toHexString(response[i]) + " ");
                }

                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                break;

            case BixolonPrinter.PROCESS_MSR_TRACK:
                intent = new Intent();
                intent.setAction(ACTION_GET_MSR_TRACK_DATA);
                intent.putExtra(EXTRA_NAME_MSR_TRACK_DATA, msg.getData());
                sendBroadcast(intent);
                break;

            case BixolonPrinter.PROCESS_GET_MSR_MODE:
                intent = new Intent(view_main.this, MsrActivity.class);
                intent.putExtra(EXTRA_NAME_MSR_MODE, msg.arg2);
                startActivity(intent);
                break;
        }
    }
    private final Handler mHandler = new Handler(new Handler.Callback() {

        @SuppressWarnings("unchecked")
        @Override
        public boolean handleMessage(Message msg) {
            Log.d(TAG, "mHandler.handleMessage(" + msg + ")");

            switch (msg.what) {
                case BixolonPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BixolonPrinter.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mListView.setEnabled(true);
                            session.mIsConnected = true;
                            Log.d("ssss","conect");
                            invalidateOptionsMenu();
                            break;


                        case BixolonPrinter.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);

                            Log.d("ssss","title_connecting");
                            break;

                        case BixolonPrinter.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            Log.d("ssss","title_not_connected");
                          //  mListView.setEnabled(false);
                            session.mIsConnected = false;
                            invalidateOptionsMenu();
                          //  mProgressBar.setVisibility(View.INVISIBLE);
                            break;
                    }
                    return true;

                case BixolonPrinter.MESSAGE_WRITE:
                    switch (msg.arg1) {
                        case BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT:
                            mHandler.obtainMessage(MESSAGE_END_WORK).sendToTarget();

                            Toast.makeText(getApplicationContext(), "Complete to set double byte font.", Toast.LENGTH_SHORT).show();
                            break;

                        case BixolonPrinter.PROCESS_DEFINE_NV_IMAGE:
                            mBixolonPrinter.getDefinedNvImageKeyCodes();
                            Toast.makeText(getApplicationContext(), "Complete to define NV image", Toast.LENGTH_LONG).show();
                            break;

                        case BixolonPrinter.PROCESS_REMOVE_NV_IMAGE:
                            mBixolonPrinter.getDefinedNvImageKeyCodes();
                            Toast.makeText(getApplicationContext(), "Complete to remove NV image", Toast.LENGTH_LONG).show();
                            break;

                        case BixolonPrinter.PROCESS_UPDATE_FIRMWARE:
                            mBixolonPrinter.disconnect();
                            Toast.makeText(getApplicationContext(), "Complete to download firmware.\nPlease reboot the printer.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;

                case BixolonPrinter.MESSAGE_READ:
                    view_main.this.dispatchMessage(msg);
                    return true;

                case BixolonPrinter.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), mConnectedDeviceName, Toast.LENGTH_LONG).show();
                    return true;

                case BixolonPrinter.MESSAGE_TOAST:
                   // mListView.setEnabled(false);
                    session.mIsConnected = false;
                    Toast.makeText(getApplicationContext(), msg.getData().getString(BixolonPrinter.KEY_STRING_TOAST), Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No paired device", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogManager.showBluetoothDialog(view_main.this, (Set<BluetoothDevice>) msg.obj);
                    }
                    return true;

                case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
                    Toast.makeText(getApplicationContext(), "Complete to print", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_INVALID_ARGUMENT:
                    Toast.makeText(getApplicationContext(), "Invalid argument", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_NV_MEMORY_CAPACITY:
                    Toast.makeText(getApplicationContext(), "NV memory capacity error", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_OUT_OF_MEMORY:
                    Toast.makeText(getApplicationContext(), "Out of memory", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_COMPLETE_PROCESS_BITMAP:
                    String text = "Complete to process bitmap.";
                    Bundle data = msg.getData();
                    byte[] value = data.getByteArray(BixolonPrinter.KEY_STRING_MONO_PIXELS);
                    if (value != null) {
                        Intent intent = new Intent();
                        intent.setAction(ACTION_COMPLETE_PROCESS_BITMAP);
                        intent.putExtra(EXTRA_NAME_BITMAP_WIDTH, msg.arg1);
                        intent.putExtra(EXTRA_NAME_BITMAP_HEIGHT, msg.arg2);
                        intent.putExtra(EXTRA_NAME_BITMAP_PIXELS, value);
                        sendBroadcast(intent);
                    }

                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    return true;

                case MESSAGE_START_WORK:
                    //mListView.setEnabled(false);
                    session.mIsConnected = false;
                 //   mProgressBar.setVisibility(View.VISIBLE);
                    return true;

                case MESSAGE_END_WORK:
                //    mListView.setEnabled(true);
                    session.mIsConnected = true;
                //    mProgressBar.setVisibility(View.INVISIBLE);
                    return true;

                case BixolonPrinter.MESSAGE_USB_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogManager.showUsbDialog(view_main.this, (Set<UsbDevice>) msg.obj, mUsbReceiver);
                    }
                    return true;

                case BixolonPrinter.MESSAGE_USB_SERIAL_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
                    } else {
                        final HashMap<String, UsbDevice> usbDeviceMap = (HashMap<String, UsbDevice>) msg.obj;
                        final String[] items = usbDeviceMap.keySet().toArray(new String[usbDeviceMap.size()]);
                        new AlertDialog.Builder(view_main.this).setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBixolonPrinter.connect(usbDeviceMap.get(items[which]));
                            }
                        }).show();
                    }
                    return true;

                case BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No connectable device", Toast.LENGTH_SHORT).show();
                    }
                    DialogManager.showNetworkDialog(view_main.this, (Set<String>) msg.obj);
                    return true;
            }
            return false;
        }
    });

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "mUsbReceiver.onReceive(" + context + ", " + intent + ")");
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                mBixolonPrinter.connect();
                Toast.makeText(getApplicationContext(), "Found USB device", Toast.LENGTH_SHORT).show();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                mBixolonPrinter.disconnect();
                Toast.makeText(getApplicationContext(), "USB device removed", Toast.LENGTH_SHORT).show();
            }

        }
    };

}
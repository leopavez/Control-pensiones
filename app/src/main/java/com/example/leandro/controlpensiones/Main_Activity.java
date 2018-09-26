package com.example.leandro.controlpensiones;

//SCRIPT DE LA IMPRESORA TERMICA
//SCRIPT DE LA IMPRESORA TERMICA

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


public class Main_Activity extends Activity implements Runnable {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;



    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.testimpresora);
        mScan = (Button) findViewById(R.id.button_scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(Main_Activity.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(Main_Activity.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        mPrint = (Button) findViewById(R.id.Send_Button);
        mPrint.setOnClickListener(new View.OnClickListener() {

            public void onClick(View mView) {

                        try {
                            OutputStream os = mBluetoothSocket
                                    .getOutputStream();
                            Handler mHandler= new Handler(Looper.getMainLooper());



                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            SimpleDateFormat horaformat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                            Date date = new Date();
                            String  fecha = dateFormat.format(date);
                            String hora = horaformat.format(date);
                            String msg2 = " "+" "+" Ingenieria y Construcciones "+" "+"\n"+
                                    " " + " "+" "+""+" "+" " +" "+" Santa Fe S.A"+" "+"\n"+
                                    " " +"\n"+
                                    " " + "Fecha: "+fecha+"\n"+
                                    " " + "Hora: "+hora+"\n"+
                                    " " +"\n"+
                                    " " + " "+" "+" "+" "+" "+"AUTORIZADO"+" "+"\n"+
                                    " " +"\n"+
                                    " " +"\n"+
                                    " " +"\n";

                            os.write(msg2.getBytes());
                            Automatizacion();

                            //This is printer specific code you can comment ==== > Start




                        } catch (Exception e) {
                            Log.e("MainActivity", "Exe ", e);
                        }
                    }

        });

    }// onCreate

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    public void imprimir() throws Exception{

        try {

            String address="66:12:43:1C:E8:61";


            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if  ((mBluetoothAdapter == null) || (!mBluetoothAdapter.isEnabled())){
                throw new Exception("Bluetooth adapter no esta funcionando o no esta habilitado");
            }

            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();


            OutputStream os = mBluetoothSocket.getOutputStream();
            InputStream is = mBluetoothSocket.getInputStream();

            Handler mHandler= new Handler(Looper.getMainLooper());



            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat horaformat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            String  fecha = dateFormat.format(date);
            String hora = horaformat.format(date);
            String msg2 = " "+" "+" Ingenieria y Construcciones "+" "+"\n"+
                    " " + " "+" "+""+" "+" " +" "+" "+" "+" Santa Fe S.A"+" "+"\n"+
                    " " +"\n"+
                    " " + "Fecha: "+fecha+"\n"+
                    " " + "Hora: "+hora+"\n"+
                    " " +"\n"+
                    " " + " "+" "+" "+" "+" "+" "+" "+" "+ "AUTORIZADO"+" "+"\n"+
                    " " +"\n"+
                    " " +"\n"+
                    " " +"\n";
            os.write(msg2.getBytes());
            mBluetoothSocket.close();




        } catch (Exception e) {
            Log.e("MainActivity", "Exe ", e);
        }
    }

    public void Automatizacion(){
        try{
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();

            mBluetoothAdapter.cancelDiscovery();
            String address="66:12:43:1C:E8:61";
            mBluetoothDevice = mBluetoothAdapter
                    .getRemoteDevice(address);
            Thread mBlutoothConnectThread = new Thread(this);
            mBlutoothConnectThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Conectando...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(Main_Activity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(Main_Activity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(Main_Activity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

}

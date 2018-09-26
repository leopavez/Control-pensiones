package com.example.leandro.controlpensiones;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue RequestQueue;
    private static Context mCtx;

    private MySingleton(Context context){
        mCtx = context;
        RequestQueue = getRequestQueue();

    }

    public static synchronized MySingleton getInstance(Context context){
        if (mInstance==null){
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }


    public RequestQueue getRequestQueue(){
        if(RequestQueue ==null){
            RequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return RequestQueue;
    }

    public <T>void addTorequestque(Request<T> request){

        RequestQueue.add(request);
    }

}

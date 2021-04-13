package com.example.tracker;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

class StringRequest extends Request<Object> {


    public StringRequest(int post, String url, Response.Listener<String> stringListener, Response.ErrorListener listener) {
        super(url, listener);
    }

    public StringRequest(int method, String url, @Nullable Response.ErrorListener listener) {
        super(method, url, listener);
    }

    public void setRetryPolicy(DefaultRetryPolicy defaultRetryPolicy) {

    }

    @Override
    protected Response<Object> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(Object response) {

    }
}

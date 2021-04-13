package com.example.tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText username, email;
    private Button save, viewData;
    private String sendUrl="https://cyclingxx.000webhostapp.com/test/getData.php";
    private RequestQue requestQue;
    private static final String TAG=MainActivity.class.getSimpleName();

    int success;
    private String TAG_SUCCESS="success";
    private String TAG_MESSAGE="message";
    private String TAG_json_obj="json_obj_req";
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.txtusername);
        email = findViewById(R.id.txtemail);
        save = findViewById(R.id.btnsave);
        viewData = findViewById(R.id.btnviewdata);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

    }

    private Map<String, String> sendData(){
        StringRequest request =new StringRequest(Request.Method.POST,sendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    JSONObject jobj= new JSONObject(response);
                    success=jobj.getInt(TAG_SUCCESS);
                    if(success==1){
                        Toast.makeText(MainActivity.this, jobj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, jobj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "An Error Occured" + e, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){

        };{
            public Map <String,String> getParams(){
                Map <String,String> params=new HashMap<String, String>();
                params.put("username",username.getText().toString());
                params.put("email",email.getText().toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1, 1.0f));
        requestQueue.add(request);
    }

}

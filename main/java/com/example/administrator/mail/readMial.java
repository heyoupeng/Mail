package com.example.administrator.mail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class readMial extends AppCompatActivity {
    private TextView mailContent;
    private String m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mial);
       mailContent=(TextView)findViewById(R.id.mail_content);
        Intent myintent=this.getIntent();
        m=myintent.getStringExtra("mail");
        mailContent.setText(m);
        Log.i("logcat",m);
    }
}

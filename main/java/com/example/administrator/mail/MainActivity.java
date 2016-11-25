package com.example.administrator.mail;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private String usernamestr;
    private String passwordstr;
    private EditText editText_username;
    private EditText editText_password;
    private Button button_login;
    private Socket socket;
    private OutputStream outputStream;
    private BufferedReader reader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_username = (EditText) findViewById(R.id.username);
        editText_password = (EditText) findViewById(R.id.password);
        button_login = (Button) findViewById(R.id.login);
        class mailThread implements Runnable{
            String username_Thread;
            String password_Thread;
            public mailThread(String username,String password){
                username_Thread=Base64String(username).trim();
                password_Thread=Base64String(password).trim();
            }
            public void run() {
                socket=new Socket();
                Toast toast;
                Looper.prepare();
                try {
                    Log.i("logcat",username_Thread);
                    Log.i("logcat",password_Thread);
                    socket.connect(new InetSocketAddress("smtp.163.com",25),3000);
                    outputStream=socket.getOutputStream();
                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write("helo 163.com\r\n".getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    if(!line.contains("250")){
                        toast=Toast.makeText(getApplicationContext(),"连接失败！请重新登录",Toast.LENGTH_SHORT);
                    }
                    outputStream.write("auth login\r\n".getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write((username_Thread+"\r\n").getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write((password_Thread+"\r\n").getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    if(!line.contains("successful")){
                        toast=Toast.makeText(getApplicationContext(),"登录失败！请重新登录",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        outputStream.close();
                        reader.close();
                        socket.close();
                        Intent intent=new Intent(MainActivity.this,Menu.class);
                        intent.putExtra("username",editText_username.getText().toString());
                        intent.putExtra("password",editText_password.getText().toString());
                        startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }

        }
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailThread m=new mailThread(editText_username.getText().toString(),editText_password.getText().toString());
                Thread t=new Thread(m);
                t.start();
            }
        });
    }
    public String Base64String(String s){
        s=Base64.encodeToString(s.getBytes(),Base64.DEFAULT);
        return s;
    }
}

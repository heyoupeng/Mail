package com.example.administrator.mail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.administrator.mail.R.layout.rightmenu;
import static com.example.administrator.mail.R.layout.support_simple_spinner_dropdown_item;

public class Menu extends AppCompatActivity {
    private Socket socket;
    private OutputStream outputStream;
    private BufferedReader reader;
    private  Button button;
    private  String username,password;
    private ListView listView;
   String[] mailnumber;
    String mail;
    int number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        button=(Button)findViewById(R.id.writemail);
        listView=(ListView)findViewById(R.id.maillist);
        Intent intent=this.getIntent();
        username=intent.getStringExtra("username");
        password=intent.getStringExtra("password");
        Log.i("logcat",username);
        Log.i("logcat",password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(Menu.this,write_mail.class);
                intent3.putExtra("username",username);
                intent3.putExtra("password",password);
                startActivity(intent3);
            }
        });
        class mailrec implements Runnable{
            @Override
            public void run() {
                socket=new Socket();
                try {
                    socket.connect(new InetSocketAddress("pop.163.com",110),3000);
                    outputStream=socket.getOutputStream();
                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write(("user "+username+"\r\n").getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write(("pass "+password+"\r\n").getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write("stat\r\n".getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    String temp[] = line.split(" ");
                    number = Integer.parseInt(temp[1]);
                    mailnumber=new String[number];
                    for (int j=0;j<number;j++){
                        mailnumber[j]="第"+(j+1)+"封邮件";
                        Log.i("loacat", mailnumber[j]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        class mailrec2 implements Runnable{
            int index;
            int mail_count;
            public mailrec2(int i,int num){
                this.index=i;
                this.mail_count=num;
            }
            public void run() {
                socket=new Socket();
                try {
                    socket.connect(new InetSocketAddress("pop.163.com",110),3000);
                    outputStream=socket.getOutputStream();
                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write(("user "+username+"\r\n").getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write(("pass "+password+"\r\n").getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    outputStream.write("stat\r\n".getBytes("utf-8"));
                    line=reader.readLine();
                    Log.i("logcat",line);
                    mail="";
                    outputStream.write(("retr "+(mail_count-index)+"\r\n").getBytes("utf-8"));
                    Log.i("logcat","这是第"+(index+1)+"封邮件");
                    while(true){
                        String reply=reader.readLine();
                        if(reply.toLowerCase().equals(".")){
                            break;
                        }
                        mail=mail+reply;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        mailrec mt=new mailrec();
        Thread thread=new Thread(mt);
        thread.start();
        while (thread.isAlive());
        {
            try {
                outputStream.close();
                reader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, mailnumber);
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mailrec2 mailrec2=new mailrec2(i,number);
                Thread thread1=new Thread(mailrec2);
                thread1.start();
                while ((thread1.isAlive()));
                {
                    Intent mail_intent=new Intent(Menu.this,readMial.class);
                    mail_intent.putExtra("mail",mail);
                    startActivity(mail_intent);
                }
            }
        });

    }
}

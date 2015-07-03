package com.example.huxianming.smsdemo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class SendMessageActivity extends ActionBarActivity {
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        final EditText to = (EditText)findViewById(R.id.to);
        final EditText content = (EditText)findViewById(R.id.content);
        Button send = (Button)findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = to.getText().toString();
                String msg = content.getText().toString();
                sendMessage(address,msg);
            }
        });
    }

    private void sendMessage(String num, String msg){
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this,0,sentIntent,0);

        Intent deliveredIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this,0,deliveredIntent,0);

        IntentFilter sentIntentFilter = new IntentFilter(SENT_SMS_ACTION);
        IntentFilter deliveredIntentFilter = new IntentFilter(DELIVERED_SMS_ACTION);

        SmsManager smsManager = SmsManager.getDefault();
        if(msg.length()>70){
            List<String> messages = smsManager.divideMessage(msg);
            for(String message:messages){
                smsManager.sendTextMessage(num,null,message,sentPendingIntent,deliveredPendingIntent);
            }
        }else{
            smsManager.sendTextMessage(num,null,msg,sentPendingIntent,deliveredPendingIntent);
        }

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(context,"Send Success",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context,"Send Fail",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        },sentIntentFilter);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context,"delivered action",Toast.LENGTH_LONG);
            }
        },deliveredIntentFilter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_message, menu);
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
}

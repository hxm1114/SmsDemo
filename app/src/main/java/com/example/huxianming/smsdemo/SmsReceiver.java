package com.example.huxianming.smsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object messages[] = (Object[]) bundle.get("pdus");
        SmsMessage smsMessage[] = new SmsMessage[messages.length];
        for (int n = 0; n < messages.length; n++) {
            smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
        }
        String from = smsMessage[0].getDisplayOriginatingAddress();
        String content = smsMessage[0].getMessageBody();

        Intent intent1 = new Intent(context,ReceiveMessageActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("from",from);
        intent1.putExtra("content",content);
        context.startActivity(intent1);
//        //产生一个Toast
        //Toast toast = Toast.makeText(context,"短信内容: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
//        //设置toast显示的位置
//        //toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 200);
//        //显示该Toast
       //toast.show();
        //abortBroadcast();
    }

}

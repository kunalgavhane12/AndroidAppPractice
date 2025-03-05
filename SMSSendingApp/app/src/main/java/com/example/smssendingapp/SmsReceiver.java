package com.example.smssendingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] smsObj = (Object[]) bundle.get("pdus");

        for (Object obj: smsObj) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);

            String mobNo = smsMessage.getDisplayOriginatingAddress();
            String msg = smsMessage.getDisplayMessageBody();

            Log.d("MsgDetails", "Mob No: " + mobNo + "Msg: " + msg);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+919922257744", null, "Hello Dear", null, null);
        }
    }
}

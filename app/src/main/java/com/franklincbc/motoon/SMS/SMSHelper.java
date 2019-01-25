package com.franklincbc.motoon.SMS;

import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by Priscila on 13/01/2017.
 */

public class SMSHelper {

    public static void enviarSMS(String numero, String msg){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(numero, null, msg, null, null);
    }

    public static SmsMessage[] getMessagesFromIntent(Intent it){
        Object[] pdusExtras = (Object[])it.getSerializableExtra("pdus");
        SmsMessage[] messages = new SmsMessage[pdusExtras.length];
        for (int i = 0; i < pdusExtras.length ; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[])pdusExtras[i]);
            
        }
        return messages;
    }

}

package com.franklincbc.motoon.SMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Priscila on 13/01/2017.
 */

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage message = SMSHelper.getMessagesFromIntent(intent)[0];
        Toast.makeText(context, "Mensagem Recebida de " + message.getOriginatingAddress() + " - " +
                message.getMessageBody(), Toast.LENGTH_LONG).show();
    }



}

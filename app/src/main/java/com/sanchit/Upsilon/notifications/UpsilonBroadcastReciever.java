package com.sanchit.Upsilon.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpsilonBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(UpsilonBroadcastReciever.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, UpsilonJobService.class));
        context.startService(new Intent(context, NotifService.class));
    }
}
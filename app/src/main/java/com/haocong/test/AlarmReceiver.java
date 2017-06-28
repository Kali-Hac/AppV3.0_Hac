package com.haocong.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        String info=arg1.getStringExtra("tx_info");
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

}

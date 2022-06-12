package com.kmab.chickeninn;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class XPlaySound {

    private Context context;

    XPlaySound(Context context) {
        this.context = context;
    }

    public void playAddSound() {
        /*final MediaPlayer mp = MediaPlayer.create(context, R.raw.add2);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });*/

        try {
            //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.add));
            //Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playRemoveSound() {
        /*final MediaPlayer mp = MediaPlayer.create(context, R.raw.add2);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });*/

        try {
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.remove);
            Ringtone r = RingtoneManager.getRingtone(context, uri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

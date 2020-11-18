package com.ggongchi.million;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MTimerService extends Service {
    public CountDownTimer CDT=null;
    int max_st;
    int st;
    int tmp;
    int count;
    int running; //0->밀리시타 실행 ㄴ 1->밀리시타 실행 ok
    private final static String TAG="TimerService";
    public static final String COUNTDOWN_BR="com.example.million.countdown_br";
    Intent bi=new Intent(COUNTDOWN_BR);
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    SharedPreferences sharedPreferences;
    NotificationCompat.Builder builder;
    int app_type;
    CountDownTimer getCDT()
    {
        return CDT;
    }
    @Override
    public void onCreate(){
        super.onCreate();
         powerManager=(PowerManager)getSystemService(POWER_SERVICE);
         wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"Million::WALELOCK");
        wakeLock.acquire();
        Log.i(TAG,"타이머 시작");
        sharedPreferences=getSharedPreferences("sFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        st=sharedPreferences.getInt("ST",-1);
        max_st=sharedPreferences.getInt("MAX",0);
        running=sharedPreferences.getInt("RB",0);
        count=0;
        tmp=0;
        app_type=sharedPreferences.getInt("APPT",0);

         CDT=new CountDownTimer((max_st-st+1)*5*60000,60000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(tmp==0) {

                    tmp++;
                }else {



                    if (count != 0 && count % 4 == 0) {
                        count = 0;
                        st++;

                    } else
                        count++;
                }
                bi.putExtra("countdown",millisUntilFinished);
                sendBroadcast(bi);
                if(builder!=null)
                {
                    startForegroundService();

                }


            }

            @Override
            public void onFinish() {
                startForegroundService();
            }
        };
        CDT.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){

        Log.i("tag","onDestroy-service");
       try {
           CDT.cancel();
       }catch (Exception e){}
       CDT=null;
        wakeLock.release();
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent,int flags, int startid)
    {
        startForegroundService();
        return super.onStartCommand(intent,flags,startid);

    }

    private void startForegroundService(){
        builder=new NotificationCompat.Builder(this,"default");
        builder.setSmallIcon(R.mipmap.msn);
        builder.setContentTitle("밀리시타 스테미나 측정");
        builder.setAutoCancel(true);
        builder.setVibrate(new long[]{0});
      // builder.mActions.clear();


        if(max_st==st)
            builder.setContentText("충전이 완료되었습니다.");

        else {
            int tmp_time = (max_st - st) * 5 - count + 1;
            if (tmp_time > 60)
                builder.setContentText("(" + st + "/" + max_st + ") 약 " + tmp_time / 60 + "시간 " + (tmp_time % 60-1) + "분 후 MAX");
            else
                builder.setContentText("(" + st + "/" + max_st + ") 약  " + ((tmp_time % 60)-1) + "분 후 MAX");
        }
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("ST",st);
        editor.commit();

        String appname="";
        if(app_type==0)
            appname="com.bandainamcoent.imas_millionlive_theaterdays";
        else
            appname="com.bandainamcoent.imas_millionlive_theaterdays_kr";

        Intent notifiIntent;
        if(running==0)
        {
            notifiIntent = getPackageManager().getLaunchIntentForPackage("com.ggongchi.million");
            notifiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        }
        else {

            notifiIntent = getPackageManager().getLaunchIntentForPackage(appname);
           // notifiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);




        }

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notifiIntent,0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
// manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel=new NotificationChannel("default","밀리_애니",NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription("밀리 애니 입니다.");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.enableVibration(true);

            manager.createNotificationChannel(notificationChannel);

        }

        startForeground(39,builder.build());


    }

}

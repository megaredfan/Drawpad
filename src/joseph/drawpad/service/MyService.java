package joseph.drawpad.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by 熊纪元 on 2015/12/28.
 */
public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        System.out.println("onStartCommand");
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }
}

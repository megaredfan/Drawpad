package joseph.drawpad.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import joseph.drawpad.rpc.IPerson;
import joseph.drawpad.rpc.IPersonImpl;

/**
 * Created by 熊纪元 on 2015/12/28.
 */
public class MyRemoteService extends Service {
    private IPerson.Stub iPerson = new IPersonImpl();

    @Override
    public IBinder onBind(Intent intent) {
        return iPerson;
    }
}

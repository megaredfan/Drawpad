package joseph.drawpad.rpc;

import android.os.RemoteException;

/**
 * Created by 熊纪元 on 2015/12/28.
 */
public class IPersonImpl extends IPerson.Stub {
    private int age;
    private String name;

    @Override
    public void setAge(int age) throws RemoteException {
        this.age = age;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String display() throws RemoteException {
        return "name: "+name+";age: "+age;
    }
}

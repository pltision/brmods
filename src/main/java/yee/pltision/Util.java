package yee.pltision;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class Util {
    public static final String MODID= "backrooms";
    public static Random RANDOM=new Random();

    public static final int MUSHROOM_DEBUG_DEBUGGER_PORT =33017;
    public static final int MUSHROOM_DEBUG_SERVER_PORT =33018;

    public static final InetAddress LOCATE_HOST;

    static {
        InetAddress LOCATE_HOST1=null;
        try {
            LOCATE_HOST1 = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        LOCATE_HOST = LOCATE_HOST1;
    }
}

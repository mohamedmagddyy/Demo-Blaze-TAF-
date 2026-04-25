package comBlazedemoTests.utils.logs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeManager {

    public static String getTimestamp() {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getSimpleTimestamp() {
        return new Date().toString();
    }

}

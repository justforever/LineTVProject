package tw.com.linetvproject;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String convertDateFormat(String date) {
        try {
            return simpleDateFormat.format(simpleDateFormat.parse(date));
        }
        catch (ParseException e) {
            Log.d("de", e.toString());
            return "";
        }
    }
}

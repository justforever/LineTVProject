package tw.com.linetvproject;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private static final String SP_NAME = "";
    public static void saveKeyword(Context context, String keyword) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString("final_keyword", keyword).commit();
//        sp.getString("final_keyword", "");
    }

    public static String getKeyword(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString("final_keyword", "");
    }
}

package tw.com.linetvproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import androidx.room.Room;

public class DramaActions {

    private static String dbName = "dramas";

    private static AppDatabase dramaDB;

    private static DramaActions dramaActions = null;

    public DramaActions(Context context) {
        dramaDB = Room.databaseBuilder(context, AppDatabase.class, dbName).build();
    }

    public static DramaActions getInstance(Context context) {
        if (dramaActions == null) {
            dramaActions = new DramaActions(context);
        }
        return dramaActions;
    }

    public static List<DramaBean> getAll() {
        return dramaDB.dramaBeanDAO().getAll();
    }

    public DramaBean findById(int drama_id) {
        return dramaDB.dramaBeanDAO().findById(drama_id);
    }

    public void insert(DramaBean dramaBean) {
        dramaDB.dramaBeanDAO().insert(dramaBean);
    }

    public void insertTask(DramaBean dramaBean) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("de", "insert: "+dramaBean.getName());
                insert(dramaBean);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getAllTask() {
        new AsyncTask<Void, Void, List<DramaBean>>() {

            @Override
            protected List<DramaBean> doInBackground(Void... voids) {
                return getAll();
            }
        }.execute();
    }

    public void updateCollect(int drama_id, boolean collect) {
        dramaDB.dramaBeanDAO().updateCollect(drama_id, collect);
    }
}

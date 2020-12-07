package tw.com.linetvproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DramaBean.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DramaBeanDAO dramaBeanDAO();
}

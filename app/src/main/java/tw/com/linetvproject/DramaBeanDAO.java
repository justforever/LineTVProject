package tw.com.linetvproject;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DramaBeanDAO {

    @Query("SELECT * FROM dramas")
    List<DramaBean> getAll();

    @Query("SELECT * FROM dramas WHERE drama_id = :drama_id")
    DramaBean findById(int drama_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DramaBean dramaBean);

    @Delete
    void delete(DramaBean dramaBean);

    @Query("Update dramas SET collect = :collect WHERE drama_id = :drama_id")
    void updateCollect(int drama_id, boolean collect);
}

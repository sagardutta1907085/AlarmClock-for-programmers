package linc.com.alarmclockforprogrammers.model.data.database.achievements;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import linc.com.alarmclockforprogrammers.entity.Achievement;

@Dao
public interface AchievementsDao {

    @Query("SELECT * FROM achievements")
    List<Achievement> getAll();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(Achievement achievement);

    @Update
    void update(List<Achievement> questions);

}

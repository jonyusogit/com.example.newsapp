package rodriguezmunoz.jonathan.comexamplenewsapp.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.CategoryEntity;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY name ASC")
    List<CategoryEntity> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryEntity> categories);

    @Query("DELETE FROM categories")
    void deleteAll();
}
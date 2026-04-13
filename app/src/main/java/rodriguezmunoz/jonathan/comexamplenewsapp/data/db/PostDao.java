package rodriguezmunoz.jonathan.comexamplenewsapp.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.PostEntity;

@Dao
public interface PostDao {

    @Query("SELECT * FROM posts ORDER BY date DESC")
    List<PostEntity> getAllPosts();

    @Query("SELECT * FROM posts WHERE id = :id")
    PostEntity getPostById(int id);

    @Query("SELECT * FROM posts WHERE categoriesJson LIKE :categoryId ORDER BY date DESC")
    List<PostEntity> getPostsByCategory(String categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PostEntity> posts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PostEntity post);

    @Delete
    void delete(PostEntity post);

    @Query("DELETE FROM posts")
    void deleteAll();

    @Query("SELECT MAX(id) FROM posts")
    int getLatestPostId();
}
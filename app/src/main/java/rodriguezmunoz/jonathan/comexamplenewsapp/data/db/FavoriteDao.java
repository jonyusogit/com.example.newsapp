package rodriguezmunoz.jonathan.comexamplenewsapp.data.db;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.PostEntity;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM posts WHERE isFavorite = 1 ORDER BY date DESC")
    List<PostEntity> getFavorites();

    @Query("UPDATE posts SET isFavorite = :isFav WHERE id = :postId")
    void setFavorite(int postId, boolean isFav);

    @Query("SELECT isFavorite FROM posts WHERE id = :postId")
    boolean isFavorite(int postId);
}
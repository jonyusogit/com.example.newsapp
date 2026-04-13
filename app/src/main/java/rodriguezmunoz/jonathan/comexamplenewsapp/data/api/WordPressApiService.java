package rodriguezmunoz.jonathan.comexamplenewsapp.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Category;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.FeaturedMedia;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;

public interface WordPressApiService {

    @GET("wp/v2/posts")
    Call<List<Post>> getPosts(
            @Query("page")        int page,
            @Query("per_page")    int perPage,
            @Query("categories")  Integer categoryId,
            @Query("_embed")      int embed
    );

    @GET("wp/v2/posts/{id}")
    Call<Post> getPost(@Path("id") int id);

    @GET("wp/v2/categories")
    Call<List<Category>> getCategories(
            @Query("per_page") int perPage
    );

    @GET("wp/v2/media/{id}")
    Call<FeaturedMedia> getMedia(@Path("id") int mediaId);
}
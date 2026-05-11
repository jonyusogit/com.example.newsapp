package rodriguezmunoz.jonathan.comexamplenewsapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.api.RetrofitClient;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.api.WordPressApiService;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.db.AppDatabase;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.db.CategoryDao;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.db.FavoriteDao;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.db.PostDao;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Category;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.CategoryEntity;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.FeaturedMedia;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.PostEntity;

public class PostRepository {
    private final WordPressApiService apiService;
    private final PostDao postDao;
    private final CategoryDao categoryDao;
    private final FavoriteDao favoriteDao;
    private final Application app;

    public PostRepository(Application app) {
        this.app    = app;
        apiService  = RetrofitClient.getApiService();
        AppDatabase db = AppDatabase.getInstance(app);
        postDao     = db.postDao();
        categoryDao = db.categoryDao();
        favoriteDao = db.favoriteDao();
    }

    // ── Posts ──────────────────────────────────────────────────────────────
    public LiveData<Resource<List<Post>>> getPosts(int page, Integer categoryId) {
        MutableLiveData<Resource<List<Post>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getPosts(page, 10, categoryId, 1).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> posts = response.body();
                    resolveImages(posts, result);
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        if (page == 1) postDao.deleteAll();
                        postDao.insertAll(PostMapper.toEntityList(posts));
                    });
                } else {
                    loadFromCache(result);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                loadFromCache(result);
            }
        });
        return result;
    }

    private void resolveImages(List<Post> posts, MutableLiveData<Resource<List<Post>>> result) {
        final int[] pending = {posts.size()};

        if (pending[0] == 0) {
            result.setValue(Resource.success(posts));
            return;
        }

        for (Post post : posts) {
            if (post.getFeaturedMediaId() == 0) {
                pending[0]--;
                if (pending[0] == 0) result.setValue(Resource.success(posts));
                continue;
            }
            apiService.getMedia(post.getFeaturedMediaId()).enqueue(new Callback<FeaturedMedia>() {
                @Override
                public void onResponse(Call<FeaturedMedia> call, Response<FeaturedMedia> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        post.setImageUrl(fixImageUrl(response.body().getSourceUrl()));
                    }
                    pending[0]--;
                    if (pending[0] == 0) result.setValue(Resource.success(posts));
                }
                @Override
                public void onFailure(Call<FeaturedMedia> call, Throwable t) {
                    pending[0]--;
                    if (pending[0] == 0) result.setValue(Resource.success(posts));
                }
            });
        }
    }

    private void loadFromCache(MutableLiveData<Resource<List<Post>>> result) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<PostEntity> cached = postDao.getAllPosts();
            AppExecutors.getInstance().mainThread().execute(() -> {
                if (cached != null && !cached.isEmpty()) {
                    result.setValue(Resource.success(PostMapper.fromEntityList(cached)));
                } else {
                    result.setValue(Resource.error("Sin conexión y sin datos en caché", null));
                }
            });
        });
    }

    public int getLatestCachedPostId() {
        return postDao.getLatestPostId();
    }

    // ── Búsqueda ───────────────────────────────────────────────────────────
    public LiveData<Resource<List<Post>>> searchPosts(String query) {
        MutableLiveData<Resource<List<Post>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.searchPosts(query, 20).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Error en la búsqueda", null));
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                result.setValue(Resource.error("Sin conexión", null));
            }
        });
        return result;
    }

    // ── Categorías ─────────────────────────────────────────────────────────
    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> result = new MutableLiveData<>();
        apiService.getCategories(100).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                    AppExecutors.getInstance().diskIO().execute(() ->
                            categoryDao.insertAll(CategoryMapper.toEntityList(response.body()))
                    );
                } else {
                    loadCategoriesFromCache(result);
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                loadCategoriesFromCache(result);
            }
        });
        return result;
    }

    private void loadCategoriesFromCache(MutableLiveData<List<Category>> result) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<CategoryEntity> cached = categoryDao.getAllCategories();
            AppExecutors.getInstance().mainThread().execute(() ->
                    result.setValue(CategoryMapper.fromEntityList(cached))
            );
        });
    }

    // ── Favoritos ──────────────────────────────────────────────────────────
    public List<PostEntity> getFavoriteEntities() {
        return favoriteDao.getFavorites();
    }

    public void setFavorite(int postId, boolean isFav) {
        favoriteDao.setFavorite(postId, isFav);
    }
    private String fixImageUrl(String url) {
        if (url == null) return null;
        return url.replace("http://localhost", "http://10.0.2.2");
    }
}
package rodriguezmunoz.jonathan.comexamplenewsapp.ui.favorites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.repository.AppExecutors;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.repository.PostMapper;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.repository.PostRepository;

public class FavoritesViewModel extends AndroidViewModel {
    private final PostRepository repository;
    private final MutableLiveData<List<Post>> favorites = new MutableLiveData<>();

    public FavoritesViewModel(@NonNull Application app) {
        super(app);
        repository = new PostRepository(app);
        loadFavorites();
    }

    public LiveData<List<Post>> getFavorites() {
        return favorites;
    }

    public void loadFavorites() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Post> list = PostMapper.fromEntityList(
                    repository.getFavoriteEntities()
            );
            AppExecutors.getInstance().mainThread().execute(() ->
                    favorites.setValue(list)
            );
        });
    }

    public void toggleFavorite(int postId, boolean isFav) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            repository.setFavorite(postId, isFav);
            loadFavorites();
        });
    }
}
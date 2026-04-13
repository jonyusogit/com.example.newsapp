package rodriguezmunoz.jonathan.comexamplenewsapp.ui.postlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.repository.PostRepository;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.repository.Resource;

public class PostListViewModel extends AndroidViewModel {
    private final PostRepository repository;
    private final MutableLiveData<Integer> selectedCategory = new MutableLiveData<>(null);
    private final LiveData<Resource<List<Post>>> posts;

    public PostListViewModel(@NonNull Application app) {
        super(app);
        repository = new PostRepository(app);
        posts = Transformations.switchMap(selectedCategory,
                categoryId -> repository.getPosts(1, categoryId));
    }

    public LiveData<Resource<List<Post>>> getPosts() {
        return posts;
    }

    public void filterByCategory(Integer categoryId) {
        selectedCategory.setValue(categoryId);
    }

    public void refresh() {
        selectedCategory.setValue(selectedCategory.getValue());
    }
}
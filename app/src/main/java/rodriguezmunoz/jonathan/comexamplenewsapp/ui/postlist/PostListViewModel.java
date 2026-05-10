package rodriguezmunoz.jonathan.comexamplenewsapp.ui.postlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Category;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.repository.PostRepository;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.repository.Resource;

public class PostListViewModel extends AndroidViewModel {
    private final PostRepository repository;
    private final MutableLiveData<Integer> selectedCategory = new MutableLiveData<>(null);
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final LiveData<Resource<List<Post>>> posts;
    private final LiveData<List<Category>> categories;

    public PostListViewModel(@NonNull Application app) {
        super(app);
        repository = new PostRepository(app);

        posts = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.isEmpty()) {
                return Transformations.switchMap(selectedCategory,
                        catId -> repository.getPosts(1, catId));
            } else {
                return repository.searchPosts(query);
            }
        });

        categories = repository.getCategories();
    }

    public LiveData<Resource<List<Post>>> getPosts()  { return posts; }
    public LiveData<List<Category>> getCategories()   { return categories; }

    public void filterByCategory(Integer categoryId) {
        searchQuery.setValue("");
        selectedCategory.setValue(categoryId);
    }

    public void search(String query) {
        searchQuery.setValue(query);
    }

    public void clearFilter() {
        selectedCategory.setValue(null);
        searchQuery.setValue("");
    }
}
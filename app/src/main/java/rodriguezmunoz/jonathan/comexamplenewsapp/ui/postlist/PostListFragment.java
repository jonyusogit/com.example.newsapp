package rodriguezmunoz.jonathan.comexamplenewsapp.ui.postlist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rodriguezmunoz.jonathan.comexamplenewsapp.R;
import rodriguezmunoz.jonathan.comexamplenewsapp.ui.favorites.FavoritesViewModel;

public class PostListFragment extends Fragment {
    private PostListViewModel viewModel;
    private FavoritesViewModel favoritesViewModel;
    private PostAdapter postAdapter;
    private CategoryAdapter categoryAdapter;
    private ProgressBar progressBar;
    private TextView tvOffline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        tvOffline   = view.findViewById(R.id.tvOffline);

        // RecyclerView de artículos
        RecyclerView recyclerPosts = view.findViewById(R.id.recyclerPosts);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(post -> { });
        postAdapter.setFavoriteListener((post, isFav) -> {
            favoritesViewModel.toggleFavorite(post.getId(), isFav);
        });
        recyclerPosts.setAdapter(postAdapter);

        // RecyclerView de categorías horizontal
        RecyclerView recyclerCategories = view.findViewById(R.id.recyclerCategories);
        recyclerCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categoryId ->
                viewModel.filterByCategory(categoryId));
        recyclerCategories.setAdapter(categoryAdapter);

        // ViewModels
        viewModel = new ViewModelProvider(this).get(PostListViewModel.class);
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        // Observar artículos
        viewModel.getPosts().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    tvOffline.setVisibility(View.GONE);
                    if (resource.data != null) postAdapter.setPosts(resource.data);
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    tvOffline.setVisibility(View.VISIBLE);
                    if (resource.data != null) postAdapter.setPosts(resource.data);
                    break;
            }
        });

        // Observar categorías
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) categoryAdapter.setCategories(categories);
        });

        // Buscador con debounce 400ms
        SearchView searchView = view.findViewById(R.id.searchView);
        Handler searchHandler = new Handler(Looper.getMainLooper());
        final Runnable[] searchRunnable = {() -> {}};

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.search(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacks(searchRunnable[0]);
                searchRunnable[0] = () -> viewModel.search(newText);
                searchHandler.postDelayed(searchRunnable[0], 400);
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        postAdapter = null;
        categoryAdapter = null;
    }
}
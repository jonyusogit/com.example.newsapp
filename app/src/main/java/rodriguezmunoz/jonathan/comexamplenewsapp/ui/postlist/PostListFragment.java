package rodriguezmunoz.jonathan.comexamplenewsapp.ui.postlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rodriguezmunoz.jonathan.comexamplenewsapp.R;

public class PostListFragment extends Fragment {
    private PostListViewModel viewModel;
    private PostAdapter adapter;
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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter(post -> {
            // Navegación al detalle (Fase siguiente)
        });
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(PostListViewModel.class);
        viewModel.getPosts().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    tvOffline.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    tvOffline.setVisibility(View.GONE);
                    if (resource.data != null) adapter.setPosts(resource.data);
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    tvOffline.setVisibility(View.VISIBLE);
                    if (resource.data != null) adapter.setPosts(resource.data);
                    break;
            }
        });
    }
}
package rodriguezmunoz.jonathan.comexamplenewsapp.ui.postlist;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.R;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts = new ArrayList<>();
    private final OnPostClickListener listener;
    private OnFavoriteClickListener favoriteListener;

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Post post, boolean isFav);
    }

    public PostAdapter(OnPostClickListener listener) {
        this.listener = listener;
    }

    public void setFavoriteListener(OnFavoriteClickListener favoriteListener) {
        this.favoriteListener = favoriteListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(posts.get(position), listener, favoriteListener);
    }

    @Override
    public int getItemCount() { return posts.size(); }

    public void setPosts(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvTitle, tvDate, tvExcerpt;
        ImageButton btnFavorite;

        PostViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            tvTitle      = itemView.findViewById(R.id.tvTitle);
            tvDate       = itemView.findViewById(R.id.tvDate);
            tvExcerpt    = itemView.findViewById(R.id.tvExcerpt);
            btnFavorite  = itemView.findViewById(R.id.btnFavorite);
        }

        void bind(Post post, OnPostClickListener listener,
                  OnFavoriteClickListener favoriteListener) {
            tvTitle.setText(Html.fromHtml(
                    post.getTitle() != null ? post.getTitle().getRendered() : "",
                    Html.FROM_HTML_MODE_COMPACT));
            tvDate.setText(post.getDate() != null ? post.getDate().substring(0, 10) : "");
            tvExcerpt.setText(Html.fromHtml(
                    post.getExcerpt() != null ? post.getExcerpt().getRendered() : "",
                    Html.FROM_HTML_MODE_COMPACT));

            Glide.with(itemView.getContext())
                    .load(post.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgThumbnail);

            // Botón favorito
            btnFavorite.setAlpha(post.isFavorite() ? 1.0f : 0.3f);
            btnFavorite.setOnClickListener(v -> {
                if (favoriteListener != null) {
                    boolean newState = !post.isFavorite();
                    post.setFavorite(newState);
                    btnFavorite.setAlpha(newState ? 1.0f : 0.3f);
                    favoriteListener.onFavoriteClick(post, newState);
                }
            });

            itemView.setOnClickListener(v -> listener.onPostClick(post));
        }
    }
}
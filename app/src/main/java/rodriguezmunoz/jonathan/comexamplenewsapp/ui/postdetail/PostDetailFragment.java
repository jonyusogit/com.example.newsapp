package rodriguezmunoz.jonathan.comexamplenewsapp.ui.postdetail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import rodriguezmunoz.jonathan.comexamplenewsapp.R;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;

public class PostDetailFragment extends Fragment {
    private Post currentPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnShare = view.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            if (currentPost != null) sharePost(currentPost);
        });
    }

    private void sharePost(Post post) {
        String title = Html.fromHtml(
                post.getTitle().getRendered(),
                Html.FROM_HTML_MODE_COMPACT).toString();
        String url = post.getLink();
        String shareText = title + "\n" + url;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        startActivity(Intent.createChooser(shareIntent, "Compartir noticia"));
    }

    private void shareToWhatsApp(Post post) {
        String text = Html.fromHtml(
                post.getTitle().getRendered(),
                Html.FROM_HTML_MODE_COMPACT).toString() + "\n" + post.getLink();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            sharePost(post);
        }
    }
}
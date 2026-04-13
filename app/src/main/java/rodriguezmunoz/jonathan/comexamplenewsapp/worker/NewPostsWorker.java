package rodriguezmunoz.jonathan.comexamplenewsapp.worker;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.R;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.api.RetrofitClient;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.api.WordPressApiService;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.repository.PostRepository;
import retrofit2.Response;

public class NewPostsWorker extends Worker {

    public NewPostsWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            PostRepository repository = new PostRepository((Application) getApplicationContext());
            WordPressApiService api   = RetrofitClient.getApiService();

            Response<List<Post>> response = api.getPosts(1, 1, null, 0).execute();
            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                int latestApiId    = response.body().get(0).getId();
                int latestCachedId = repository.getLatestCachedPostId();

                if (latestApiId > latestCachedId) {
                    sendNotification(response.body().get(0));
                }
            }
            return Result.success();
        } catch (Exception e) {
            return Result.retry();
        }
    }

    private void sendNotification(Post post) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), "news_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Nuevo artículo")
                .setContentText(Html.fromHtml(
                        post.getTitle().getRendered(),
                        Html.FROM_HTML_MODE_COMPACT))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat.from(getApplicationContext())
                .notify((int) System.currentTimeMillis(), builder.build());
    }
}
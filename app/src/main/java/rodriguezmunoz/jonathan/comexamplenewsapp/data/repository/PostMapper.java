package rodriguezmunoz.jonathan.comexamplenewsapp.data.repository;

import java.util.ArrayList;
import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Post;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.PostEntity;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.RenderedField;

public class PostMapper {

    public static PostEntity toEntity(Post post) {
        PostEntity entity = new PostEntity();
        entity.setId(post.getId());
        entity.setDate(post.getDate());
        entity.setTitle(post.getTitle() != null ? post.getTitle().getRendered() : "");
        entity.setExcerpt(post.getExcerpt() != null ? post.getExcerpt().getRendered() : "");
        entity.setContent(post.getContent() != null ? post.getContent().getRendered() : "");
        entity.setImageUrl(post.getImageUrl());
        entity.setLink(post.getLink());
        entity.setCachedAt(System.currentTimeMillis());
        return entity;
    }

    public static List<PostEntity> toEntityList(List<Post> posts) {
        List<PostEntity> entities = new ArrayList<>();
        for (Post p : posts) entities.add(toEntity(p));
        return entities;
    }

    public static Post fromEntity(PostEntity entity) {
        Post post = new Post();
        post.setId(entity.getId());
        post.setDate(entity.getDate());
        post.setImageUrl(entity.getImageUrl());
        return post;
    }

    public static List<Post> fromEntityList(List<PostEntity> entities) {
        List<Post> posts = new ArrayList<>();
        for (PostEntity e : entities) posts.add(fromEntity(e));
        return posts;
    }
}
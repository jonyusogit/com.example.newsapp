package rodriguezmunoz.jonathan.comexamplenewsapp.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "posts")
public class PostEntity {
    @PrimaryKey
    private int id;
    private String date;
    private String title;
    private String excerpt;
    private String content;
    private String imageUrl;
    private String link;
    private String categoriesJson;
    private long cachedAt;
    private boolean isFavorite;

    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }
    public String getDate()             { return date; }
    public void setDate(String date)    { this.date = date; }
    public String getTitle()            { return title; }
    public void setTitle(String title)  { this.title = title; }
    public String getExcerpt()          { return excerpt; }
    public void setExcerpt(String e)    { this.excerpt = e; }
    public String getContent()          { return content; }
    public void setContent(String c)    { this.content = c; }
    public String getImageUrl()         { return imageUrl; }
    public void setImageUrl(String url) { this.imageUrl = url; }
    public String getLink()             { return link; }
    public void setLink(String link)    { this.link = link; }
    public String getCategoriesJson()           { return categoriesJson; }
    public void setCategoriesJson(String json)  { this.categoriesJson = json; }
    public long getCachedAt()               { return cachedAt; }
    public void setCachedAt(long cachedAt)  { this.cachedAt = cachedAt; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
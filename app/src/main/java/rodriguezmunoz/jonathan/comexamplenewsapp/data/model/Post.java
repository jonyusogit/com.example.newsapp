package rodriguezmunoz.jonathan.comexamplenewsapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Post {
    @SerializedName("id")             private int id;
    @SerializedName("date")           private String date;
    @SerializedName("title")          private RenderedField title;
    @SerializedName("excerpt")        private RenderedField excerpt;
    @SerializedName("content")        private RenderedField content;
    @SerializedName("featured_media") private int featuredMediaId;
    @SerializedName("categories")     private List<Integer> categories;
    @SerializedName("link")           private String link;
    private String imageUrl;

    public int getId()                  { return id; }
    public String getDate()             { return date; }
    public RenderedField getTitle()     { return title; }
    public RenderedField getExcerpt()   { return excerpt; }
    public RenderedField getContent()   { return content; }
    public int getFeaturedMediaId()     { return featuredMediaId; }
    public List<Integer> getCategories(){ return categories; }
    public String getLink()             { return link; }
    public String getImageUrl()         { return imageUrl; }
    public void setImageUrl(String url) { this.imageUrl = url; }
    public void setId(int id)           { this.id = id; }
    public void setDate(String date)    { this.date = date; }
}
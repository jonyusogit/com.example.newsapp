package rodriguezmunoz.jonathan.comexamplenewsapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")    private int id;
    @SerializedName("name")  private String name;
    @SerializedName("slug")  private String slug;
    @SerializedName("count") private int count;

    public int getId()      { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public int getCount()   { return count; }
}
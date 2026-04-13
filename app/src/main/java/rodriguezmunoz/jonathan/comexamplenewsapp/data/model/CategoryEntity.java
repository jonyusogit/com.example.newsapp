package rodriguezmunoz.jonathan.comexamplenewsapp.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryEntity {
    @PrimaryKey
    private int id;
    private String name;
    private String slug;
    private int count;

    public int getId()          { return id; }
    public void setId(int id)   { this.id = id; }
    public String getName()     { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug()     { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public int getCount()       { return count; }
    public void setCount(int count) { this.count = count; }
}
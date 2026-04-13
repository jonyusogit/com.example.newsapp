package rodriguezmunoz.jonathan.comexamplenewsapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class FeaturedMedia {
    @SerializedName("id")            private int id;
    @SerializedName("source_url")    private String sourceUrl;
    @SerializedName("media_details") private MediaDetails mediaDetails;

    public int getId()                  { return id; }
    public String getSourceUrl()        { return sourceUrl; }
    public MediaDetails getMediaDetails(){ return mediaDetails; }

    public static class MediaDetails {
        @SerializedName("sizes") private Map<String, ImageSize> sizes;
        public Map<String, ImageSize> getSizes() { return sizes; }
    }

    public static class ImageSize {
        @SerializedName("source_url") private String sourceUrl;
        @SerializedName("width")      private int width;
        @SerializedName("height")     private int height;

        public String getSourceUrl() { return sourceUrl; }
        public int getWidth()        { return width; }
        public int getHeight()       { return height; }
    }
}
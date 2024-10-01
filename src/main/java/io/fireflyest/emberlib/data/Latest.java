package io.fireflyest.emberlib.data;

import java.util.List;

/**
 * 新版本查询
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class Latest {


    @com.google.gson.annotations.SerializedName("author")
    private Author author;
    @com.google.gson.annotations.SerializedName("name")
    private String name;
    @com.google.gson.annotations.SerializedName("published_at")
    private String publishedAt;
    @com.google.gson.annotations.SerializedName("assets")
    private java.util.List<Assets> assets;
    @com.google.gson.annotations.SerializedName("body")
    private String body;

    public Latest() {
        //
    }

    public Author getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public List<Assets> getAssets() {
        return assets;
    }

    public String getBody() {
        return body;
    }

    /**
     * 作者信息
     */
    public static class Author {
        @com.google.gson.annotations.SerializedName("login")
        private String login;

        public String getLogin() {
            return login;
        }
    }

    /**
     * 资源
     */
    public static class Assets {
        @com.google.gson.annotations.SerializedName("name")
        private String name;
        @com.google.gson.annotations.SerializedName("size")
        private Long size;
        @com.google.gson.annotations.SerializedName("download_count")
        private Integer downloadCount;
        @com.google.gson.annotations.SerializedName("updated_at")
        private String updatedAt;
        @com.google.gson.annotations.SerializedName("browser_download_url")
        private String browserDownloadUrl;

        public String getName() {
            return name;
        }

        public Long getSize() {
            return size;
        }

        public Integer getDownloadCount() {
            return downloadCount;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getBrowserDownloadUrl() {
            return browserDownloadUrl;
        }

    }

}

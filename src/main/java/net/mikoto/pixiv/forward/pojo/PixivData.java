package net.mikoto.pixiv.forward.pojo;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author mikoto
 * Created at 21:30:24, 2021/9/19
 * Project: PixivRelay
 */
public class PixivData {
    private int artworkId;
    private String artworkTitle;
    private int authorId;
    private String authorName;
    private String description;
    private Map<String, String> illustUrls;
    private int pageCount;
    private int bookmarkCount;
    private int likeCount;
    private int viewCount;
    private int grading;
    private String[] tags;
    private String createDate;
    private String updateDate;

    /**
     * Change pixiv data to fast json object.
     *
     * @return A fast json object.
     */
    public JSONObject toJsonObject() {
        JSONObject outputJson = new JSONObject();
        outputJson.put("artworkId", artworkId);
        outputJson.put("artworkTitle", artworkTitle);
        outputJson.put("authorId", authorId);
        outputJson.put("authorName", authorName);
        outputJson.put("description", description);
        outputJson.put("illustUrls", illustUrls);
        outputJson.put("pageCount", pageCount);
        outputJson.put("bookmarkCount", bookmarkCount);
        outputJson.put("likeCount", likeCount);
        outputJson.put("viewCount", viewCount);
        outputJson.put("grading", grading);
        outputJson.put("tags", tags);
        outputJson.put("createDate", createDate);
        outputJson.put("updateDate", updateDate);

        return outputJson;
    }

    public int getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(int artworkId) {
        this.artworkId = artworkId;
    }

    public String getArtworkTitle() {
        return artworkTitle;
    }

    public void setArtworkTitle(String artworkTitle) {
        this.artworkTitle = artworkTitle;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getGrading() {
        return grading;
    }

    public void setGrading(int grading) {
        this.grading = grading;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Map<String, String> getIllustUrls() {
        return illustUrls;
    }

    public void setIllustUrls(Map<String, String> illustUrls) {
        this.illustUrls = illustUrls;
    }
}


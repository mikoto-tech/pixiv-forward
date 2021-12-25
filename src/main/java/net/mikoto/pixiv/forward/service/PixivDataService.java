package net.mikoto.pixiv.forward.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mikoto.pixiv.forward.pojo.PixivData;
import net.mikoto.pixiv.forward.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author mikoto
 * Created at 2:45:41, 2021/10/3
 * Project: pixiv-forward
 */
public class PixivDataService {
    /**
     * 单例
     */
    private static final PixivDataService INSTANCE = new PixivDataService();

    /**
     * error常量
     */
    private static final String ERROR = "error";

    /**
     * api链接常量
     */
    private static final String PIXIV_ARTWORK_API = "https://www.pixiv.net/ajax/illust/";

    /**
     * 构建方法
     */
    private PixivDataService() {
    }

    /**
     * 获取单例
     *
     * @return 当前实例
     */
    public static PixivDataService getInstance() {
        return INSTANCE;
    }

    /**
     * Get a pixiv data by the artwork id.
     *
     * @param artworkId The id of this artwork.
     * @return An artwork object.
     * @throws IOException IOException.
     */
    public PixivData getPixivDataById(int artworkId) throws IOException {
        PixivData pixivData = new PixivData();
        // 获取原始Json
        JSONObject rawJson = JSON.parseObject(HttpUtil.httpGet(PIXIV_ARTWORK_API + artworkId));

        if (!rawJson.getBoolean(ERROR)) {
            // 解析Json到PixivData对象
            JSONObject jsonBody = rawJson.getJSONObject("body");

            // 处理基础数据
            pixivData.setArtworkId(Integer.parseInt(jsonBody.getString(
                    "illustId")));
            pixivData.setArtworkTitle(jsonBody.getString("illustTitle"));
            pixivData.setAuthorId(Integer.parseInt(jsonBody.getString(
                    "userId")));
            pixivData.setAuthorName(jsonBody.getString("userName"));
            pixivData.setDescription(jsonBody.getString("description"));
            pixivData.setPageCount(jsonBody.getInteger("pageCount"));
            pixivData.setBookmarkCount(jsonBody.getInteger(
                    "bookmarkCount"));
            pixivData.setLikeCount(jsonBody.getInteger("likeCount"));
            pixivData.setViewCount(jsonBody.getInteger("viewCount"));
            pixivData.setCreateDate(jsonBody.getString("createDate"));
            pixivData.setUpdateDate(jsonBody.getString("uploadDate"));

            // 处理链接
            Map<String, String> urls = new HashMap<>(5);
            urls.put("mini", jsonBody.getJSONObject("urls").getString("mini"));
            urls.put("thumb", jsonBody.getJSONObject("urls").getString("thumb"));
            urls.put("small", jsonBody.getJSONObject("urls").getString("small"));
            urls.put("regular", jsonBody.getJSONObject("urls").getString("regular"));
            urls.put("original", jsonBody.getJSONObject("urls").getString(
                    "original"));
            pixivData.setIllustUrls(urls);

            // 根据标签判定年龄分级
            Set<String> tags = new HashSet<>();
            int grading = 0;
            for (int i = 0; i <
                    jsonBody.getJSONObject("tags").getJSONArray("tags").size(); i++) {
                String tag =
                        jsonBody.getJSONObject("tags").getJSONArray("tags").getJSONObject(i).getString("tag");
                if ("R-18".equals(tag)) {
                    grading = 1;
                } else if ("R-18G".equals(tag)) {
                    grading = 2;
                }

                tags.add(tag);
            }
            pixivData.setGrading(grading);
            pixivData.setTags(tags.toArray(new String[0]));

            return pixivData;
        } else {
            return null;
        }
    }
}

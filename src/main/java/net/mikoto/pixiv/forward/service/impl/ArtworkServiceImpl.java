package net.mikoto.pixiv.forward.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mikoto.pixiv.api.pojo.Artwork;
import net.mikoto.pixiv.forward.service.ArtworkService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

/**
 * @author mikoto
 * Created at 2:45:41, 2021/10/3
 * Project: pixiv-forward
 */
public class ArtworkServiceImpl implements ArtworkService {
    private static final String ERROR = "error";
    private static final String PIXIV_ARTWORK_API = "https://www.pixiv.net/ajax/illust/";
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
    private static final int SUCCESS_CODE = 200;

    /**
     * Get a pixiv data by the artwork id.
     *
     * @param artworkId The id of this artwork.
     * @return An artwork object.
     */
    @Override
    public Artwork getPixivDataById(int artworkId) throws IOException, InterruptedException {
        Artwork artwork = new Artwork();
        // build request
        Request artworkRequest = new Request.Builder()
                .url(PIXIV_ARTWORK_API + artworkId)
                .build();
        // execute a call
        Response artworkResponse = OK_HTTP_CLIENT.newCall(artworkRequest).execute();
        if (artworkResponse.code() == SUCCESS_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());

            if (!jsonObject.getBoolean(ERROR)) {
                // 解析Json到PixivData对象
                JSONObject jsonBody = jsonObject.getJSONObject("body");

                // 处理基础数据
                artwork.setArtworkId(Integer.parseInt(jsonBody.getString(
                        "illustId")));
                artwork.setArtworkTitle(jsonBody.getString("illustTitle"));
                artwork.setAuthorId(Integer.parseInt(jsonBody.getString(
                        "userId")));
                artwork.setAuthorName(jsonBody.getString("userName"));
                artwork.setDescription(jsonBody.getString("description"));
                artwork.setPageCount(jsonBody.getInteger("pageCount"));
                artwork.setBookmarkCount(jsonBody.getInteger(
                        "bookmarkCount"));
                artwork.setLikeCount(jsonBody.getInteger("likeCount"));
                artwork.setViewCount(jsonBody.getInteger("viewCount"));
                artwork.setCreateDate(jsonBody.getString("createDate"));
                artwork.setUpdateDate(jsonBody.getString("uploadDate"));

                // 处理链接
                Map<String, String> urls = new HashMap<>(5);
                urls.put("mini", jsonBody.getJSONObject("urls").getString("mini"));
                urls.put("thumb", jsonBody.getJSONObject("urls").getString("thumb"));
                urls.put("small", jsonBody.getJSONObject("urls").getString("small"));
                urls.put("regular", jsonBody.getJSONObject("urls").getString("regular"));
                urls.put("original", jsonBody.getJSONObject("urls").getString(
                        "original"));
                artwork.setIllustUrls(urls);

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
                artwork.setGrading(grading);
                artwork.setTags(tags.toArray(new String[0]));

                return artwork;
            } else {
                return null;
            }
        } else {
            Thread.sleep(500);
            return getPixivDataById(artworkId);
        }
    }

    /**
     * Get the image by url.
     *
     * @param url The url of the image.
     * @return Image bytes.
     */
    @Override
    public byte[] getImage(String url) throws IOException, InterruptedException {
        // build request
        Request artworkRequest = new Request.Builder()
                .url(url)
                .header("Referer", "https://www.pixiv.net")
                .build();
        // execute a call
        Response artworkResponse = OK_HTTP_CLIENT.newCall(artworkRequest).execute();
        if (artworkResponse.code() == SUCCESS_CODE) {
            return Objects.requireNonNull(artworkResponse.body()).bytes();
        } else {
            Thread.sleep(500);
            return getImage(url);
        }
    }


}

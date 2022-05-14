package net.mikoto.pixiv.forward.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.pojo.Artwork;
import net.mikoto.pixiv.forward.exception.ArtworkException;
import net.mikoto.pixiv.forward.service.ArtworkService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

import static net.mikoto.pixiv.api.constant.Constants.USUAL_DATE_FORMAT;
import static net.mikoto.pixiv.forward.constant.PixivApi.PIXIV_ARTWORK_API;

/**
 * @author mikoto
 * Created at 2:45:41, 2021/10/3
 * Project: pixiv-forward
 */
@Service("artworkService")
public class ArtworkServiceImpl implements ArtworkService {
    private static final String ERROR = "error";
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();
    private static final int SUCCESS_CODE = 200;
    private static final int NOT_FIND_CODE = 404;
    private static final String SERIES_NAV_DATA = "seriesNavData";

    /**
     * Get an artwork by the artwork id.
     *
     * @param artworkId The id of this artwork.
     * @return An artwork object.
     * @throws IOException      An exception.
     * @throws ArtworkException An exception.
     * @throws ParseException   An exception.
     */
    @Override
    public Artwork getArtworkById(int artworkId) throws IOException, ParseException, ArtworkException {
        Artwork artwork = new Artwork();
        // build request.
        Request artworkRequest = new Request.Builder()
                .url(PIXIV_ARTWORK_API + artworkId)
                .build();
        // execute a call.
        Response artworkResponse = OK_HTTP_CLIENT.newCall(artworkRequest).execute();
        if (artworkResponse.code() == SUCCESS_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
            artworkResponse.close();
            if (!jsonObject.getBooleanValue(ERROR)) {
                JSONObject jsonBody = jsonObject.getJSONObject("body");

                // 处理基础数据
                artwork.setArtworkId(Integer.parseInt(jsonBody.getString("illustId")));
                artwork.setArtworkTitle(jsonBody.getString("illustTitle"));
                artwork.setAuthorId(Integer.parseInt(jsonBody.getString("userId")));
                artwork.setAuthorName(jsonBody.getString("userName"));
                artwork.setDescription(jsonBody.getString("description"));
                artwork.setPageCount(jsonBody.getIntValue("pageCount"));
                artwork.setBookmarkCount(jsonBody.getIntValue("bookmarkCount"));
                artwork.setLikeCount(jsonBody.getIntValue("likeCount"));
                artwork.setViewCount(jsonBody.getIntValue("viewCount"));
                artwork.setCreateTime(USUAL_DATE_FORMAT.parse(jsonBody.getString("createDate")));
                artwork.setUpdateTime(USUAL_DATE_FORMAT.parse(jsonBody.getString("uploadDate")));
                artwork.setPatchTime(new Date());

                // 处理链接
                artwork.setIllustUrlMini(jsonBody.getJSONObject("urls").getString("mini"));
                artwork.setIllustUrlOriginal(jsonBody.getJSONObject("urls").getString("original"));
                artwork.setIllustUrlRegular(jsonBody.getJSONObject("urls").getString("regular"));
                artwork.setIllustUrlThumb(jsonBody.getJSONObject("urls").getString("thumb"));
                artwork.setIllustUrlSmall(jsonBody.getJSONObject("urls").getString("small"));

                // 根据标签判定年龄分级
                int grading = 0;

                StringJoiner stringJoiner = new StringJoiner(";");
                for (int i = 0;
                     i <
                             jsonBody
                                     .getJSONObject("tags")
                                     .getJSONArray("tags")
                                     .size();
                     i++) {
                    String tag =
                            jsonBody.getJSONObject("tags")
                                    .getJSONArray("tags")
                                    .getJSONObject(i)
                                    .getString("tag");
                    if ("R-18".equals(tag)) {
                        grading = 1;
                    } else if ("R-18G".equals(tag)) {
                        grading = 2;
                    }

                    stringJoiner.add(tag);
                }
                artwork.setGrading(grading);
                artwork.setTags(stringJoiner.toString());

                // 处理系列作品数据
                JSONObject seriesJson = jsonBody.getJSONObject(SERIES_NAV_DATA);
                if (seriesJson != null) {
                    artwork.setHasSeries(true);
                    artwork.setSeriesId(Integer.parseInt(seriesJson.getString("seriesId")));
                    artwork.setSeriesOrder(seriesJson.getIntValue("order"));
                    JSONObject previousJson = seriesJson.getJSONObject("prev");
                    if (previousJson != null) {
                        artwork.setPreviousArtworkId(Integer.parseInt(previousJson.getString("id")));
                        artwork.setPreviousArtworkTitle(previousJson.getString("title"));
                    } else {
                        artwork.setPreviousArtworkId(0);
                        artwork.setPreviousArtworkTitle(null);
                    }

                    JSONObject nextJson = seriesJson.getJSONObject("next");
                    if (nextJson != null) {
                        artwork.setNextArtworkId(Integer.parseInt(nextJson.getString("id")));
                        artwork.setNextArtworkTitle(nextJson.getString("title"));
                    } else {
                        artwork.setNextArtworkId(0);
                        artwork.setNextArtworkTitle(null);
                    }
                } else {
                    artwork.setHasSeries(false);
                    artwork.setSeriesId(0);
                    artwork.setSeriesOrder(0);
                    artwork.setNextArtworkId(0);
                    artwork.setNextArtworkTitle(null);
                    artwork.setPreviousArtworkId(0);
                    artwork.setPreviousArtworkTitle(null);
                }

                return artwork;
            } else {
                throw new ArtworkException(jsonObject.getString("message"));
            }
        } else if (artworkResponse.code() == NOT_FIND_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
            artworkResponse.close();
            throw new ArtworkException(jsonObject.getString("message"));
        } else {
            artworkResponse.close();
            throw new ArtworkException("Http response code: " + artworkResponse.code());
        }
    }

    /**
     * Get the image by url.
     *
     * @param url The url of the image.
     * @return Image bytes.
     * @throws IOException          An exception.
     * @throws InterruptedException An exception.
     */
    @Override
    public byte[] getImage(String url) throws IOException, InterruptedException {
        // build request
        Request artworkRequest = new Request.Builder()
                .url(url)
                .header("Referer", "https://www.pixiv.net")
                .get()
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

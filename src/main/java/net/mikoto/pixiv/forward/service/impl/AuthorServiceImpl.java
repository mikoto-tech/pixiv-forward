package net.mikoto.pixiv.forward.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.model.Author;
import net.mikoto.pixiv.api.model.AuthorProfile;
import net.mikoto.pixiv.api.model.PickupArtwork;
import net.mikoto.pixiv.forward.exception.PixivException;
import net.mikoto.pixiv.forward.service.AuthorService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author mikoto
 * @date 2022/6/11 16:58
 */
@Service("authorService")
public class AuthorServiceImpl implements AuthorService {
    /**
     * Instances
     */
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();

    /**
     * Constants
     */
    private static final String PIXIV_AUTHOR_API = "https://www.pixiv.net/ajax/user/";
    private static final String PIXIV_AUTHOR_PROFILE_API = "/profile/all";
    private static final String BACKGROUND = "background";
    private static final int SUCCESS_CODE = 200;
    private static final int NOT_FIND_CODE = 404;
    private static final String ERROR = "error";

    @Override
    public Author getAuthorById(int authorId) throws IOException, PixivException {
        Author author = new Author();
        // build request.
        Request artworkRequest = new Request.Builder()
                .url(PIXIV_AUTHOR_API + authorId)
                .build();
        // execute a call.
        Response artworkResponse = OK_HTTP_CLIENT.newCall(artworkRequest).execute();
        if (artworkResponse.code() == SUCCESS_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
            artworkResponse.close();
            if (!jsonObject.getBooleanValue(ERROR)) {
                JSONObject jsonBody = jsonObject.getJSONObject("body");

                author.setAuthorId(Integer.parseInt(jsonBody.getString("userId")));
                author.setAuthorName(jsonBody.getString("name"));
                author.setAuthorUrl(jsonBody.getString("image").replace("_50.jpg", ".jpg").replace("https://i.pximg.net", ""));
                author.setPremium(jsonBody.getBooleanValue("premium"));

                return author;
            } else {
                throw new PixivException(jsonObject.getString("message"));
            }
        } else if (artworkResponse.code() == NOT_FIND_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
            artworkResponse.close();
            throw new IOException(jsonObject.getString("message"));
        } else {
            artworkResponse.close();
            throw new IOException("Http response code: " + artworkResponse.code());
        }
    }

    @Override
    public AuthorProfile getAuthorProfileById(int authorId) throws IOException, PixivException {
        AuthorProfile authorProfile = new AuthorProfile();
        // build request.
        Request artworkRequest = new Request.Builder()
                .url(PIXIV_AUTHOR_API + authorId + PIXIV_AUTHOR_PROFILE_API)
                .build();
        // execute a call.
        Response artworkResponse = OK_HTTP_CLIENT.newCall(artworkRequest).execute();
        if (artworkResponse.code() == SUCCESS_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
            artworkResponse.close();
            if (!jsonObject.getBooleanValue(ERROR)) {
                JSONObject jsonBody = jsonObject.getJSONObject("body");

                Set<String> stringSet = jsonBody.getJSONObject("illusts").keySet();
                if (jsonBody.getJSONObject("manga") != null) {
                    stringSet.addAll(jsonBody.getJSONObject("manga").keySet());
                }
                System.out.println(stringSet);

                authorProfile.setArtworks(
                        Stream.of(stringSet.toArray(new String[0]))
                                .mapToInt(Integer::parseInt)
                                .boxed()
                                .toArray(Integer[]::new)
                );

                JSONArray pickups = jsonBody.getJSONArray("pickup");
                PickupArtwork[] pickupArtworks = new PickupArtwork[pickups.size()];
                for (int i = 0; i < pickups.size(); i++) {
                    JSONObject pickup = pickups.getJSONObject(i);
                    if ("illust".equals(pickup.getString("type"))) {

                    }
                }

                return authorProfile;
            } else {
                throw new PixivException(jsonObject.getString("message"));
            }
        } else if (artworkResponse.code() == NOT_FIND_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
            artworkResponse.close();
            throw new IOException(jsonObject.getString("message"));
        } else {
            artworkResponse.close();
            throw new IOException("Http response code: " + artworkResponse.code());
        }
    }
}

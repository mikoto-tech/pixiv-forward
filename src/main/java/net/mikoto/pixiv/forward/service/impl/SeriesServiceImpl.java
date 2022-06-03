package net.mikoto.pixiv.forward.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.model.Series;
import net.mikoto.pixiv.forward.exception.SeriesException;
import net.mikoto.pixiv.forward.service.SeriesService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import static net.mikoto.pixiv.api.constant.Constants.USUAL_DATE_FORMAT;

/**
 * @author mikoto
 * @date 2022/4/23 2:39
 */
@Service("seriesService")
public class SeriesServiceImpl implements SeriesService {
    private static final int SUCCESS_CODE = 200;
    private static final int NOT_FIND_CODE = 404;
    private static final String ERROR = "error";
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();
    public static final String PIXIV_SERIES_API = "https://www.pixiv.net/ajax/series/";

    @Override
    public Series getSeriesById(int seriesId) throws IOException, SeriesException, ParseException {
        Series series = new Series();
        // build request.
        Request seriesRequest = new Request.Builder()
                .url(PIXIV_SERIES_API + seriesId + "?p=1")
                .build();
        // execute a call.
        Response seriesResponse = OK_HTTP_CLIENT.newCall(seriesRequest).execute();
        if (seriesResponse.code() == SUCCESS_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(seriesResponse.body()).string());
            seriesResponse.close();
            if (!jsonObject.getBooleanValue(ERROR)) {
                // 获取body
                JSONObject jsonBody = jsonObject.getJSONObject("body");

                // 获取series数据
                JSONObject seriesJson = null;
                JSONArray illustSeries = jsonBody.getJSONArray("illustSeries");
                for (int i = 0; i < illustSeries.size(); i++) {
                    if (illustSeries.getJSONObject(i).getString("id").equals(String.valueOf(seriesId))) {
                        seriesJson = illustSeries.getJSONObject(i);
                    }
                }
                if (seriesJson == null) {
                    throw new SeriesException("Null series.");
                }

                // 解析原始数据
                series.setSeriesId(seriesId);
                series.setAuthorId(Integer.parseInt(seriesJson.getString("userId")));
                series.setSeriesTitle(seriesJson.getString("title"));
                series.setDescription(seriesJson.getString("description"));
                series.setFrontImageUrl(seriesJson.getString("url"));
                series.setCreateTime(USUAL_DATE_FORMAT.parse(seriesJson.getString("createDate")));
                series.setUpdateTime(USUAL_DATE_FORMAT.parse(seriesJson.getString("updateDate")));
                series.setPatchTime(new Date());
                return series;
            } else {
                throw new SeriesException(jsonObject.getString("message"));
            }

        } else if (seriesResponse.code() == NOT_FIND_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(seriesResponse.body()).string());
            seriesResponse.close();
            throw new SeriesException(jsonObject.getString("message"));
        } else {
            seriesResponse.close();
            throw new SeriesException("Http response code: " + seriesResponse.code());
        }
    }
}

package net.mikoto.pixiv.forward.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.http.forward.series.GetInformation;
import net.mikoto.pixiv.api.pojo.Series;
import net.mikoto.pixiv.forward.service.SeriesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static net.mikoto.pixiv.api.http.HttpApi.FORWARD_SERIES;
import static net.mikoto.pixiv.api.http.HttpApi.FORWARD_SERIES_GET_INFORMATION;
import static net.mikoto.pixiv.api.util.RsaUtil.getPrivateKey;
import static net.mikoto.pixiv.api.util.RsaUtil.sign;
import static net.mikoto.pixiv.api.util.Sha256Util.getSha256;
import static net.mikoto.pixiv.forward.constant.Constant.MAIN_PROPERTIES;

/**
 * @author mikoto
 * @date 2022/5/1 14:06
 */
@RestController
@RequestMapping(
        FORWARD_SERIES
)
public class SeriesController implements GetInformation {
    private static final String PIXIV_FORWARD_KEY = "PIXIV_FORWARD_KEY";
    private static final String RSA_PRIVATE_KEY = "RSA_PRIVATE_KEY";
    @Qualifier("seriesService")
    private final SeriesService seriesService;

    @Autowired
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.GET
    )
    public String seriesHttpApi() {
        return "<script>window.location.href='" + FORWARD_SERIES + "/index.html'</script>";
    }

    /**
     * Get information
     *
     * @param response A http servlet response object.
     * @param key      The key of Pixiv-Forward.
     * @param seriesId The id of the series.
     * @return Result.
     */
    @RequestMapping(
            value = FORWARD_SERIES_GET_INFORMATION,
            method = RequestMethod.GET
    )
    @Override
    public JSONObject getInformationHttpApi(@NotNull HttpServletResponse response, @NotNull String key, int seriesId) {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject outputJson = new JSONObject();

        if (key.equals(MAIN_PROPERTIES.getProperty(PIXIV_FORWARD_KEY))) {
            try {
                Series series = seriesService.getSeriesById(seriesId);
                if (series != null) {
                    JSONObject body = JSON.parseObject(JSON.toJSONString(series));
                    outputJson.put("body", body);
                    outputJson.put("sign", sign(getSha256(body.toJSONString()), getPrivateKey(MAIN_PROPERTIES.getProperty(RSA_PRIVATE_KEY))));
                    outputJson.put("success", true);
                    outputJson.put("message", "");
                } else {
                    outputJson.put("body", null);
                    outputJson.put("success", false);
                    outputJson.put("message", "Cannot find series.");
                }
            } catch (Exception e) {
                outputJson.put("success", false);
                outputJson.put("message", e.getMessage());
            }
        } else {
            outputJson.put("success", false);
            outputJson.put("message", "Wrong key!");
        }

        return outputJson;
    }
}

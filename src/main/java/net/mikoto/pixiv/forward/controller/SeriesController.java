package net.mikoto.pixiv.forward.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.core.model.Series;
import net.mikoto.pixiv.forward.service.SeriesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author mikoto
 * @date 2022/5/1 14:06
 */
@RestController
@RequestMapping(
        "/series"
)
public class SeriesController {
    /**
     * Instances
     */
    @Qualifier("seriesService")
    private final SeriesService seriesService;

    /**
     * Variables
     */
    @Value("${mikoto.pixiv.forward.key}")
    private String forwardKey;
    @Value("${mikoto.pixiv.forward.key.enable}")
    private boolean keyEnable;

    @Autowired
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
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
            value = "/getInformation",
            method = RequestMethod.GET
    )
    public JSONObject getInformationHttpApi(@NotNull HttpServletResponse response, String key, int seriesId) {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject outputJson = new JSONObject();

        if (!keyEnable || key.equals(forwardKey)) {
            try {
                Series series = seriesService.getSeriesById(seriesId);
                if (series != null) {
                    JSONObject body = JSON.parseObject(JSON.toJSONString(series));
                    outputJson.put("body", body);
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

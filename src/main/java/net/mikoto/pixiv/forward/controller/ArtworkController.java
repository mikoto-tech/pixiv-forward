package net.mikoto.pixiv.forward.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.core.connector.DirectConnector;
import net.mikoto.pixiv.core.model.Artwork;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author mikoto
 * Created at 17:15:44, 2021/9/20
 * Project: pixiv-forward
 */
@RestController
@RequestMapping(
        "/artwork"
)
public class ArtworkController {

    /**
     * Instances
     */
    @Qualifier
    private final DirectConnector directConnector;

    /**
     * Variables
     */
    @Value("${mikoto.pixiv.forward.key}")
    private String forwardKey;
    @Value("${mikoto.pixiv.forward.key.enable}")
    private boolean keyEnable;

    @Autowired
    public ArtworkController(DirectConnector directConnector) {
        this.directConnector = directConnector;
    }

    @RequestMapping(
            value = "/getInformation"
    )
    public JSONObject getInformationHttpApi(@NotNull HttpServletResponse response,
                                            String key,
                                            int artworkId) {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject outputJson = new JSONObject();

        if (!keyEnable || key.equals(forwardKey)) {
            try {
                Artwork artwork = directConnector.getArtwork(artworkId);
                if (artwork != null) {
                    JSONObject body = JSON.parseObject(JSON.toJSONString(artwork));
                    outputJson.put("body", body);
                    outputJson.put("success", true);
                    outputJson.put("message", "");
                } else {
                    outputJson.put("body", null);
                    outputJson.put("success", false);
                    outputJson.put("message", "Cannot find artwork.");
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

    @RequestMapping(
            value = "/getImage",
            produces = "image/jpeg"
    )
    public byte[] getImageHttpApi(@NotNull String key,
                                  String url) {
        if (!keyEnable || key.equals(forwardKey)) {
            return directConnector.getImage(url);
        } else {
            return null;
        }
    }
}

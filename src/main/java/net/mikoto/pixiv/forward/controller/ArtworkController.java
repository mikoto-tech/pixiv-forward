package net.mikoto.pixiv.forward.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.http.forward.artwork.GetImage;
import net.mikoto.pixiv.api.http.forward.artwork.GetInformation;
import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.direct.connector.DirectConnector;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static net.mikoto.pixiv.api.http.HttpApi.*;

/**
 * @author mikoto
 * Created at 17:15:44, 2021/9/20
 * Project: pixiv-forward
 */
@RestController
@RequestMapping(
        FORWARD_ARTWORK
)
public class ArtworkController implements GetInformation, GetImage {
    /**
     * Constants
     */
    private static final String PIXIV_IMAGE_URL = "https://i.pximg.net";

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

    @Autowired
    public ArtworkController(DirectConnector directConnector) {
        this.directConnector = directConnector;
    }

    @RequestMapping(
            value = FORWARD_ARTWORK_GET_INFORMATION
    )
    @Override
    public JSONObject getInformationHttpApi(@NotNull HttpServletResponse response,
                                            @NotNull String key,
                                            int artworkId) {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject outputJson = new JSONObject();

        if (key.equals(forwardKey)) {
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
            value = FORWARD_ARTWORK_GET_IMAGE,
            produces = "image/jpeg"
    )
    @Override
    public byte[] getImageHttpApi(@NotNull HttpServletResponse response,
                                  @NotNull String key,
                                  String url) {
        if (key.equals(forwardKey)) {
            return directConnector.getImage(url);
        } else {
            return null;
        }
    }
}

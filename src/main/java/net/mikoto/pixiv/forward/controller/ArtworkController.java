package net.mikoto.pixiv.forward.controller;

import com.alibaba.fastjson.JSONObject;
import net.mikoto.pixiv.forward.pojo.Artwork;
import net.mikoto.pixiv.forward.service.ArtworkService;
import net.mikoto.pixiv.forward.service.impl.ArtworkServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static net.mikoto.pixiv.forward.constant.Properties.MAIN_PROPERTIES;

/**
 * @author mikoto
 * Created at 17:15:44, 2021/9/20
 * Project: pixiv-forward
 */
@RestController
public class ArtworkController {
    private static final ArtworkService ARTWORK_SERVICE = new ArtworkServiceImpl();
    private static final String PIXIV_FORWARD_KEY = "PIXIV_FORWARD_KEY";

    @RequestMapping(value = "/getArtworkInformation", method =
            RequestMethod.GET, produces = {
            "application/JSON"
    })
    public String artworkInformation(@NotNull @RequestParam String artworkId,
                                     @NotNull @RequestParam String key) {
        JSONObject outputJson = new JSONObject();

        if (key.equals(MAIN_PROPERTIES.getProperty(PIXIV_FORWARD_KEY))) {
            try {
                Artwork pixivData = ARTWORK_SERVICE.getPixivDataById(Integer.parseInt(artworkId));
                if (pixivData != null) {
                    outputJson.put("body", pixivData.toJsonObject());
                    outputJson.put("success", true);
                    outputJson.put("message", "");
                } else {
                    outputJson.put("body", null);
                    outputJson.put("success", false);
                    outputJson.put("message", "Cannot find artwork.");
                }
            } catch (IOException | InterruptedException e) {
                outputJson.put("success", false);
                outputJson.put("message", e.getMessage());
            }
        } else {
            outputJson.put("success", false);
            outputJson.put("message", "Wrong key!");
        }

        return outputJson.toJSONString();
    }

    @RequestMapping(value = "/getImage", method =
            RequestMethod.GET, produces = {
            "image/jpeg"
    })
    public byte[] getImage(@RequestParam @NotNull String key,
                           @RequestParam @NotNull String url) throws IOException, InterruptedException {
        if (key.equals(MAIN_PROPERTIES.getProperty(PIXIV_FORWARD_KEY))) {
            return ARTWORK_SERVICE.getImage(url);
        } else {
            return null;
        }
    }
}

package net.mikoto.pixiv.forward.controller;

import com.alibaba.fastjson.JSONObject;
import net.mikoto.pixiv.api.http.forward.artwork.GetImage;
import net.mikoto.pixiv.api.http.forward.artwork.GetInformation;
import net.mikoto.pixiv.api.pojo.Artwork;
import net.mikoto.pixiv.forward.service.ArtworkService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.mikoto.pixiv.api.http.HttpApi.*;
import static net.mikoto.pixiv.forward.constant.Properties.MAIN_PROPERTIES;
import static net.mikoto.pixiv.forward.util.RsaUtil.getPrivateKey;
import static net.mikoto.pixiv.forward.util.RsaUtil.sign;
import static net.mikoto.pixiv.forward.util.Sha256Util.getSha256;

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
    @Qualifier("artworkService")
    private final ArtworkService artworkService;
    private static final String PIXIV_FORWARD_KEY = "PIXIV_FORWARD_KEY";
    private static final String RSA_PRIVATE_KEY = "RSA_PRIVATE_KEY";

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.GET
    )
    public String artworkHttpApi() {
        return "<script>window.location.href='" + FORWARD_ARTWORK + "/index.html'</script>";
    }

    @RequestMapping(
            value = FORWARD_ARTWORK_GET_INFORMATION,
            method = RequestMethod.GET
    )
    @Override
    public JSONObject getInformationHttpApi(@NotNull HttpServletResponse response,
                                            @RequestParam @NotNull String key,
                                            @RequestParam String artworkId) {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject outputJson = new JSONObject();

        if (key.equals(MAIN_PROPERTIES.getProperty(PIXIV_FORWARD_KEY))) {
            try {
                Artwork artwork = artworkService.getPixivDataById(Integer.parseInt(artworkId));
                if (artwork != null) {
                    JSONObject body = artwork.toJsonObject();
                    outputJson.put("body", body);
                    outputJson.put("sign", sign(getSha256(body.toJSONString()), getPrivateKey(MAIN_PROPERTIES.getProperty(RSA_PRIVATE_KEY))));
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
            method = RequestMethod.GET,
            produces = "image/jpeg"
    )
    @Override
    public byte[] getImageHttpApi(@NotNull HttpServletResponse response,
                                  @RequestParam @NotNull String key,
                                  @RequestParam String url) throws IOException, InterruptedException {
        if (key.equals(MAIN_PROPERTIES.getProperty(PIXIV_FORWARD_KEY))) {
            return artworkService.getImage(url);
        } else {
            return null;
        }
    }
}

package net.mikoto.pixiv.forward.controller;

import com.alibaba.fastjson.JSONObject;
import net.mikoto.pixiv.forward.pojo.PixivData;
import net.mikoto.pixiv.forward.pojo.User;
import net.mikoto.pixiv.forward.service.PixivDataService;
import net.mikoto.pixiv.forward.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static net.mikoto.pixiv.forward.util.HttpUtil.encode;

/**
 * @author mikoto
 * Created at 17:15:44, 2021/9/20
 * Project: pixiv-forward
 */
@RestController
public class ArtworkInformationController {
    /**
     * Get artwork information.
     *
     * @param request   The httpServlet request.
     * @param artworkId The id of this artwork.
     * @return Json string.
     */
    @RequestMapping(value = "/getArtworkInformation", method =
            RequestMethod.GET, produces = {
            "application/JSON"
    })
    public String artworkInformation(HttpServletRequest request,
                                     @RequestParam String artworkId,
                                     @RequestParam String key) {
        // 初始化对象实例
        JSONObject outputJson = new JSONObject();

        User user = UserService.getInstance().getUser(key);
        if (user.getUserKey() != null) {
            if (user.getUserKey().equals(key)) {
                try {
                    // 通过作品Id获取作品详细信息
                    PixivData pixivData = PixivDataService.getInstance().getPixivDataById(Integer.parseInt(artworkId));
                    if (pixivData != null) {
                        outputJson.put("body", pixivData.toJsonObject());
                        outputJson.put("mikotoAccountId", user.getId());
                        outputJson.put("mikotoAccountName", user.getUserName());
                        outputJson.put("error", false);
                        outputJson.put("message", "");
                    } else {
                        outputJson.put("body", null);
                        outputJson.put("mikotoAccountId", user.getId());
                        outputJson.put("mikotoAccountName", user.getUserName());
                        outputJson.put("error", true);
                        outputJson.put("message", "Cannot find artwork.");
                    }
                } catch (IOException e) {
                    // 输出错误信息
                    outputJson.put("error", true);
                    outputJson.put("message", e.getMessage());
                }
            } else {
                outputJson.put("error", true);
                outputJson.put("message", "Wrong key!");
            }
        } else {
            outputJson.put("error", true);
            outputJson.put("message", "Wrong key!");
        }

        return encode(outputJson.toJSONString());
    }
}

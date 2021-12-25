package net.mikoto.pixiv.forward.controller;

import net.mikoto.pixiv.forward.pojo.User;
import net.mikoto.pixiv.forward.service.UserService;
import net.mikoto.pixiv.forward.util.HttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static net.mikoto.pixiv.forward.util.FileUtil.inputStream2ByteArray;

/**
 * @author mikoto
 * Created at 22:35:40, 2021/9/20
 * Project: pixiv-forward
 */
@RestController
public class ImageController {
    private static final String ERROR_1_PATH = "error1.png";
    private static final String ERROR_2_PATH = "error2.png";

    /**
     * Get image.
     *
     * @param request The httpServlet request.
     * @param url     The url of this image.
     * @return Image byte.
     */
    @RequestMapping(value = "/getImage", method =
            RequestMethod.GET, produces = {
            "image/jpeg"
    })
    public byte[] getImage(HttpServletRequest request,
                           @RequestParam String url,
                           @RequestParam String key) {

        User user = UserService.getInstance().getUser(key);
        if (user.getUserKey() != null) {
            if (user.getUserKey().equals(key)) {
                byte[] imageData;
                try {
                    imageData = HttpUtil.httpGetWithReferer(url);
                } catch (IOException | IllegalArgumentException e) {
                    byte[] fileData = null;
                    try {
                        fileData = inputStream2ByteArray(ERROR_1_PATH);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    // 返回错误信息
                    return fileData;
                }
                return imageData;
            } else {
                // 返回错误信息
                byte[] fileData = null;
                try {
                    fileData = inputStream2ByteArray(ERROR_2_PATH);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // 返回错误信息
                return fileData;
            }
        } else {
            // 返回错误信息
            byte[] fileData = null;
            try {
                fileData = inputStream2ByteArray(ERROR_2_PATH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // 返回错误信息
            return fileData;
        }
    }
}

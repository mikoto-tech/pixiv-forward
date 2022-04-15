package net.mikoto.pixiv.forward.controller;

import net.mikoto.pixiv.api.http.forward.web.PublicKey;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static net.mikoto.pixiv.api.http.HttpApi.FORWARD_WEB;
import static net.mikoto.pixiv.api.http.HttpApi.FORWARD_WEB_PUBLIC_KEY;
import static net.mikoto.pixiv.forward.constant.Properties.MAIN_PROPERTIES;

/**
 * @author mikoto
 * @date 2022/3/19 19:36
 */
@RestController
@RequestMapping(
        FORWARD_WEB
)
public class WebController implements PublicKey {
    private static final String RSA_PUBLIC_KEY = "RSA_PUBLIC_KEY";

    @RequestMapping(
            value = "",
            method = RequestMethod.GET
    )
    public String forwardHttpApi() {
        return "<script>window.location.href='" + FORWARD_WEB + "/index.html'</script>";
    }

    @RequestMapping(
            value = FORWARD_WEB_PUBLIC_KEY,
            method = RequestMethod.GET
    )
    @Override
    public String publicKeyHttpApi(HttpServletResponse response) {
        return MAIN_PROPERTIES.getProperty(RSA_PUBLIC_KEY);
    }
}

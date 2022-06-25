package net.mikoto.pixiv.forward.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.http.forward.author.GetInformation;
import net.mikoto.pixiv.api.http.forward.author.GetProfile;
import net.mikoto.pixiv.api.model.Author;
import net.mikoto.pixiv.api.model.AuthorProfile;
import net.mikoto.pixiv.forward.service.AuthorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static net.mikoto.pixiv.api.http.HttpApi.*;

/**
 * @author mikoto
 * @date 2022/6/11 20:08
 */
@RestController
@RequestMapping(
        FORWARD_AUTHOR
)
public class AuthorController implements GetInformation, GetProfile {
    /**
     * Instances
     */
    @Qualifier("authorService")
    private final AuthorService authorService;

    /**
     * Variables
     */
    @Value("${mikoto.pixiv.forward.key}")
    private String forwardKey;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping(
            value = FORWARD_AUTHOR_GET_INFORMATION
    )
    @Override
    public JSONObject getInformationHttpApi(@NotNull HttpServletResponse response, @NotNull String key, int authorId) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject outputJson = new JSONObject();

        if (key.equals(forwardKey)) {
            try {
                Author author = authorService.getAuthorById(authorId);
                if (author != null) {
                    JSONObject body = JSON.parseObject(JSON.toJSONString(author));
                    outputJson.put("body", body);
                    outputJson.put("success", true);
                    outputJson.put("message", "");
                } else {
                    outputJson.put("body", null);
                    outputJson.put("success", false);
                    outputJson.put("message", "Cannot find author.");
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
            value = FORWARD_AUTHOR_GET_PROFILE
    )
    @Override
    public JSONObject getProfileHttpApi(@NotNull HttpServletResponse response, @NotNull String key, int authorId) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject outputJson = new JSONObject();

        if (key.equals(forwardKey)) {
            try {
                AuthorProfile author = authorService.getAuthorProfileById(authorId);
                if (author != null) {
                    JSONObject body = JSON.parseObject(JSON.toJSONString(author));
                    outputJson.put("body", body);
                    outputJson.put("success", true);
                    outputJson.put("message", "");
                } else {
                    outputJson.put("body", null);
                    outputJson.put("success", false);
                    outputJson.put("message", "Cannot find author profile.");
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

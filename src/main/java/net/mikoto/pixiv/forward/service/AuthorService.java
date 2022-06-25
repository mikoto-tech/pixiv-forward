package net.mikoto.pixiv.forward.service;

import net.mikoto.pixiv.api.model.Author;
import net.mikoto.pixiv.api.model.AuthorProfile;
import net.mikoto.pixiv.forward.exception.PixivException;

import java.io.IOException;

/**
 * @author mikoto
 * @date 2022/6/11 16:58
 */
public interface AuthorService {
    Author getAuthorById(int authorId) throws IOException, PixivException;

    AuthorProfile getAuthorProfileById(int authorId) throws IOException, PixivException;
}

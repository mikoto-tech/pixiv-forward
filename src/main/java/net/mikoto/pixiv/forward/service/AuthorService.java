package net.mikoto.pixiv.forward.service;

import net.mikoto.pixiv.api.model.Author;

/**
 * @author mikoto
 * @date 2022/6/4 5:11
 */
public interface AuthorService {
    /**
     * Get the author by id.
     *
     * @param authorId The author id.
     * @return The author object.
     */
    Author getAuthorById(int authorId);
}

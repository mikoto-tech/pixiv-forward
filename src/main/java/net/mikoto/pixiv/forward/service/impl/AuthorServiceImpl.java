package net.mikoto.pixiv.forward.service.impl;

import net.mikoto.pixiv.api.model.Author;
import net.mikoto.pixiv.forward.service.AuthorService;
import org.springframework.stereotype.Service;

/**
 * @author mikoto
 * @date 2022/6/4 5:11
 */
@Service("authorService")
public class AuthorServiceImpl implements AuthorService {
    @Override
    public Author getAuthorById(int authorId) {
        return null;
    }
}

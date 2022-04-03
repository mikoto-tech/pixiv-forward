package net.mikoto.pixiv.forward.exception;

import java.io.IOException;

/**
 * @author mikoto
 * @date 2022/4/3 12:01
 */
public class ArtworkException extends IOException {
    public ArtworkException(String message) {
        super("Get artwork failed: " + message);
    }
}

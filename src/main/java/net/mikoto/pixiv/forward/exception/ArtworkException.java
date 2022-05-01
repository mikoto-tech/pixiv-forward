package net.mikoto.pixiv.forward.exception;


/**
 * @author mikoto
 * @date 2022/4/3 12:01
 */
public class ArtworkException extends Exception {
    public ArtworkException(String message) {
        super("Get artwork failed: " + message);
    }
}

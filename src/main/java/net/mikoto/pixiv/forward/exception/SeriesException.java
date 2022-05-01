package net.mikoto.pixiv.forward.exception;

/**
 * @author mikoto
 * @date 2022/4/23 2:52
 */
public class SeriesException extends Exception {
    public SeriesException(String message) {
        super("Get series failed: " + message);
    }
}

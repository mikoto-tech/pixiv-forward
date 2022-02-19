package net.mikoto.pixiv.forward.service;

import net.mikoto.pixiv.forward.pojo.Artwork;

import java.io.IOException;

/**
 * @author mikoto
 * @date 2022/2/19 10:08
 */
public interface ArtworkService {
    /**
     * Get a pixiv data by the artwork id.
     *
     * @param artworkId The id of this artwork.
     * @return An artwork object.
     * @throws IOException          IOException.
     * @throws InterruptedException An error.
     */
    Artwork getPixivDataById(int artworkId) throws IOException, InterruptedException;

    /**
     * Get the image by url.
     *
     * @param url The url of the image.
     * @return Image bytes.
     * @throws IOException          An error.
     * @throws InterruptedException An error.
     */
    byte[] getImage(String url) throws IOException, InterruptedException;
}

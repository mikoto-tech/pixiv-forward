package net.mikoto.pixiv.forward.service;


import net.mikoto.pixiv.api.pojo.Artwork;
import net.mikoto.pixiv.forward.exception.ArtworkException;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author mikoto
 * @date 2022/2/19 10:08
 */
public interface ArtworkService {
    /**
     * Get an artwork by the artwork id.
     *
     * @param artworkId The id of this artwork.
     * @return An artwork object.
     * @throws IOException      An exception.
     * @throws ArtworkException An exception.
     * @throws ParseException   An exception.
     */
    Artwork getArtworkById(int artworkId) throws IOException, ArtworkException, ParseException;

    /**
     * Get the image by url.
     *
     * @param url The url of the image.
     * @return Image bytes.
     * @throws IOException          An exception.
     * @throws InterruptedException An exception.
     */
    byte[] getImage(String url) throws IOException, InterruptedException;
}

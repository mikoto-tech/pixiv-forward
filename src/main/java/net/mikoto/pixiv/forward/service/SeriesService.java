package net.mikoto.pixiv.forward.service;

import net.mikoto.pixiv.api.pojo.Series;
import net.mikoto.pixiv.forward.exception.SeriesException;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author mikoto
 * @date 2022/4/23 2:39
 */
public interface SeriesService {
    /**
     * Get the series by id.
     *
     * @param seriesId The series id.
     * @return The series.
     * @throws IOException     An exception.
     * @throws SeriesException An exception.
     * @throws ParseException  An exception.
     */
    Series getSeriesById(int seriesId) throws IOException, SeriesException, ParseException;
}

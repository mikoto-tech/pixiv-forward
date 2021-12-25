package net.mikoto.pixiv.forward.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author mikoto
 * @date 2021/12/11 11:01
 */
public class FileUtil {
    /**
     * Get file.
     *
     * @param filePath Path.
     * @return File data.
     * @throws IOException Cannot find file.
     */
    public static byte[] inputStream2ByteArray(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        InputStream in = resource.getInputStream();
        byte[] data = toByteArray(in);
        in.close();
        return data;
    }

    /**
     * Read input stream to bytes.
     *
     * @param in Input stream.
     * @return Bytes.
     * @throws IOException Error.
     */
    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}

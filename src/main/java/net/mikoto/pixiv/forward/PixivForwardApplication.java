package net.mikoto.pixiv.forward;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static net.mikoto.pixiv.forward.constant.Properties.MAIN_PROPERTIES;
import static net.mikoto.pixiv.forward.util.FileUtil.createDir;
import static net.mikoto.pixiv.forward.util.FileUtil.createFile;

/**
 * @author mikoto
 */
@SpringBootApplication
public class PixivForwardApplication {
    public static void main(String[] args) throws IOException {
        createDir("config");
        createFile(new File("config/config.properties"), IOUtils.toString(Objects.requireNonNull(PixivForwardApplication.class.getClassLoader().getResourceAsStream("config.properties")), StandardCharsets.UTF_8));
        MAIN_PROPERTIES.load(new FileReader("config/config.properties"));

        SpringApplication.run(PixivForwardApplication.class, args);
    }
}

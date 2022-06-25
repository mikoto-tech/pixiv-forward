package net.mikoto.pixiv.forward;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mikoto
 */
@SpringBootApplication
@ComponentScan("net.mikoto.pixiv")
@ForestScan(basePackages = "net.mikoto.pixiv.direct.connector.client")
public class PixivForwardApplication {
    public static void main(String[] args) {
        SpringApplication.run(PixivForwardApplication.class, args);
    }
}

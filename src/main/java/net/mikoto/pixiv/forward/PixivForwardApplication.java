package net.mikoto.pixiv.forward;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mikoto
 */
@SpringBootApplication
@ComponentScan("net.mikoto.pixiv.core.connector.impl.independence")
@ComponentScan("net.mikoto.pixiv.forward")
@ForestScan(basePackages = "net.mikoto.pixiv.core.client")
public class PixivForwardApplication {
    public static void main(String[] args) {
        SpringApplication.run(PixivForwardApplication.class, args);
    }
}

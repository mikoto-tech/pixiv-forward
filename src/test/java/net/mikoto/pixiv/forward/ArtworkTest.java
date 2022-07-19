package net.mikoto.pixiv.forward;

import com.dtflys.forest.springboot.annotation.ForestScan;
import net.mikoto.pixiv.core.connector.ForwardConnector;
import net.mikoto.pixiv.core.model.Artwork;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author mikoto
 * Create for pixiv-forward
 * Create at 2022/7/20
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan("net.mikoto.pixiv")
@ForestScan("net.mikoto.pixiv")
@TestPropertySource("classpath:application.properties")
public class ArtworkTest {
    @Value("${mikoto.pixiv.forward.key}")
    private final String key = "";
    @Qualifier
    private final ForwardConnector forwardConnector;
    @LocalServerPort
    private int port;
    private String address = "http://127.0.0.1";

    @Autowired
    public ArtworkTest(ForwardConnector forwardConnector) {
        this.forwardConnector = forwardConnector;
    }

    @Test
    public void getInformationTest() {
        address += ":" + port;
        Artwork artwork = forwardConnector.getArtworkSingleServer(address, key, 91262365);

        assertEquals(91262365, artwork.getArtworkId());
        assertEquals("初音ミク", artwork.getArtworkTitle());
        assertEquals(3259336, artwork.getAuthorId());
        assertEquals("/c/48x48/custom-thumb/img/2021/07/16/00/48/17/91262365_p0_custom1200.jpg", artwork.getIllustUrlMini());
        assertEquals("/img-original/img/2021/07/16/00/48/17/91262365_p0.jpg", artwork.getIllustUrlOriginal());
        assertEquals("/img-master/img/2021/07/16/00/48/17/91262365_p0_master1200.jpg", artwork.getIllustUrlRegular());
        assertEquals("/c/540x540_70/img-master/img/2021/07/16/00/48/17/91262365_p0_master1200.jpg", artwork.getIllustUrlSmall());
        assertEquals("/c/250x250_80_a2/custom-thumb/img/2021/07/16/00/48/17/91262365_p0_custom1200.jpg", artwork.getIllustUrlThumb());
        assertEquals("初音ミク;足裏;足指;女の子;つま先;裸足;ギリシャ型;美脚;縞パン;VOCALOID10000users入り", artwork.getTags());
    }
}

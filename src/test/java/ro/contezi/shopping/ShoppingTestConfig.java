package ro.contezi.shopping;

import static org.slf4j.LoggerFactory.getLogger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.contezi.shopping.reply.ReplySender;

@Configuration
@Import(Shopping.class)
public class ShoppingTestConfig {
    private static final org.slf4j.Logger LOGGER = getLogger(ShoppingTestConfig.class);
    
    @Bean
    public ReplySender replySender() {
        return str -> LOGGER.info("{}", str);
    }

    static String mesageFromUser(String message, String facebookPageUserId) {
        return "{\"object\":\"page\",\"entry\":[{\"id\":\"535405013470180\",\"time\":1508051848445,\"messaging\":"
                + "[{\"sender\":{\"id\":\"" + facebookPageUserId + "\"},\"recipient\":{\"id\":\"535405013470180\"},\"timestamp\":1508051847330,\"message\":"
                + "{\"mid\":\"mid.$cAAHm8o_kpEtlUCpQolfHuUHC30ja\",\"seq\":1171418,\"text\":\"" + message + "\"}}]}]}";
    }

    static String quickReplyFromUser(String payload, String facebookPageUserId) {
        return "{\"object\":\"page\",\"entry\":[{\"id\":\"535405013470180\",\"time\":1510896979779,\"messaging\":["
                + "{\"sender\":{\"id\":\"" + facebookPageUserId + "\"},\"recipient\":{\"id\":\"535405013470180\"},\"timestamp\":1510896978831,"
                + "\"message\":{\"quick_reply\":{\"payload\":\"" + payload + "\"},"
                + "\"mid\":\"mid.$cAAG8Bk94BHhl-b-Tj1fyHpUbK2Qm\",\"seq\":3787,\"text\":\"OK\",\"nlp\":{\"entities\":{\"location\":[{\"suggested\":true,\"confidence\":0.929,\"value\":\"OK\",\"type\":\"value\"}]}}}}]}]}";
    }
}
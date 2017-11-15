package ro.contezi.shopping.facebook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.contezi.shopping.facebook.FacebookReply;
import ro.contezi.shopping.facebook.MessageFromFacebook;

public class FacebookReplyTest {
    
    private static final Logger LOGGER = Logger.getLogger(FacebookReplyTest.class);
    
    @Test
    public void repliesInJson() throws IOException {
        MessageFromFacebook message = new ObjectMapper().readValue("{\"object\":\"page\",\"entry\":[{\"id\":\"535405013470180\",\"time\":1508051848445,\"messaging\":"
                + "[{\"sender\":{\"id\":\"1513421495405103\"},\"recipient\":{\"id\":\"535405013470180\"},\"timestamp\":1508051847330,\"message\":"
                + "{\"mid\":\"mid.$cAAHm8o_kpEtlUCpQolfHuUHC30ja\",\"seq\":1171418,\"text\":\"hello!\"}}]}]}", 
            MessageFromFacebook.class);
        
        FacebookReply reply = new FacebookReply(message.getSender(), message.getText());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectMapper().writeValue(baos , reply);
        
        LOGGER.info(baos.toString());
    }

}

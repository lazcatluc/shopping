package ro.contezi.shopping.reply;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public class CompositeReplyProviderTest {
    
    private boolean applies;
    private CompositeReplyProvider compositeReply = new CompositeReplyProvider(s -> "default", 
            Arrays.asList(new ConditionalReplyProvider() {
                
                @Override
                public String reply(MessageFromFacebook messageFromFacebook) {
                    return "special";
                }
                
                @Override
                public boolean applies(MessageFromFacebook messageFromFacebook) {
                    return applies;
                }
            }));

    @Test
    public void respondsDefault() {
        applies = false;
        assertThat(compositeReply.reply(null)).isEqualTo("default");
    }

    @Test
    public void respondsSpecial() {
        applies = true;
        assertThat(compositeReply.reply(null)).isEqualTo("special");
    }
}

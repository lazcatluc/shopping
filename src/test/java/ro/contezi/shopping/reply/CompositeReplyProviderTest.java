package ro.contezi.shopping.reply;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.AuthorRepository;

public class CompositeReplyProviderTest {
    
    private boolean applies;
    @Mock
    private AuthorRepository authorRepository;
    @Mock(answer = Answers.RETURNS_MOCKS)
    private MessageFromFacebook facebookMessage;
    private CompositeReplyProvider compositeReply;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compositeReply = new CompositeReplyProvider(s -> "default", authorRepository,
                Collections.singletonList(new ConditionalReplyProvider() {

                    @Override
                    public String reply(MessageFromFacebook messageFromFacebook) {
                        return "special";
                    }

                    @Override
                    public boolean applies(MessageFromFacebook messageFromFacebook) {
                        return applies;
                    }
                }));
    }

    @Test
    public void respondsDefault() {
        applies = false;
        assertThat(compositeReply.reply(facebookMessage)).isEqualTo("default");
    }

    @Test
    public void respondsSpecial() {
        applies = true;
        assertThat(compositeReply.reply(facebookMessage)).isEqualTo("special");
    }
}

package ro.contezi.shopping.reply.text;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.author.AuthorRepository;
import ro.contezi.shopping.reply.text.CompositeReplier;
import ro.contezi.shopping.reply.text.ConditionalReplier;

public class CompositeReplierTest {
    
    private boolean applies;
    @Mock
    private AuthorRepository authorRepository;
    @Mock(answer = Answers.RETURNS_MOCKS)
    private MessageFromFacebook facebookMessage;
    private CompositeReplier compositeReply;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compositeReply = new CompositeReplier(s -> "default", authorRepository,
                Collections.singletonList(new ConditionalReplier() {

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

package ro.contezi.shopping;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class FacebookWebhookSignatureValidatorTest {
    private FacebookWebhookSignatureValidator facebookWebhookSignatureValidator;
    private String facebookApplicationSecret = "7a0620ed9d370809bb5b70aeeb6abcf4";
    private String body = "{\"object\":\"page\",\"entry\":[{\"id\":\"107755263254963\",\"time\":1503660976519,\"messaging\":[{\"sender\":{\"id\":\"1468502036561745\"},\"recipient\":{\"id\":\"107755263254963\"},\"timestamp\":1503660441982,\"message\":{\"mid\":\"mid.$cAABiAIR3ThxkSmrJfleGSWBPCaxc\",\"seq\":1128157,\"text\":\"hi\",\"nlp\":{\"entities\":{\"greetings\":[{\"confidence\":0.99993050098374,\"value\":\"true\"}]}}}}]}]}";

    @Before
    public void setUp() throws Exception {
        facebookWebhookSignatureValidator = new FacebookWebhookSignatureValidator(facebookApplicationSecret);
    }

    @Test
    public void generatesSha1WithKey() throws Exception {
        String signature = "sha1=2643e53b80e6a8bf17500d289645a28da536a77e";

        boolean matches = facebookWebhookSignatureValidator.isValid(signature, body);

        assertThat(matches).isTrue();
    }
    
    @Test
    public void isValidWhenSendingSecret() throws Exception {
        String signature = facebookApplicationSecret;

        boolean matches = facebookWebhookSignatureValidator.isValid(signature, body);

        assertThat(matches).isTrue();
    }
    
}

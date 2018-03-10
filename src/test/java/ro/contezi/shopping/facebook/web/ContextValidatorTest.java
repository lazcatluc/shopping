package ro.contezi.shopping.facebook.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextValidatorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextValidatorTest.class);
    private ContextValidator contextValidator;

    @Test
    public void validatesContext() throws Exception {
        contextValidator = new ContextValidator("36efac08a48954fd149eda20469aa81d",
                new ObjectMapper());
        FacebookContext validFacebookContext = parsePayload();

        LOGGER.info("{}", validFacebookContext);
        assertThat(validFacebookContext.getUserId()).isEqualTo("1570628139696428");
    }

    @Test(expected = ValidationException.class)
    public void doesNotValidateContextWithInvalidSecret() throws Exception {
        contextValidator = new ContextValidator("26efac08a48954fd149eda20469aa81d",
                new ObjectMapper());

        parsePayload();
    }

    private FacebookContext parsePayload() throws IOException {
        return contextValidator.getValid(
            "8pBfs1H-JJinZuonv7fxmZjBjlR_PPIALPIT8sSAkDI." +
                "eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTUxNzIxMjYyNSwibWV0YWRhdGEiOm51bGwsInBhZ" +
                "2VfaWQiOjEwNzc1NTI2MzI1NDk2MywicHNpZCI6IjE1NzA2MjgxMzk2OTY0MjgiLCJ0aHJlYWRfdHlwZSI6IlVTRVJfVE" +
                "9fUEFHRSIsInRpZCI6IjE1NzA2MjgxMzk2OTY0MjgifQ");
    }
}
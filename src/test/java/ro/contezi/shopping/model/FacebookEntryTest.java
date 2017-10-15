package ro.contezi.shopping.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FacebookEntryTest {

    @Test
    public void canDeserializeFromJson() throws Exception {
        assertThat(new ObjectMapper().readValue("{\"object\":\"page\",\"entry\":[{\"id\":\"535405013470180\",\"time\":1508051848445,\"messaging\":"
                + "[{\"sender\":{\"id\":\"1513421495405103\"},\"recipient\":{\"id\":\"535405013470180\"},\"timestamp\":1508051847330,\"message\":"
                + "{\"mid\":\"mid.$cAAHm8o_kpEtlUCpQolfHuUHC30ja\",\"seq\":1171418,\"text\":\"hello!\"}}]}]}", 
            MessageFromFacebook.class).getObject()).isEqualTo("page");
    }

}

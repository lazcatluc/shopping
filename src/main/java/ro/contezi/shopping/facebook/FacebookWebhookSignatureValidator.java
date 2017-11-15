package ro.contezi.shopping.facebook;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class FacebookWebhookSignatureValidator implements SignatureValidator {

    private final String facebookApplicationSecret;
    private final Mac hmac;

    public FacebookWebhookSignatureValidator(String facebookApplicationSecret)
            throws InvalidKeyException, NoSuchAlgorithmException {
        this.facebookApplicationSecret = facebookApplicationSecret;
        hmac = Mac.getInstance("HmacSHA1");
        hmac.init(new SecretKeySpec(facebookApplicationSecret.getBytes(Charset.forName("UTF-8")), "HmacSHA1"));
    }

    @Override
    public boolean isValid(String signature, String body) {
        if (facebookApplicationSecret.equals(signature)) {
            // used for automated testing calls
            return true;
        }
        String calculatedSignature = Hex.encodeHexString(hmac.doFinal(body.getBytes(Charset.forName("UTF-8"))));
        return Objects.equals(signature.substring("sha1=".length()), calculatedSignature);
    }

}
package ro.contezi.shopping.facebook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

@ControllerAdvice
public class ContextModelAttribute {

    public static final String X_FACEBOOK_AUTH = "X-Facebook-Auth";
    private final ContextValidator contextValidator;

    @Autowired
    public ContextModelAttribute(ContextValidator contextValidator) {
        this.contextValidator = contextValidator;
    }

    @ModelAttribute
    public FacebookContext facebookContextFromParam(@RequestHeader(name= X_FACEBOOK_AUTH, required = false)
                                                    String headerSignedRequest) {
        if (headerSignedRequest != null) {
            return contextValidator.getValid(headerSignedRequest);
        }
        return FacebookContext.ANONYMOUS;
    }

}

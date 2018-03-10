package ro.contezi.shopping.facebook.web;

class ValidationException extends IllegalStateException {
    ValidationException(String signedRequest) {
        super(signedRequest);
    }

    ValidationException(Throwable t) {
        super(t);
    }
}

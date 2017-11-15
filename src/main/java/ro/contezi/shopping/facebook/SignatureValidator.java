package ro.contezi.shopping.facebook;

@FunctionalInterface
public interface SignatureValidator {
    boolean isValid(String signature, String payload);
}

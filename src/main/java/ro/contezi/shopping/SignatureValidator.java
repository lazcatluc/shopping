package ro.contezi.shopping;

@FunctionalInterface
public interface SignatureValidator {
    boolean isValid(String signature, String payload);
}

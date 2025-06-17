import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

class Vector {
    private byte[] iv;
    private static final int IV_LENGTH = 16; // AES 블록 크기

    // 생성자 오버로딩
    public Vector() {
        this.iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(this.iv);
    }

    public Vector(byte[] iv) {
        if (iv == null || iv.length != IV_LENGTH) {
            throw new IllegalArgumentException("IV must be " + IV_LENGTH + " bytes");
        }
        this.iv = Arrays.copyOf(iv, iv.length);
    }

    // Getter/Setter
    public byte[] getIv() {
        return Arrays.copyOf(iv, iv.length);
    }

    public void setIv(byte[] iv) {
        if (iv == null || iv.length != IV_LENGTH) {
            throw new IllegalArgumentException("IV must be " + IV_LENGTH + " bytes");
        }
        this.iv = Arrays.copyOf(iv, iv.length);
    }

    public IvParameterSpec getIvParameterSpec() {
        return new IvParameterSpec(this.iv);
    }

    public String toBase64() {
        return Base64.getEncoder().encodeToString(this.iv);
    }

    public static Vector fromBase64(String base64) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64);
            return new Vector(decoded);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Base64 IV string", e);
        }
    }

    @Override
    public String toString() {
        return "Vector{" + "iv=" + toBase64() + '}';
    }
}
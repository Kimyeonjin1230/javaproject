import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Arrays;

class SecKey {
    private SecretKey secretKey;
    private final String algorithm;

    // 생성자 오버로딩
    public SecKey(SecretKey key) {
        this.secretKey = key;
        this.algorithm = key.getAlgorithm();
    }

    public SecKey(byte[] keyBytes) {
        this.algorithm = "AES";
        this.secretKey = new SecretKeySpec(keyBytes, algorithm);
    }

    public SecKey(String base64Key) {
        this.algorithm = "AES";
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        this.secretKey = new SecretKeySpec(keyBytes, algorithm);
    }

    // Getter/Setter
    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public byte[] getEncoded() {
        return secretKey.getEncoded();
    }

    public String toBase64() {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // 키 유효성 검사
    public boolean isValid() {
        return secretKey != null && secretKey.getEncoded().length == 16; // AES-128
    }

    @Override
    public String toString() {
        return "SecKey{" + "algorithm='" + algorithm + '\'' + ", keyLength=" +
                (secretKey != null ? secretKey.getEncoded().length * 8 : 0) + " bits}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecKey secKey = (SecKey) obj;
        return Arrays.equals(this.getEncoded(), secKey.getEncoded());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getEncoded());
    }
}
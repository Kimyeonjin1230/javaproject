import javax.crypto.KeyGenerator;
import java.security.SecureRandom;

class RandomKey {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128; // AES-128

    // 정적 팩토리 메서드
    public static SecKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE);
            return new SecKey(keyGen.generateKey());
        } catch (Exception e) {
            throw new RuntimeException("키 생성 실패", e);
        }
    }

    // 시드를 사용한 키 생성
    public static SecKey generateKey(long seed) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(seed);

            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE, secureRandom);
            return new SecKey(keyGen.generateKey());
        } catch (Exception e) {
            throw new RuntimeException("시드 키 생성 실패", e);
        }
    }

    // 바이트 배열로부터 키 생성
    public static SecKey generateKeyFromBytes(byte[] keyBytes) {
        if (keyBytes.length != 16) {
            throw new IllegalArgumentException("AES 키는 16바이트여야 합니다");
        }
        return new SecKey(keyBytes);
    }
}
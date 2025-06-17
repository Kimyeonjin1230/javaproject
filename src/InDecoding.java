import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

public class InDecoding {
    private KeyManager keyManager;

    // 생성자
    public InDecoding() {
        this.keyManager = new KeyManager();
    }

    public InDecoding(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    // 암호화 메서드
    public Optional<String> encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    keyManager.getCurrentKey(),
                    keyManager.getCurrentVector());

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
            return Optional.of(encryptedText);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // 복호화 메서드
    public Optional<String> decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,
                    keyManager.getCurrentKey(),
                    keyManager.getCurrentVector());

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return Optional.of(new String(decryptedBytes));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public KeyManager getkeyManager() {
        return keyManager;
    }

    // 암호화 테스트
    public boolean testEncrypt(String testText) {
        System.out.println("테스트 중: " + testText);

        Optional<String> encrypted = encrypt(testText);
        if (encrypted.isEmpty()) {
            System.out.println("암호화 실패");
            return false;
        }

        System.out.println("암호화 결과: " + encrypted.get());

        Optional<String> decrypted = decrypt(encrypted.get());
        if (decrypted.isEmpty()) {
            System.out.println("복호화 실패");
            return false;
        }

        System.out.println("복호화 결과: " + decrypted.get());

        boolean success = decrypted.get().equals(testText);
        System.out.println(success ? "테스트 성공" : "테스트 실패");
        System.out.println();

        return success;
    }

    // KeyManager 클래스
    public static class KeyManager {
        private SecretKey secretKey;
        private IvParameterSpec ivParameterSpec;

        public KeyManager() {
            generateKeyVector();
        }

        // 새로운 키와 벡터 생성
        public void generateKeyVector() {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(256);
                this.secretKey = keyGenerator.generateKey();

                byte[] iv = new byte[16];
                new SecureRandom().nextBytes(iv);
                this.ivParameterSpec = new IvParameterSpec(iv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Base64 문자열로부터 키와 벡터 로드
        public boolean loadFromString(String keyBase64, String ivBase64) {
            try {
                byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
                byte[] ivBytes = Base64.getDecoder().decode(ivBase64);

                this.secretKey = new SecretKeySpec(keyBytes, "AES");
                this.ivParameterSpec = new IvParameterSpec(ivBytes);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public SecretKey getCurrentKey() {
            return secretKey;
        }

        public IvParameterSpec getCurrentVector() {
            return ivParameterSpec;
        }

        // 키 정보 출력
        public void printkeyInfo() {
            String keyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            String ivBase64 = Base64.getEncoder().encodeToString(ivParameterSpec.getIV());

            System.out.println("\n=== 현재 키 정보 ===");
            System.out.println("키 (Base64): " + keyBase64);
            System.out.println("벡터 (Base64): " + ivBase64);
        }
    }
}
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

public class InDecoding {
    private KeyManager keyManager;

    // 생성자 오버로딩
    public InDecoding() {
        this.keyManager = new KeyManager();
    }

    public InDecoding(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    // 암호화 메서드
    public Optional<String> encrypt(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            System.err.println("암호화할 텍스트가 비어있습니다");
            return Optional.empty();
        }

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    keyManager.getCurrentKey(),
                    keyManager.getCurrentVector());

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);

            return Optional.of(encryptedText);
        } catch (Exception e) {
            System.err.println("암호화 실패: " + e.getMessage());
            return Optional.empty();
        }
    }

    // 복호화 메서드
    public Optional<String> decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.trim().isEmpty()) {
            System.err.println("복호화할 텍스트가 비어있습니다");
            return Optional.empty();
        }

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,
                    keyManager.getCurrentKey(),
                    keyManager.getCurrentVector());

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return Optional.of(new String(decryptedBytes, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("복호화 실패: " + e.getMessage());
            return Optional.empty();
        }
    }

    // 키 매니저 교체
    public void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
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
        System.out.println(success ? "테스트 성공" : "테스트 실패 ");
        System.out.println();

        return success;
    }

    // KeyManager 클래스 (내부 클래스 또는 별도 파일로 분리 가능)
    public static class KeyManager {
        private SecretKey secretKey;
        private IvParameterSpec ivParameterSpec;
        private static final String ALGORITHM = "AES";
        private static final int KEY_LENGTH = 256;
        private static final int IV_LENGTH = 16;

        public KeyManager() {
            generateKeyVector();
        }

        // 새로운 키와 벡터 생성
        public void generateKeyVector() {
            try {
                // AES 키 생성
                KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
                keyGenerator.init(KEY_LENGTH);
                this.secretKey = keyGenerator.generateKey();

                // 초기화 벡터 생성
                byte[] iv = new byte[IV_LENGTH];
                new SecureRandom().nextBytes(iv);
                this.ivParameterSpec = new IvParameterSpec(iv);

            } catch (Exception e) {
                throw new RuntimeException("키/벡터 생성 실패: " + e.getMessage(), e);
            }
        }

        // Base64 문자열로부터 키와 벡터 로드
        public boolean loadFromString(String keyBase64, String ivBase64) {
            try {
                byte[] keyBytes = Base64.getDecoder().decode(keyBase64.trim());
                byte[] ivBytes = Base64.getDecoder().decode(ivBase64.trim());

                // 키 길이 검증 (AES-256: 32바이트, IV: 16바이트)
                if (keyBytes.length != 32) {
                    System.err.println("키 길이가 올바르지 않습니다. 32바이트여야 합니다.");
                    return false;
                }
                if (ivBytes.length != 16) {
                    System.err.println("벡터 길이가 올바르지 않습니다. 16바이트여야 합니다.");
                    return false;
                }

                this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
                this.ivParameterSpec = new IvParameterSpec(ivBytes);

                return true;

            } catch (IllegalArgumentException e) {
                System.err.println("Base64 디코딩 실패: 올바른 Base64 형식이 아닙니다.");
                return false;
            } catch (Exception e) {
                System.err.println("키/벡터 로드 실패: " + e.getMessage());
                return false;
            }
        }

        // 현재 키 반환
        public SecretKey getCurrentKey() {
            return secretKey;
        }

        // 현재 벡터 반환
        public IvParameterSpec getCurrentVector() {
            return ivParameterSpec;
        }

        // 키 정보 출력
        public void printkeyInfo() {
            if (secretKey == null || ivParameterSpec == null) {
                System.out.println("키와 벡터가 설정되지 않았습니다.");
                return;
            }

            String keyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            String ivBase64 = Base64.getEncoder().encodeToString(ivParameterSpec.getIV());

            System.out.println("\n=== 현재 키 정보 ===");
            System.out.println("키 (Base64): " + keyBase64);
            System.out.println("벡터 (Base64): " + ivBase64);
            System.out.println("키 길이: " + secretKey.getEncoded().length * 8 + " bits");
            System.out.println("벡터 길이: " + ivParameterSpec.getIV().length + " bytes");
        }
    }
}
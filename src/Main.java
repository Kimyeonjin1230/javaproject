import java.util.Optional;
import java.util.Scanner;


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static InDecoding processor = new InDecoding();

    public static void main(String[] args) {
        System.out.println("***AES 암호화/복호화 프로그램***");

        processor.getkeyManager().printkeyInfo(); //초기 키 정보 출력

        while(true) {
            System.out.println("\n===메뉴===");
            System.out.println("1. plaintext 암호화");
            System.out.println("2. plaintext 복호화");
            System.out.println("3. 키 생성");
            System.out.println("4. 키 입력 : ");
            System.out.println("5. 암호화 테스트 : ");
            System.out.println("6. 종료");
            System.out.print(" 번호 선택 : ");

            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice){
                case 1:
                    encryptText();
                    break;
                case 2:
                    decryptText();
                    break;
                case 3:
                    generateKey();
                    break;
                case 4:
                    InputKey();
                    break;
                case 5:
                    testCrypto();
                    break;
                case 6 :
                    System.out.println("\n프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }
    private  static void encryptText() {
        System.out.print("\n암호화할 평문 입력 : ");
        String plainText = scanner.nextLine();

        Optional<String> result = processor.encrypt(plainText); //평문을 암호화한것 string 리스트화
        if (result.isPresent()) {
            System.out.println("암호화 성공. 원본 : " + plainText + " => ");
            System.out.println(result.get());
        } else {
            System.out.println("암호화 실패");
        }
    }
    private static void decryptText() {
        System.out.print("\n복호화할 암호문 입력 : ");
        String encryptedText = scanner.nextLine();

        Optional<String> result = processor.decrypt(encryptedText); //암호문 복호화한 결과
        if (result.isPresent()) {
            System.out.println("복호화 성공. 암호문 : " + encryptedText + " => ");
            System.out.println(result.get());
        } else {
            System.out.println("복호화 실패");
        }
    }
    private static void generateKey(){
            processor.getkeyManager().generateKeyVector();
            System.out.println("\n키와 벡터가 생성되었습니다.");
            processor.getkeyManager().printkeyInfo();

    }

    private static void InputKey() {
        System.out.println("\n사용자 키 입력 : ");
        System.out.print("키(Base64) : ");
        String key = scanner.nextLine();
        System.out.print("벡터 (Base64) : ");
        String vector = scanner.nextLine();


        boolean success = processor.getkeyManager().loadFromString(key, vector);
        if(success) {
            System.out.println("키/벡터 입력 성공");
        }else {
            System.out.println("키/벡터 입력 실패");
        }
    }
    private static void testCrypto() {
        String[] testTexts = {
                "자바프로그래밍 기말프젝",
                "java final project",
                "123456789",
                "!@#$%^&*<>[]{};:?"
        };
        System.out.println("\n=== 테스트 시작 ===");
        for(int i =0; i < testTexts.length; i++) {
            String text = testTexts[i];
            boolean result = processor.testEncrypt(text);
        }
        System.out.println("===테스트 완료===");
    }
}
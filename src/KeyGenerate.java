class KeyGenerate {
    private SecKey currentKey;
    private Vector currentVector;

    // 생성자
    public KeyGenerate() {
        generateNewKey();
        generateNewVector();
    }

    public KeyGenerate(SecKey key, Vector vector) {
        this.currentKey = key;
        this.currentVector = vector;
    }

    // 새로운 키와 벡터 생성
    public void generateNewKey() {
        this.currentKey = RandomKey.generateKey();
    }

    public void generateNewVector() {
        this.currentVector = new Vector();
    }

    public void generateKeyVector() {
        generateNewKey();
        generateNewVector();
    }

    // Getter/Setter
    public SecKey getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(SecKey key) {
        this.currentKey = key;
    }

    public Vector getCurrentVector() {
        return currentVector;
    }

    public void setCurrentVector(Vector vector) {
        this.currentVector = vector;
    }

    // 키와 벡터 정보 출력
    public void printkeyInfo() {
        System.out.println("=== 키 정보 ===");
        System.out.println("키: " + currentKey.toBase64());
        System.out.println("벡터: " + currentVector.toBase64());
        System.out.println("키 유효성: " + currentKey.isValid());
    }

    // 키와 벡터를 파일에서 불러오기 (시뮬레이션)
    public boolean loadFromString(String keyStr, String vectorStr) {
        try {
            this.currentKey = new SecKey(keyStr);
            this.currentVector = Vector.fromBase64(vectorStr);
            return true;
        } catch (Exception e) {
            System.err.println("키/벡터 로드 실패: " + e.getMessage());
            return false;
        }
    }
}
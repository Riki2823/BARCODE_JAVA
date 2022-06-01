public class main {
    public static void main(String[] args) {
        Code11 mycode = new Code11();
        String barcode = Code11.encode("*01234*");
        String code = Code11.decode(barcode);
        System.out.println(code);
    }
}

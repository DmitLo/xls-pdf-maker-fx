package payment;

/**
 * зашифрованный массив по XOR
 */

public class XorEncoder {
        public static void main(String[] args) {
            // 1. Вставьте сюда ваш реальный секретный ключ от WEBPAY
            String realSecretKey = "mzIOhdhgjd652467hgfswjhGHDF";

            // 2. Выберите любое число-маску от 1 до 255
            byte mask = 0x5A;

            byte[] textBytes = realSecretKey.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            byte[] encrypted = new byte[textBytes.length];

            // Шифруем каждый байт
            for (int i = 0; i < textBytes.length; i++) {
                encrypted[i] = (byte) (textBytes[i] ^ mask);
            }

            // Выводим результат в консоль, чтобы скопировать в основную программу
            System.out.print("Зашифрованный массив для вставки: ");
            System.out.print("new byte[]{ ");
            for (int i = 0; i < encrypted.length; i++) {
                System.out.print(encrypted[i]);
                if (i < encrypted.length - 1) System.out.print(", ");
            }
            System.out.println(" };");
            System.out.println("Использованная маска (Hex): 0x" + Integer.toHexString(mask).toUpperCase());
        }

}

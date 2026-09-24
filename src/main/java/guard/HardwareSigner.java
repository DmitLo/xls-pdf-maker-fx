package guard;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Получение уникального кода машины
 */

public class HardwareSigner {

    public static String getHWID() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hardware = systemInfo.getHardware();

            // 1. Получаем UUID материнской платы / системы
            ComputerSystem computerSystem = hardware.getComputerSystem();
            String baseboardUuid = computerSystem.getHardwareUUID(); // Уникальный UUID

            // 2. Получаем ID процессора (включая его семейство, модель и т.д.)
            CentralProcessor processor = hardware.getProcessor();
            String processorId = processor.getProcessorIdentifier().getProcessorID();

            // Соединяем параметры в одну уникальную строку
            String rawId = baseboardUuid + "|" + processorId;

            // 3. Хешируем строку в SHA-256 для получения красивого ID
            return bytesToHex(sha256(rawId));

        } catch (Exception e) {
            // Фолбэк на случай непредвиденной ошибки доступа к железу
            return "UNKNOWN_HWID_" + System.getProperty("user.name");
        }
    }

    private static byte[] sha256(String base) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(base.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
}

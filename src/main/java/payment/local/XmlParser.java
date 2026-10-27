package payment.local;

import guard.HardwareSigner;
import guard.LicenseGeneratorIn;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class XmlParser {

    /**
     * Метод извлекает значение тега <status> из XML-ответа WebPay
     */
    public static String getStatusFromXml(String xmlData) {
        try {
            // Создаем фабрику и билдер для парсинга XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Превращаем строку в поток байт и парсим в Document
            ByteArrayInputStream input = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));
            Document doc = builder.parse(input);

            // Нормализуем структуру XML (убирает лишние пробелы и переносы строк)
            doc.getDocumentElement().normalize();

            // Ищем все теги с именем "status"
            NodeList nodeList = doc.getElementsByTagName("status");

            // Если такой тег найден, возвращаем его текстовое содержимое
            if (nodeList.getLength() > 0) {
                //получение HASH машины
                String hash = guard.HardwareSigner.getHWID();
                //генерация ключа для локальной программы и сохранение в файле
                LicenseGeneratorIn.mainStart(hash);
                return nodeList.item(0).getTextContent().trim();
            }

        } catch (Exception e) {
            System.err.println("Ошибка при парсинге XML: " + e.getMessage());
            e.printStackTrace();
        }

        return "unknown"; // Возвращаем, если тег не найден или произошла ошибка
    }

    // Пример использования
    public static void main(String[] args) {
        String xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<wsb_api_response>\n" +
                "\t<version>1</version>\n" +
                "\t<command>get_transaction</command>\n" +
                "\t<status>success</status>\n" +
                "\t<fields>\n" +
                "\t\t<transaction_id>510901506</transaction_id>\n" +
                "\t\t<amount>20.5</amount>\n" +
                "\t</fields>\n" +
                "</wsb_api_response>";

        // Вызываем метод
        String status = getStatusFromXml(xmlResponse);

        System.out.println("Извлеченный статус: " + status);
        // Выведет: Извлеченный статус: success

        if ("success".equals(status)) {
            System.out.println("Ура! Оплата подтверждена платежной системой.");
        }
    }
}

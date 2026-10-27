package payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import payment.dto.ReceiveFromServToApp;
import payment.dto.SendFromAppToServ;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static guard.GuiCheck.getAndCreateSaveDirectory;

public class PaymentManagerServ {

//    private final String serverUrl = "https://your-node-server.com"; // Ваш Express сервер
private final String serverUrl = "http://127.0.0.1:3000"; // Ваш Express сервер
    private ScheduledExecutorService scheduler;

    // Вызывать этот метод сразу после того, как вы открыли браузер пользователю для оплаты
    public void startPollingPaymentStatus(String orderNum) {
        // Создаем фоновый однопоточный таймер
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Запускаем задачу каждые 3 секунды
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                try {
//                    System.out.println("Проверяем статус заказа " + orderNum + " на сервере Express...");
//
//                    HttpClient client = HttpClient.newHttpClient();
//                    HttpRequest request = HttpRequest.newBuilder()
//                            .uri(URI.create(serverUrl + "/payment/check-status?order_num=" + orderNum))
//                            .GET()
//                            .build();
//
//                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//                    if (response.statusCode() == 200) {
//                        String body = response.body();
//                        System.out.println("Ответ сервера: " + body);
//
//                        // Простейший разбор JSON ответа без сторонних библиотек
//                        if (body.contains("\"status\":\"success\"")) {
//                            System.out.println("ПЛАТЕЖ УСПЕШЕН!");
//
//                            // Возвращаемся в поток Swing для обновления UI
//                            SwingUtilities.invokeLater(() -> {
//                                // КЛИЕНТ ОПЛАТИЛ: Разблокируйте функции вашего Swing UI здесь
//                                System.out.println("Интерфейс Swing обновлен!");
//                            });
//
//                            stopPolling(); // Останавливаем таймер
//                        } else if (body.contains("\"status\":\"failed\"")) {
//                            System.out.println("Платеж отклонен.");
//                            stopPolling();
//                        }
//                        // Если статус "pending", ничего не делаем, ждем следующего тика таймера
//                    }
//
//                } catch (Exception e) {
//                    System.err.println("Ошибка при опросе сервера Express: " + e.getMessage());
//                }

                System.out.println("Проверяем статус заказа " + orderNum + " на сервере Express...");

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/payment/check-status?order_num=" + orderNum))
                        .GET()
                        .build();

                try {
                    // Отправляем запрос и получаем ответ в виде строки
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        String jsonBody = response.body();
                        System.out.println("Ответ от сервера Node.js: " + jsonBody);

                        // Пример ручного разбора, если не хочется подключать Jackson/Gson:
                        // (Для сложных JSON рекомендуется использовать библиотеку org.json или Jackson)
                        System.out.println("Данные успешно получены, можно обновлять Swing UI!");
                        //остановка запросов к серверу оплаты
                        stopPolling();

                        /////
                        ObjectMapper mapper = new ObjectMapper();
                        ReceiveFromServToApp responseLicense = mapper.readValue(jsonBody, ReceiveFromServToApp.class);

                        /////
                        //получение пути файла
                        //получение или создание папки
                        File folder = getAndCreateSaveDirectory();
                        System.out.println("Безопасный путь для сохранения настроек: " + folder.getAbsolutePath());
                        //String mainPath = folder.getAbsolutePath();

                        //Path path = CreateUserFolder.create();
                        String fileNameResult = "license.lic";
                        Path fileOutPath = folder.toPath().resolve(fileNameResult);
                        System.out.println("license.lic " + fileOutPath);

                        // Записываем зашифрованную подпись в файл license.lic
                        try (FileWriter writer = new FileWriter(fileOutPath.toFile())) {
                            writer.write(responseLicense.getLicense());
                        }
                        System.out.println("Файл license.lic успешно создан для HWID: " + responseLicense.getHwid());





                    } else {
                        System.out.println("Ошибка сервера. Код ответа: " + response.statusCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    // Принудительная остановка таймера (например, если пользователь закрыл окно оплаты)
    public void stopPolling() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Опрос статуса платежа остановлен.");
        }
    }
}



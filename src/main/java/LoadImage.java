import java.io.FileInputStream;
import java.io.IOException;

/**
 * Загрузка изображения
 */
public class LoadImage {
    public static byte[] loadImage(String imagePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(imagePath);
        byte[] imageBytes = inputStream.readAllBytes();
        inputStream.close();
        return imageBytes;
    }
}

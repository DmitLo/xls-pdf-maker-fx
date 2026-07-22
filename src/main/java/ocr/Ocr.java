package ocr;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.debugger.PDFDebugger;


import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class Ocr {

    //public static void osr(String fileNameResult, List<String> strings, JTextArea textArea) {
    public static String osr(List<String> strings) throws IOException {

        String os = System.getProperty("os.name").toLowerCase();
        String tessPath = getTessdataPath(os);
//        System.setProperty("jna.library.path", "/opt/local/lib/");
        if (!os.contains("win")) {
            System.setProperty("jna.library.path", tessPath);
            //System.setProperty("tess4j.library.path", "/opt/local/bin/");
        }

        String result = "";
        boolean dpfFlag = false;

        // Укажите путь к изображению для распознавания
        // File imageFile = new File("./test.tif");
        File imageFile = new File(String.valueOf(Path.of(strings.get(0))));
        String mimeType = Files.probeContentType(Path.of(strings.get(0))).
                replace("application/", "").replace("image/", "");
        System.out.println("mime = " + mimeType);

        Tesseract tesseract = new Tesseract();

        tesseract.setPageSegMode(6);
        // Укажите путь к папке "tessdata" (нужна для распознавания конкретного языка).
        // Скачайте файлы языков (например, rus.traineddata) с официального репозитория
        // https://github.com
        tesseract.setDatapath("./");
        //tesseract.setDatapath("/pt/local/share/tessdata/");

        // Установка языка распознавания
        tesseract.setLanguage("rus");


        //Распознование docx
//        if ((mimeType.substring(0, 3)).equals("vnd")) {
//
//            try (FileInputStream fis = new FileInputStream(imageFile);
//                 FileOutputStream fos = new FileOutputStream(new File("temp.pdf"))) {
//
//                // Чтение docx файла
//                XWPFDocument document = new XWPFDocument(fis);
//
//                // Создание настроек конвертации
//                PdfOptions options = PdfOptions.create();
//
//                // Конвертация
//                PdfConverter.getInstance().convert(document, fos, options);
//
//                System.out.println("PDF успешно создан!");
//                dpfFlag = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//        if (dpfFlag) {
//            imageFile = new File("temp.pdf");
//        }

        if (mimeType.equals("msword")) {
            FileInputStream fis = new FileInputStream(imageFile);
            HWPFDocument document = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(document);

            String[] paragraphs = extractor.getParagraphText();
            StringBuilder sb = new StringBuilder();

            for (String paragraph : paragraphs) {
                if (paragraph != null && !paragraph.trim().isEmpty()) {
                    sb.append(paragraph.trim()).append("\n"); // Добавление разделителя строк
                }
            }
//            String finalString = sb.toString(); // При необходимости получить String
//
//            StringBuilder outText = extractor;
//
//            PrintWriter out = new PrintWriter("output.txt")
//
//            String[] paragraphs = extractor.getParagraphText();
//
//            for (String paragraph : paragraphs) {
//                out.println(paragraph);
//            }
            System.out.println("Текст успешно извлечен!");
            return  sb.toString();
        }

            //Распознование pdf
            if (mimeType.equals("pdf")) {
                try (PDDocument document = PDDocument.load(imageFile)) {
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    StringBuilder fullText = new StringBuilder();

                    for (int page = 0; page < document.getNumberOfPages(); page++) {
                        BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.GRAY);
                        String pageText = tesseract.doOCR(bim);
                        fullText.append(pageText).append("\n");
                    }
                    result = fullText.toString();
                    //System.out.println("strings = " + fullText);
                    //result = tesseract.doOCR(imageFile);

                } catch (Exception e) {
                    e.printStackTrace();
                    //dpfFlag = false;
                }
            }


            if (mimeType.equals("tiff") || mimeType.equals("jpg") || mimeType.equals("jpeg") || mimeType.equals("png")) {
                try {
                    // Запуск процесса распознавания
                    result = tesseract.doOCR(imageFile);
                    System.out.println("Распознанный текст:\n");
                    System.out.println(result);
                } catch (TesseractException e) {
                    System.err.println("Ошибка распознавания: " + e.getMessage());
                }
            }

            return result;
        }

        private static String getTessdataPath (String os){
            System.out.println("Текущая операционная система: " + os);

            if (os.contains("win")) {
                // Путь для Windows по умолчанию
                return "C:\\Tesseract-OCR\\";
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                // Путь для Linux при установке через apt/yum (обычно /usr/share/tesseract-ocr/4.00/tessdata или аналогичный)
                return "/usr/share/tesseract-ocr/";
            } else if (os.contains("mac")) {
                // Путь для macOS при установке через Homebrew
                return "/opt/local/lib/";
                // Примечание: на новых Mac с процессорами Apple Silicon (M1/M2/M3)
                // Homebrew ставит файлы в: "/opt/homebrew/share/tessdata"
            } else {
                throw new RuntimeException("Неподдерживаемая операционная система: " + os);
            }
        }
    }


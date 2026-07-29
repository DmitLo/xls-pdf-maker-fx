package pdf;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Вставка листов в документ PDF
 */

public class PdfUnion {

    public static void pdfPage (String fileNameResult, List<String> strings) throws IOException {

        PDFMergerUtility PdfMerger = new PDFMergerUtility();

        //базовый файл должен быть без пути
        if (fileNameResult.isEmpty()) {
            fileNameResult = "./unionPdf.pdf";
        }
        String filePath = "./";

        // файл назначения
        PdfMerger.setDestinationFileName(fileNameResult + ".pdf");

        // исходные файлы
        //int length = strings.size();
        int lengthStart = strings.size();
        int length = 0;
        //удаление пустых ячеек
        for (String string : strings) {
            if (string.isEmpty()) {
                length = lengthStart - 1;
                break;
            }
        }



        for (int i = 0; i < length; i++) {
            File file = new File(strings.get(i));
            PdfMerger.addSource(file);
        }

        //объединение файлов
        PdfMerger.mergeDocuments();
        System.out.println("Documents merged");

    }
}

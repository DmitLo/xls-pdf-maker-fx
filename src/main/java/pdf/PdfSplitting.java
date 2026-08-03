package pdf;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import utils.CreateUserFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

/**
 * Разбиение на листы PDF
 */
public class PdfSplitting {

    public static void pdfSomePages(String fileNameResult, List<String> strings) throws IOException {

        // загрузка исходного документа
        File file = new File(strings.get(0));
        PDDocument document = PDDocument.load(file);

        // класс для разделения документов
        Splitter splitter = new Splitter();

        // список документов из файла pdf
        List<PDDocument> Pages = splitter.split(document);

        // получение отдельных документов списком
        Iterator<PDDocument> iterator = Pages.listIterator();

        //получение или создание папки
        Path path = CreateUserFolder.create();

        //Saving each page as an individual document
        int i = 1;
        while(iterator.hasNext()) {
            PDDocument pd = iterator.next();
            Path fileOutPath = path.resolve(fileNameResult);
//            pd.save(fileNameResult + i++ +".pdf");
            pd.save(fileOutPath.toString() + i++ +".pdf");
        }
        System.out.println("Multiple PDF’s created");
        // закрытие исходного документа
        document.close();

    }
}

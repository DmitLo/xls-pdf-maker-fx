import com.aspose.cells.FileFormatType;
import com.aspose.cells.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Вставка листов в документ XLS
 */

public class ExcelUnionSheet {

    public static void sheet (String fileNameResult, List<String> strings) throws Exception {

        //базовый файл должен быть без пути
        if (fileNameResult.isEmpty()) {
            fileNameResult = "./union.xls";
        }
        String filePath = "./";
        //String fileName = filePath + "3" + fileName1;
        String fileName = filePath + "unionNew.xls";

        //создание пустой книги
        XSSFWorkbook wb1 = new XSSFWorkbook();
        try
        {
            XSSFSheet sheet1 = wb1.createSheet("Лист1");
            FileOutputStream fileOut = new FileOutputStream(fileNameResult);
            wb1.write(fileOut);
            fileOut.close();
        }
        catch(Exception ex)
        {
            System.out.println("XLSX Generated Error...");
        }


        // создание файла с тремя вкладками
        // org.apache.poi.ss.usermodel.Workbook
        //org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath + fileName1));
        org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(new FileInputStream(fileNameResult));

        //создание листов в fileNameResult
        int length = strings.size();
        for (int i = 1; i < length; i++) {
            workbook.createSheet("Лист" + (i + 1) );
        }

        //запись в файл
        FileOutputStream out = new FileOutputStream(fileNameResult);
        workbook.write(out);
        out.close();
        workbook.close();

        // Load the destination Excel workbook
        Workbook destination = new Workbook(fileNameResult);

        for (int i = 0; i < length; i++) {
                Workbook source = new Workbook(strings.get(i));
                // Copy the first sheet of the source workbook into second workbook
                destination.getWorksheets().get(i).copy(source.getWorksheets().get(0));
            }

        // Save the file.
        destination.save(fileNameResult, FileFormatType.EXCEL_97_TO_2003);
    }
}

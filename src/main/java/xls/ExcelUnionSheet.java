package xls;

import com.aspose.cells.FileFormatType;
import com.aspose.cells.Workbook;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
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
        FileOutputStream out = new FileOutputStream(fileNameResult + ".xls");
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

    /**
     * Вставка изображения в ячейку
     */
    public static class PasteStamp {

        public static void pasteStamp(HSSFWorkbook sourceWb, byte[] imageBytes, HSSFSheet sourceSheet,
                                      int column1, int row1, int column2, int row2) {
            //вставка картинки
            int pictureIndex = sourceWb.addPicture(imageBytes, XSSFWorkbook.PICTURE_TYPE_PNG);
            ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,
                    (short) column1, row1, (short) column2, row2);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE); //изменение размера при изменении ячейки
            sourceSheet.createDrawingPatriarch().createPicture(anchor, pictureIndex);
        }
    }
}

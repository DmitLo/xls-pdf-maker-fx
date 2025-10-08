import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Создание листа оборудовани
 */
public class WriteIntoExcel {
    public static void writeIntoExcel(String file, List<Read> read, boolean materialsEquipment) throws FileNotFoundException, IOException {
        Workbook book = new HSSFWorkbook();
        Row row;
        Cell cell;
        int length = 0;
        //создание листа
        Sheet sheet = book.createSheet("Оборудование");

        row = sheet.createRow(0);
        cell = row.createCell(5);
        cell.setCellValue("Утверждаю");
        row = sheet.createRow(1);
        cell = row.createCell(3);
        cell.setCellValue("");
        row = sheet.createRow(2);
        cell = row.createCell(3);
        cell.setCellValue("");
        row = sheet.createRow(3);
        cell = row.createCell(3);
        cell.setCellValue("");

        row = sheet.createRow(4);
        cell = row.createCell(2);
        // по умолчанию оборудованине
        cell.setCellValue("ОБОРУДОВАНИЕ");
        // для материалов
        if (materialsEquipment) {
            cell.setCellValue("МАТЕРИАЛЫ");
        }
        row = sheet.createRow(5);
        cell = row.createCell(2);
        cell.setCellValue("");

        row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("№ п/п");
        cell = row.createCell(1);
        cell.setCellValue("Код");
        cell = row.createCell(2);
        cell.setCellValue("Наименование");
        cell = row.createCell(3);
        cell.setCellValue("Ед. изм.");
        cell = row.createCell(4);
        cell.setCellValue("Кол-во");
        cell = row.createCell(5);
        cell.setCellValue("Сумма ед., руб.");
        cell = row.createCell(6);
        cell.setCellValue("Сумма общ., руб.");


        // определение глубины списка (количество строу)
        for (Read k : read) {
            String commaSeparated = k.toString();
            List<String> items = Arrays.asList(commaSeparated.split("\\s*,\\s*"));
            length = items.size();
        }

        // заполнение таблицы
        for (int i = 0; i < length; i++) {
            row = sheet.createRow(i + 7);
            int j = 0;
            for (Read k : read) {
                cell = row.createCell(j);
                cell.setCellValue(k.rowTable().get(i));
                j++;
            }
        }

        row = sheet.createRow(length + 8);
        cell = row.createCell(2);
        cell.setCellValue("Составил");

        // Меняем размер столбца
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);

        // Записываем всё в файл
        book.write(new FileOutputStream(file));
        book.close();
    }
}

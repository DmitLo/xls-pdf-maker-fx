import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Чтение данных из файла и весь цикл определения координат вставки ячейки
 */

public class ReadFromExcelInsertStamp {
    public static void readFromExcelStamp(String fileInput, String fileOutput) throws IOException {

        String leadName = "Директор ОДО \"РеТехГрупп\"";

        String imagePathLead = "./lead.png";
        String imagePathStamp = "./stamp.png";

        //чтение листа
        try (FileInputStream fis = new FileInputStream(fileInput);
             HSSFWorkbook sourceWb = new HSSFWorkbook(fis)) {

            //количество листов в документе
            int numberOfSheets = sourceWb.getNumberOfSheets();

            //загрузка изображения м создание битового массива
            byte[] imageBytesLead = LoadImage.loadImage(imagePathLead);
            byte[] imageBytesStamp = LoadImage.loadImage(imagePathStamp);

            //проход по листам документа
            for (int i = 0; i < numberOfSheets; i++) {
                //HSSFSheet sourceSheet = sourceWb.getSheet("Лист" + (i + 1)); // Получаем исходный лист
                HSSFSheet sourceSheet = sourceWb.getSheetAt(i); // Получаем исходный лист

                //количество заполненных строк и столбцов в документе
                Sheet sheet = sourceWb.getSheetAt(i); // Получаем первый лист

                // Подсчет строк
                int lastRowNum = sheet.getLastRowNum(); // Получаем индекс последней строки (с данными)
                int totalRowsWithData = lastRowNum + 1; // Общее количество строк с данными

                // Подсчет столбцов
                // Чтобы найти последний заполненный столбец, нужно найти максимальное значение getLastCellNum() среди всех строк.
                short maxColNum = 0;
                for (int j = 0; j <= lastRowNum; j++) {
                    if (sheet.getRow(j) != null) {
                        if (sheet.getRow(j).getLastCellNum() > maxColNum) {
                            maxColNum = sheet.getRow(j).getLastCellNum();
                        }
                    }
                }
                int totalColumnsWithData = maxColNum; // Количество столбцов с данными

                //количество строк и столбцов в документе
                System.out.println("Количество строк и столбцов в документе: " + i + " - " + totalRowsWithData + " " + totalColumnsWithData);

                Sheet sheetRead = sourceWb.getSheetAt(i); // Получаем первый лист (индексация с 0)
                //проход по строкам
                for (int r = 0; r < totalRowsWithData; r++) {
                    //проход по колонкам
                    for (int k = 0; k < totalColumnsWithData; k++) {

                        Row row = sheetRead.getRow(r); // Получаем первую строку (индексация с 0)
                        Cell cell = row.getCell(k); // Получаем первую ячейку в первой строке (A1)

                        String cellValue;
                        if (cell != null) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                // Преобразуем числовое значение в строку
                                cellValue = String.valueOf(cell.getNumericCellValue());
                            } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
                                cellValue = cell.getCellFormula();
                            } else {
                                // Получаем строковое значение ячейки
                                cellValue = cell.getStringCellValue();
                            }

                            if (cellValue.toLowerCase().contains(leadName.toLowerCase())) {
                                System.out.println("column: " + k + "  row: " + r);
                                PasteStamp.pasteStamp(sourceWb, imageBytesStamp, sourceSheet,
                                        k, r, k + 2, r + 5);
                                PasteImage.pasteImage(sourceWb, imageBytesLead, sourceSheet,
                                        k, r, k + 3, r + 3);

                           }
                        }
                    }
                }
            }
            //сохранение в файл
            FileOutputStream out = new FileOutputStream(fileOutput);
            sourceWb.write(out);
            out.close();
        }
    }
}

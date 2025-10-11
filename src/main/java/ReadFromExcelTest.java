import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
//для формата xlsx
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Чтение данных из файла
 */

public class ReadFromExcelTest {
    // public static List<Read> readFromExcelTest(String file) throws IOException {
    public static void readFromExcelTest(String fileInput, String fileOutput) throws IOException {

        String leadName = "Руководитель";
        String engineerName = "Главный";
        String madeName = "Составил";
        String checkName = "Проверил";
        String noName = "/  /";

        String imagePathLead = "П13.png";
        String imagePathEngineer = "П13.png";
        String imagePathMade = "П13.png";
        String imagePathCheck = "П13.png";

        //внешний список для теста
        //List<List<Object>> listOfLists = new ArrayList<>();
        //внутренний список
        //List<Object> mixedList = new ArrayList<>();

        //чтение листа
        try (FileInputStream fis = new FileInputStream(fileInput);
             HSSFWorkbook sourceWb = new HSSFWorkbook(fis)) {

            //количество листов в документе
            int numberOfSheets = sourceWb.getNumberOfSheets();

            //загрузка изображения м создание битового массива
            byte[] imageBytesLead = LoadImage.loadImage(imagePathLead);
            byte[] imageBytesEngineer = LoadImage.loadImage(imagePathEngineer);
            byte[] imageBytesMade = LoadImage.loadImage(imagePathMade);
            byte[] imageBytesCheck = LoadImage.loadImage(imagePathCheck);

            //проход по листам документа
            for (int i = 0; i < numberOfSheets; i++) {
                HSSFSheet sourceSheet = sourceWb.getSheet("Лист" + (i + 1)); // Получаем исходный лист

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

                        if (cell != null) {
                            // Получаем строковое значение ячейки
                            String cellValue = cell.getStringCellValue();
                            //System.out.println("Значение ячейки: " + cell + " " + cellValue);

                            if (cellValue.toLowerCase().contains(leadName.toLowerCase())) {
                                //mixedList.clear();
                                //mixedList.add(cellValue);
                                PasteImage.pasteImage(sourceWb, imageBytesLead, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                            } else if (cellValue.toLowerCase().contains(engineerName.toLowerCase())) {
                                PasteImage.pasteImage(sourceWb, imageBytesEngineer, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                            } else if (cellValue.toLowerCase().contains(madeName.toLowerCase())) {
                                PasteImage.pasteImage(sourceWb, imageBytesMade, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                            } else if (cellValue.toLowerCase().contains(checkName.toLowerCase())) {
                                PasteImage.pasteImage(sourceWb, imageBytesCheck, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                            }
                        }
                    }
                }
                //System.out.println(listOfLists.get(2).get(0));
                //System.out.println("Количество записей cуб: " + listOfLists.size() + " количество записей в суб: " + mixedList.size());
            }
            //сохранение в файл
            FileOutputStream out = new FileOutputStream(fileOutput);
            sourceWb.write(out);
            out.close();
        }
    }
}

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
//для формата xlsx

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Чтение данных из файла и весь цикл определения координат вставки ячейки
 */

public class ReadFromExcelInsertImage {
    // public static List<Read> readFromExcelImage(String file) throws IOException {
    public static void readFromExcelImage(String fileInput, String fileOutput) throws IOException {

        String leadName = "Руководитель организации";
        String engineerName = "Главный";
        String engineerOfficeName = "подразделения";
        String madeName = "Составил";
        String checkName = "Проверил";
        String noName = "/  /";

        String imagePathLead = "./lead.png";
        String imagePathEngineer = "./engineer.png";
        String imagePathEngineerOffice = "./engineer.png";
        String imagePathMade = "./lead.png";
        String imagePathCheck = "./lead.png";

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
            byte[] imageBytesEngineerOffice = LoadImage.loadImage(imagePathEngineerOffice);
            byte[] imageBytesMade = LoadImage.loadImage(imagePathMade);
            byte[] imageBytesCheck = LoadImage.loadImage(imagePathCheck);

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
                            } else {
                                // Получаем строковое значение ячейки
                                cellValue = cell.getStringCellValue();
                            }

                            if (cellValue.toLowerCase().contains(leadName.toLowerCase())) {
                                //mixedList.clear();
                                //mixedList.add(cellValue);
                                System.out.println("column: " + k + "  row: " + r);
                                PasteImage.pasteImage(sourceWb, imageBytesLead, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                                CellStyleLeft.cellSlyleLeft(sourceWb, cell);

                            } else if (cellValue.toLowerCase().contains(engineerName.toLowerCase())) {
                                System.out.println("column: " + k + "  row: " + r);
                                PasteImage.pasteImage(sourceWb, imageBytesEngineer, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                                CellStyleLeft.cellSlyleLeft(sourceWb, cell);
                            } else if (cellValue.toLowerCase().contains(engineerOfficeName.toLowerCase())) {
                                System.out.println("column: " + k + "  row: " + r);
                                PasteImage.pasteImage(sourceWb, imageBytesEngineerOffice, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                                CellStyleLeft.cellSlyleLeft(sourceWb, cell);
                            } else if (cellValue.toLowerCase().contains(madeName.toLowerCase())) {
                                System.out.println("column: " + k + "  row: " + r);
                                PasteImage.pasteImage(sourceWb, imageBytesMade, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                                CellStyleLeft.cellSlyleLeft(sourceWb, cell);
                            } else if (cellValue.toLowerCase().contains(checkName.toLowerCase())) {
                                System.out.println("column: " + k + "  row: " + r);
                                PasteImage.pasteImage(sourceWb, imageBytesCheck, sourceSheet,
                                        k, r - 1, k + 1, r + 1);
                                CellStyleLeft.cellSlyleLeft(sourceWb, cell);
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

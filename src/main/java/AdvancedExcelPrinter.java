import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.swing.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Для печати в xls не доделано
 */

public class AdvancedExcelPrinter {

    private static HSSFWorkbook workbook;
    private static int currentY = 0;
    private static List<List<Print>> listPrint = new ArrayList<>();


    public static List<List<Print>> initTextLines(JTextArea textArea) {

//        file = "./result.xls";

        //получение списка файлов
        List<String> strings = textArea.getText().lines().collect(Collectors.toList());
        String file = strings.get(0);


        try {
            FileInputStream fis = new FileInputStream(file);
            workbook = new HSSFWorkbook(fis);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error preparing Excel for printing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        //Sheet sheet = workbook.getSheetAt(pageIndex); // Получаем первый лист
        Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист

        // Подсчет строк
        int lastRowNum = sheet.getLastRowNum(); // Получаем индекс последней строки (с данными)
        int totalRowsWithData = lastRowNum + 1; // Общее количество строк с данными!!!

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
        int totalColumnsWithData = maxColNum; // Количество столбцов с данными!!!
        //System.out.println(" last " + totalRowsWithData);
        //int totalColumnsWithData = lastRowNum - 2;

        float deltaY = 0; // дельта rows
        //проход по строкам
        // rows
        int rowStart = 0;
        int test = 0;
        for (int r = rowStart; r < totalRowsWithData - 2; r++) {
            test ++;

            List<Print> columnPrint = new ArrayList<>();

            //System.out.println("row = " + r);
            int currentX = 0; // column
            float deltaX; // дельта column
            Row row = sheet.getRow(r); // Получаем первую строку (индексация с 0)
            short rowHeight = row.getHeight(); // высота столбца
            //проход по колонкам
            for (int k = 0; k < totalColumnsWithData; k++) {
//                columnPrint = new ArrayList<>();

                deltaX = sheet.getColumnWidthInPixels(k); // sourceSheet?
                deltaY = sheet.getDefaultRowHeightInPoints(); // sourceSheet?

                Cell cell = row.getCell(k); // Получаем первую ячейку в первой строке (A1)
                int collWidth = sheet.getColumnWidth(k); // ширина столбца

                //приведение ячейки к типу string
                String cellValue = "";
                if (cell != null) {
                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        // Преобразуем числовое значение в строку
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    } else {
                        // Получаем строковое значение ячейки
                        cellValue = cell.getStringCellValue();
                    }
                }
                //вывод длинного текста в несколько строк
                int cut = 30;
                int lengthType = cellValue.length();
                if (lengthType >= cut) {
                    //количество строк по cut символов
                    int a = lengthType / cut;
                    //остаток строки до cut символов
                    int b = lengthType % cut;
                    int c = 0;
                    String temp = "";
                    for (int i = 0; i < a; i++) {
                        temp = cellValue.substring(c, cut + c);
                        c = c + cut;
                        Print printListTemp = new Print(temp, currentX, currentY);
                        columnPrint.add(printListTemp);
                        listPrint.add(columnPrint);
                        currentY = (int) (currentY + deltaY);
                        columnPrint = new ArrayList<>();
                    }
                    if (b > 0) {
                        temp = cellValue.substring(lengthType - b);
                        Print printListTemp = new Print(temp, currentX, currentY);
                        columnPrint.add(printListTemp);
                        listPrint.add(columnPrint);
                        columnPrint = new ArrayList<>();
                    }
                } else {
                    Print printListTemp = new Print(cellValue, currentX, currentY);
                    columnPrint.add(printListTemp);
                }
                //уменьшение ширины столбца для длинного текста
                if (k == 2) {
                    currentX = currentX + 180;
                } else {
                    currentX = (int) (currentX + deltaX);
                }
            }
            listPrint.add(columnPrint);
            currentY = (int) (currentY + deltaY);
        }
        System.out.println("test " + test);
        return listPrint;
    }
}
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;

/**
 * Для печати в xls не доделано
 */

public class AdvancedExcelPrinter2 implements Printable {

    private final HSSFWorkbook workbook;
    private int pageCount = 0;

    public AdvancedExcelPrinter2(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        workbook = new HSSFWorkbook(fis);
        workbook.close();
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
//        if (pageIndex >= workbook.getNumberOfSheets()) {
//            return NO_SUCH_PAGE;
//        }
//        if (pageIndex >= 2) {
//            return NO_SUCH_PAGE;
//        }
        int positionY = 650;

        System.out.println("pageIndex = " + pageIndex + ", pageCount = " + pageCount);
        if (pageIndex > pageCount) {
            System.out.println("--------------------------------------length = " + pageCount);
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        int fontSize = 10;
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        //pageIndex = 0;

        //HSSFSheet sourceSheet = workbook.getSheetAt(pageIndex); // Получаем исходный лист от 1 до последнего

        //количество заполненных строк и столбцов в документе
        Sheet sheet = workbook.getSheetAt(pageIndex); // Получаем первый лист

        // Подсчет строк
        int lastRowNum = sheet.getLastRowNum(); // Получаем индекс последней строки (с данными)
        int totalRowsWithData = lastRowNum + 1; // Общее количество строк с данными!

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
        int totalColumnsWithData = maxColNum; // Количество столбцов с данными!

        //количество строк и столбцов в документе
        //System.out.println("Количество строк и столбцов в документе: " + pageIndex + " - " + totalRowsWithData + " " + totalColumnsWithData);

        //Sheet sheetRead = sourceWb.getSheetAt(i); // Получаем первый лист (индексация с 0)

        Sheet sheetRead = sheet;
        int currentY = 0; // rows
        float deltaY = 0; // дельта rows
        //проход по строкам
        for (int r = 0; r < totalRowsWithData - 5; r++) {
            int currentX = 0; // column
            float deltaX; // дельта column
            Row row = sheetRead.getRow(r); // Получаем первую строку (индексация с 0)
            short rowHeight = row.getHeight(); // высота столбца
            //проход по колонкам
            for (int k = 0; k < totalColumnsWithData; k++) {

                deltaX = sheetRead.getColumnWidthInPixels(k); // sourceSheet?
                deltaY = sheetRead.getDefaultRowHeightInPoints(); // sourceSheet?
                //System.out.println("Количество пикселей в ширине столбца и точек в высоте строки: " + deltaX + " - " + deltaY);

                Cell cell = row.getCell(k); // Получаем первую ячейку в первой строке (A1)
                int collWidth = sheet.getColumnWidth(k);
                //System.out.println("Ширина столбца = " + collWidth + " высота строки " + rowHeight);

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
                //System.out.println("Номер строки: " + r + " Номер столбца: " + k + " Значение ячейки " + cellValue);

                //вывод длинного текста в несколько строк
                int cut = 30;
                int lengthType = cellValue.length();
                if (lengthType >= cut) {
                    int a = lengthType / cut;
                    int b = lengthType % cut;
                    int c = 0;
                    String temp = "";
                    for (int i = 0; i < a; i++) {
                        temp = cellValue.substring(c, cut + c);
                        //System.out.println("k = " + k + " ,a = " + a + " ,lengthType = " + lengthType + ", cellValue = " + temp);
                        c = c + cut;
                        g2d.drawString(temp, currentX, currentY);
                        currentY = (int) (currentY + deltaY);
                    }
                    if (b > 0) {
                        temp = cellValue.substring(lengthType - b);
                        //System.out.println("k = " + k + " ,a = " + a + " ,lengthType = " + lengthType + ", cellValue = " + temp);
                        g2d.drawString(temp, currentX, currentY);
                        currentY = (int) (currentY + deltaY);
                    }
                } else {
                    g2d.drawString(cellValue, currentX, currentY);
                }

                //уменьшение ширины столбца для длинного текста
                if (k == 2) {
                    currentX = currentX + 180;
                } else {
                    currentX = (int) (currentX + deltaX);
                }

                //System.out.println("Позиция Х:" + currentX);
                //System.out.println("Позийия Y:" + currentY);
            }

            if (currentY >= positionY) {
                System.out.println("currentY = " + currentY + ", positionY = " + positionY);
                currentY = 0;
                pageCount = pageCount + 1;
                return PAGE_EXISTS;
            }
            currentY = (int) (currentY + deltaY / 1);
        }



        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         */
//        int y = 0;
//        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex - 1];
//        int end = (pageIndex == pageBreaks.length)
//                ? textLines.length : pageBreaks[pageIndex];
//        for (int line = start; line < end; line++) {
//            y += lineHeight;
//            g.drawString(textLines[line], 0, y);
//        }

        //test-----
        /*************************
         * Draw directly into the Printable graphics object
         */
//        System.out.println("ok");
//        g2d.setColor(Color.black);
//        g2d.drawString("example string", 250, 250);
//        g2d.fillRect(0, 0, 200, 200);
        //------

        return NO_SUCH_PAGE;
    }

    public void initiatePrinting() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(null, "Error during printing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public static void mainPrint() {
        try {
            AdvancedExcelPrinter2 printer = new AdvancedExcelPrinter2("./result.xls");
            printer.initiatePrinting();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error preparing Excel for printing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

import com.aspose.cells.Cells;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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

public class AdvancedExcelPrinter implements Printable {

    private HSSFWorkbook workbook;

    public AdvancedExcelPrinter(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        workbook = new HSSFWorkbook(fis);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex >= workbook.getNumberOfSheets()) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        //
        HSSFSheet sourceSheet = workbook.getSheetAt(pageIndex); // Получаем исходный лист

        //количество заполненных строк и столбцов в документе
        Sheet sheet = workbook.getSheetAt(pageIndex); // Получаем первый лист

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
        System.out.println("Количество строк и столбцов в документе: " + pageIndex + " - " + totalRowsWithData + " " + totalColumnsWithData);

        //Sheet sheetRead = sourceWb.getSheetAt(i); // Получаем первый лист (индексация с 0)

        Sheet sheetRead = sheet;
        int currentX = 0;
        //проход по строкам
        for (int r = 0; r < totalRowsWithData; r++) {
            int currentY = 0;
            Row row = sheetRead.getRow(r); // Получаем первую строку (индексация с 0)
            short rowHeight = row.getHeight(); // высота столбца
            //проход по колонкам
            for (int k = 0; k < totalColumnsWithData; k++) {


                float deltaX = sourceSheet.getColumnWidthInPixels(k);
                float deltaY = sourceSheet.getDefaultRowHeightInPoints();


                Cell cell = row.getCell(k); // Получаем первую ячейку в первой строке (A1)
                int collWidth = sheet.getColumnWidth(k);

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


                g2d.fillRect(currentX, currentY, collWidth, rowHeight);
                g2d.drawString(cellValue, currentX + 100, currentY + 100);

                currentX += collWidth;
            }
            currentY += rowHeight;
        }


        return PAGE_EXISTS;
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
            AdvancedExcelPrinter printer = new AdvancedExcelPrinter("./result.xls");
            printer.initiatePrinting();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error preparing Excel for printing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

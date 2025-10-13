import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Смещение формата ячейки расположения теста слева
 */
public class CellStyleLeft {

    public static void cellSlyleLeft (HSSFWorkbook sourceWb, Cell cell) {
        // Создание или получение объекта CellStyle
        CellStyle cellStyle = sourceWb.createCellStyle();
        cellStyle.cloneStyleFrom(cell.getCellStyle()); // Клонируем существующий стиль, если он есть
        // Установка горизонтального выравнивания по левому краю
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        // Применение стиля к ячейке
        cell.setCellStyle(cellStyle);
    }
}

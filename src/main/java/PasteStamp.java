import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Вставка изображения в ячейку
 */
public class PasteStamp {

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

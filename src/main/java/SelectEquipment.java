import java.io.IOException;
import java.util.List;

/**
 * Выбор оборудования или материалов
 */

public class SelectEquipment {
    public static void select(String fileName1, String fileNameResult, boolean materialsEquipment) throws IOException {

        System.out.println("start read");
        List<Read> read = ReadFromExcel.readFromExcel(fileName1, materialsEquipment);
        System.out.println("stop read");


        System.out.println("start write");
        if (fileNameResult.isEmpty()) {
            fileNameResult = "./select.xls";
        }
        WriteIntoExcel.writeIntoExcel(fileNameResult, read, materialsEquipment);
        System.out.println("stop write");

    }



}

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Чтение данных из файла
 */

public class ReadFromExcel {
    public static List<Read> readFromExcel(String file, boolean materialsEquipment) throws IOException {

        boolean start = false;
        String nameDescription = "";
        // поле наименования
        String nameEquipment = "";
        // поле единицы измерения
        String unit = "";
        double quantity = 0;
        String quantityString = "";
        // поле цены единицы
        double priceUnit = 0d;
        String priceUnitString = "";
        // поле общей цены
        double priceGeneral = 0;
        String priceGeneralString = "";
        String materialsEquipmentString = "Оборудование"; // для materialsEquipment false
        int count = 0;

        if (materialsEquipment) {
            materialsEquipmentString = "Материалы, изделия, конструкции";
        }

        //Read read = new Read(new ArrayList<>(0));
        List<String> read1 = new ArrayList<>(0);
        List<String> read2 = new ArrayList<>(0);
        List<String> read3 = new ArrayList<>(0);
        List<String> read4 = new ArrayList<>(0);
        List<String> read5 = new ArrayList<>(0);
        List<String> read6 = new ArrayList<>(0);
        List<String> read7 = new ArrayList<>(0);

        List<Read> read = new ArrayList<>();
        Read read11 = new Read(read1);
        Read read22 = new Read(read2);
        Read read33 = new Read(read3);
        Read read44 = new Read(read4);
        Read read55 = new Read(read5);
        Read read66 = new Read(read6);
        Read read77 = new Read(read7);

        read.add(read11);
        read.add(read22);
        read.add(read33);
        read.add(read44);
        read.add(read55);
        read.add(read66);
        read.add(read77);

        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet myExcelSheet = myExcelBook.getSheet("Лист1");
        // получение строки

        for (int i = 0, j = 0; i < myExcelSheet.getLastRowNum(); i++) {
            HSSFRow row = myExcelSheet.getRow(i);

            // получение ячейкм
            try {
                String name = row.getCell(0).getStringCellValue();

                //if (name.equals("Оборудование")) {
                if (name.equals(materialsEquipmentString) & count == 0 ) {
                    start = true;
                    count++;
                } else if (name.equals("Машины и механизмы") || name.equals("Оборудование")) {
                    start = false;
                }


                if (start) {
                    //j++;
                    // если число
                    if (name.substring(0, name.length() - 1).matches("[-+]?\\d+")) {

                        j++;
                        // поле кода
                        nameDescription = row.getCell(1).getStringCellValue();
                        // поле наименования
                        nameEquipment = row.getCell(2).getStringCellValue();
                        // поле единицы измерения
                        unit = row.getCell(4).getStringCellValue();
                        // поле количества
                        quantity = row.getCell(5).getNumericCellValue();
                        quantityString = String.valueOf(quantity);
                        // поле цены единицы
                        priceUnit = row.getCell(6).getNumericCellValue();
                        priceUnitString = String.valueOf(priceUnit);
                        //String priceUnit = "1";
                        // поле общей цены
                        priceGeneral = row.getCell(8).getNumericCellValue();
                        priceGeneralString = String.valueOf(priceGeneral);

                        // запись в лист
                        read1.add(String.valueOf(j));
                        read2.add(nameDescription);
                        read3.add(nameEquipment);
                        read4.add(unit);
                        read5.add(quantityString);
                        read6.add(priceUnitString);
                        read7.add(priceGeneralString);

                        //String priceGeneral = "2";
                        System.out.println(j + " " + name + " "
                                + nameDescription + " " + nameEquipment + " " + unit + " "
                                + quantityString + " " + priceUnitString + " " + priceGeneralString);

                    }
                }
            } catch (NullPointerException | IllegalStateException | StringIndexOutOfBoundsException e) {
                i++;
            }
        }
        
        myExcelBook.close();

        //возврат списка
        return read;
    }
}

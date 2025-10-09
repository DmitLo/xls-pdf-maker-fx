import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Чтение данных из файла
 */

public class ReadFromExcelTest {
    // public static List<Read> readFromExcelTest(String file) throws IOException {
    public static void readFromExcelTest(String file) throws IOException {


        boolean start = false;
        //String nameDescription = "";
        // поле наименования
        //String nameEquipment = "";
        // поле единицы измерения
        //String unit = "";
        //double quantity = 0;
        //String quantityString = "";
        // поле цены единицы
        //double priceUnit = 0d;
        //String priceUnitString = "";
        // поле общей цены
        //double priceGeneral = 0;
        //String priceGeneralString = "";
        //String materialsEquipmentString = "Оборудование";
        String CheckName = "Проверил";

        //if (materialsEquipment) {
        //   materialsEquipmentString = "Материалы, изделия, конструкции";
        //}

//        Read read = new Read(new ArrayList<>(0));
//        List<String> read1 = new ArrayList<>(0);
//        List<String> read2 = new ArrayList<>(0);
//        List<String> read3 = new ArrayList<>(0);
//        List<String> read4 = new ArrayList<>(0);
//        List<String> read5 = new ArrayList<>(0);
//        List<String> read6 = new ArrayList<>(0);
//        List<String> read7 = new ArrayList<>(0);
//
//        List<Read> read = new ArrayList<>();
//        Read read11 = new Read(read1);
//        Read read22 = new Read(read2);
//        Read read33 = new Read(read3);
//        Read read44 = new Read(read4);
//        Read read55 = new Read(read5);
//        Read read66 = new Read(read6);
//        Read read77 = new Read(read7);
//
//        read.add(read11);
//        read.add(read22);
//        read.add(read33);
//        read.add(read44);
//        read.add(read55);
//        read.add(read66);
//        read.add(read77);
//
//        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));
//        HSSFSheet myExcelSheet = myExcelBook.getSheet("Лист1");
//        // получение строки
//
//        for (int i = 0, j = 0; i < myExcelSheet.getLastRowNum(); i++) {
//            HSSFRow row = myExcelSheet.getRow(i);
//
//            // получение ячейкм
//            try {
//                String name = row.getCell(0).getStringCellValue();
//
//                //if (name.equals("Оборудование")) {
//                if (name.equals(materialsEquipmentString)) {
//                    start = true;
//                } else if (name.equals("Машины и механизмы")) {
//                    start = false;
//                }
//                if (start) {
//                    //j++;
//                    // если число
//                    if (name.substring(0, name.length() - 1).matches("[-+]?\\d+")) {
//
//                        j++;
//                        // поле кода
//                        nameDescription = row.getCell(1).getStringCellValue();
//                        // поле наименования
//                        nameEquipment = row.getCell(2).getStringCellValue();
//                        // поле единицы измерения
//                        unit = row.getCell(4).getStringCellValue();
//                        // поле количества
//                        quantity = row.getCell(5).getNumericCellValue();
//                        quantityString = String.valueOf(quantity);
//                        // поле цены единицы
//                        priceUnit = row.getCell(6).getNumericCellValue();
//                        priceUnitString = String.valueOf(priceUnit);
//                        //String priceUnit = "1";
//                        // поле общей цены
//                        priceGeneral = row.getCell(8).getNumericCellValue();
//                        priceGeneralString = String.valueOf(priceGeneral);
//
//                        // запись в лист
//                        read1.add(String.valueOf(j));
//                        read2.add(nameDescription);
//                        read3.add(nameEquipment);
//                        read4.add(unit);
//                        read5.add(quantityString);
//                        read6.add(priceUnitString);
//                        read7.add(priceGeneralString);
//
//                        //String priceGeneral = "2";
//                        System.out.println(j + " " + name + " "
//                                + nameDescription + " " + nameEquipment + " " + unit + " "
//                                + quantityString + " " + priceUnitString + " " + priceGeneralString);
//
//                    }
//                }
//            } catch (NullPointerException | IllegalStateException | StringIndexOutOfBoundsException e) {
//                i++;
//            }
//        }
//
//        myExcelBook.close();

//        //возврат списка
//        return read;


        //--------

        //чтение листа
        try (FileInputStream fis = new FileInputStream("Об.xls");
             HSSFWorkbook sourceWb = new HSSFWorkbook(fis)) {
            // ... ваш код
            //}

            //количество листов в документе
            int numberOfSheets = sourceWb.getNumberOfSheets();

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
                System.out.println(totalRowsWithData + " " + totalColumnsWithData);

                ///

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

                ///


                //копирование и создание новой области
                //HSSFSheet sourceSheet = sourceWb.getSheet("Лист1"); // Получаем исходный лист
                //HSSFSheet newSheet = sourceWb.cloneSheet(sourceWb.getSheetIndex(sourceSheet));
                // newSheet.getSheetName(); // Устанавливаем новое имя для скопированного листа

//            //сохранение изменений
//            try (FileOutputStream fos = new FileOutputStream("object.xls")) {
//                sourceWb.write(fos);
//            }
//        }

                //----------


//        //создание книги и листа
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("Лист с рисунком");

                //загрузка изображения м создание битового массива
                String imagePath = "П13.png";
                FileInputStream inputStream = new FileInputStream(imagePath);
                byte[] imageBytes = inputStream.readAllBytes();
                inputStream.close();

                //добавление рисунка на лист
//        int pictureIndex = workbook.addPicture(imageBytes, XSSFWorkbook.PICTURE_TYPE_PNG);
//        ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 0, 0, 2, 2);
//        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE); //изменение размера при изменении ячейки
//        sheet.createDrawingPatriarch().createPicture(anchor, pictureIndex);

                int pictureIndex = sourceWb.addPicture(imageBytes, XSSFWorkbook.PICTURE_TYPE_PNG);
                ClientAnchor anchor = new HSSFClientAnchor(1020, 0, 1023, 0, (short) 2, 16, (short) 3, 18);
                //ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 18, 2);
                //ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 18, 2);
                //ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 18, 2);
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE); //изменение размера при изменении ячейки
                //newSheet.createDrawingPatriarch().createPicture(anchor, pictureIndex);
                sourceSheet.createDrawingPatriarch().createPicture(anchor, pictureIndex);

//        //сохранение в файл
//        FileOutputStream out = new FileOutputStream("output.xls");
//        workbook.write(out);
//        out.close();
//        workbook.close();

            }

            //сохранение в файл
            FileOutputStream out = new FileOutputStream("output.xls");
            sourceWb.write(out);
            out.close();
            sourceWb.close();

            //------------

        }


    }
}

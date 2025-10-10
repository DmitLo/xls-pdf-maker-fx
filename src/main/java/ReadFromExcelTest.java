import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Row;
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
        String leadName = "Руководитель";
        String engineerName = "Главный";
        String madeName = "Составил";
        String checkName = "Проверил";
        String noName = "/  /";

        //внешний список
        List<List<Object>> listOfLists = new ArrayList<>();
        //внутренний список
        List<Object> mixedList = new ArrayList<>();


        //чтение листа
        try (FileInputStream fis = new FileInputStream("Об.xls");
             HSSFWorkbook sourceWb = new HSSFWorkbook(fis)) {

            //количество листов в документе
            int numberOfSheets = sourceWb.getNumberOfSheets();

            //загрузка изображения м создание битового массива
            String imagePath = "П13.png";
            FileInputStream inputStream = new FileInputStream(imagePath);
            byte[] imageBytes = inputStream.readAllBytes();
            inputStream.close();

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
                System.out.println("Количество строк и столбцов в документе: " + totalRowsWithData + " " + totalColumnsWithData);

                Sheet sheetRead = sourceWb.getSheetAt(i); // Получаем первый лист (индексация с 0)
                //проход по строкам
                for (int r = 0; r < totalRowsWithData; r++) {
                    //проход по колонкам
                    for (int k = 0; k < totalColumnsWithData; k++) {


                        Row row = sheet.getRow(r); // Получаем первую строку (индексация с 0)
                        Cell cell = row.getCell(k); // Получаем первую ячейку в первой строке (A1)

                        if (cell != null) {
                            // Получаем строковое значение ячейки
                            String cellValue = cell.getStringCellValue();
                            //System.out.println("Значение ячейки: " + cell + " " + cellValue);

                            if (cellValue.toLowerCase().contains(leadName.toLowerCase())) {
                                mixedList.add(cellValue);
                                mixedList.add(r);
                                mixedList.add(k);
                                //mixedList.add(imageBytes);
                                //listOfLists.add()
                            } else if (cellValue.toLowerCase().contains(engineerName.toLowerCase())) {
                                mixedList.add(cellValue);
                                mixedList.add(r);
                                mixedList.add(k);
                                //mixedList.add(imageBytes);
                            } else if (cellValue.toLowerCase().contains(madeName.toLowerCase())) {
                                mixedList.add(cellValue);
                                mixedList.add(r);
                                mixedList.add(k);
                                //mixedList.add(imageBytes);
                            } else if (cellValue.toLowerCase().contains(checkName.toLowerCase())) {
                                mixedList.add(cellValue);
                                mixedList.add(r);
                                mixedList.add(k);
                                //mixedList.add(imageBytes);
                            }

                            //добавление в общий список
                            listOfLists.add(mixedList);


                        }

                    }
                }


                System.out.println(listOfLists.get(3).get(0));
                System.out.println("Количество записей cуб: " + listOfLists.size() + " количество записей в суб: " + mixedList.size());


                //вставка картинки
                int pictureIndex = sourceWb.addPicture(imageBytes, XSSFWorkbook.PICTURE_TYPE_PNG);
                ClientAnchor anchor = new HSSFClientAnchor(1020, 0, 1023, 0, (short) 2, 16, (short) 3, 18);
                //ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 18, 2);
                //ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 18, 2);
                //ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 18, 2);
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE); //изменение размера при изменении ячейки
                //newSheet.createDrawingPatriarch().createPicture(anchor, pictureIndex);
                sourceSheet.createDrawingPatriarch().createPicture(anchor, pictureIndex);


            }


            //System.out.println(listOfLists.get(235).get(0));
            //System.out.println(listOfLists.size() + " " + mixedList.size());

            //сохранение в файл
            FileOutputStream out = new FileOutputStream("output.xls");
            sourceWb.write(out);
            out.close();
            sourceWb.close();


        }


    }
}

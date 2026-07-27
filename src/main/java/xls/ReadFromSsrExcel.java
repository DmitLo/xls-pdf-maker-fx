package xls;

import gui.GuiMain;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import utils.Read;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Чтение данных из файла
 */

public class ReadFromSsrExcel {
    public static List<Read> readFromExcel(String file, String nameWork) throws IOException {

        // поле единицы измерения
        boolean start = false;
        // наименование глав
        String nameSubstantion = "";
        // наименование глав
        String nameDescription = "";
        // заработная плата
        double salary = 0;
        // эксплуатация машин
        double mashineOperation = 0;
        // заработная плата машин
        double mashineOperationSalary = 0;
        // материалы
        double materials = 0;
        // инструменты
        double materialsTransport = 0;
        // охр, опр
        double expenses = 0;
        // прибыль
        double profit = 0;
        // монтируемое оборудовани
        double mountEquipment = 0;
        // монтируемое оборудовани
        double mountEquipmentTransport = 0;
        // другие стредства
        double othersMeans = 0;
        // общая стоимость
        double generalPrice = 0;
        // трудоемкость
        double laborIntensity = 0;

        List<String> read1 = new ArrayList<>(0);
        List<String> read2 = new ArrayList<>(0);
        List<String> read3 = new ArrayList<>(0);
        List<String> read4 = new ArrayList<>(0);
        List<String> read5 = new ArrayList<>(0);
        List<String> read6 = new ArrayList<>(0);
        List<String> read7 = new ArrayList<>(0);
        List<String> read8 = new ArrayList<>(0);
        List<String> read9 = new ArrayList<>(0);
        List<String> read10 = new ArrayList<>(0);
        List<String> read11 = new ArrayList<>(0);
        List<String> read12 = new ArrayList<>(0);
        List<String> read13 = new ArrayList<>(0);
        List<String> read14 = new ArrayList<>(0);

        List<Read> read = new ArrayList<>();
        Read read1_1 = new Read(read1);
        Read read2_2 = new Read(read2);
        Read read3_3 = new Read(read3);
        Read read4_4 = new Read(read4);
        Read read5_5 = new Read(read5);
        Read read6_6 = new Read(read6);
        Read read7_7 = new Read(read7);
        Read read8_8 = new Read(read8);
        Read read9_9 = new Read(read9);
        Read read10_10 = new Read(read10);
        Read read11_11 = new Read(read11);
        Read read12_12 = new Read(read12);
        Read read13_13 = new Read(read13);
        Read read14_14 = new Read(read14);

        read.add(read1_1);
        read.add(read2_2);
        read.add(read3_3);
        read.add(read4_4);
        read.add(read5_5);
        read.add(read6_6);
        read.add(read7_7);
        read.add(read8_8);
        read.add(read9_9);
        read.add(read10_10);
        read.add(read11_11);
        read.add(read12_12);
        read.add(read13_13);
        read.add(read14_14);

        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet myExcelSheet = myExcelBook.getSheet("Лист1");

        // получение строки
        for (int i = 0, j = 0; i < myExcelSheet.getLastRowNum(); i++) {
            HSSFRow rowCurrent = myExcelSheet.getRow(i);
            HSSFRow rowForward = myExcelSheet.getRow(i + 1);

            // получение ячейкм
            try {
                String name = rowCurrent.getCell(1).getStringCellValue();
                start = name.equals(nameWork);
               // }
                if (start) {

                    // наименование глав
                    nameDescription = rowCurrent.getCell(1).getStringCellValue();
                    // заработная плата
                    salary = (rowCurrent.getCell(2) != null) ? rowCurrent.getCell(2).getNumericCellValue() : 0;
                    //salaryString = String.valueOf(salary);
                    // эксплуатация машин
                    mashineOperation = (rowCurrent.getCell(3) != null) ? rowCurrent.getCell(3).getNumericCellValue() : 0;
                    i++;
                    // заработная плата машин
                    mashineOperationSalary = (rowForward.getCell(3) != null) ? rowForward.getCell(3).getNumericCellValue() : 0;
                    // материалы
                    materials = (rowCurrent.getCell(6) != null) ? rowCurrent.getCell(6).getNumericCellValue() : 0;
                    // инструменты
                    materialsTransport = (rowForward.getCell(6) != null) ? rowForward.getCell(6).getNumericCellValue() : 0;
                    // охр, опр
                    expenses = (rowCurrent.getCell(7) != null) ? rowCurrent.getCell(7).getNumericCellValue() : 0;
                    // прибыль
                    profit = (rowForward.getCell(7) != null) ? rowForward.getCell(7).getNumericCellValue() : 0;
                    // монтируемое оборудовани
                    mountEquipment = (rowCurrent.getCell(8) != null) ? rowCurrent.getCell(8).getNumericCellValue() : 0;
                    // монтируемое оборудовани
                    mountEquipmentTransport = (rowForward.getCell(8) != null) ? rowForward.getCell(8).getNumericCellValue() : 0;
                    // другие стредства
                    othersMeans = (rowCurrent.getCell(9) != null) ? rowCurrent.getCell(9).getNumericCellValue() : 0;
                    // общая стоимость
                    generalPrice = (rowCurrent.getCell(11) != null) ? rowCurrent.getCell(11).getNumericCellValue() : 0;
                    // трудоемкость
                    laborIntensity = (rowForward.getCell(11) != null) ? rowForward.getCell(11).getNumericCellValue() : 0;

                    // запись в лист
                    read1.add(String.valueOf(j));
                    read2.add(nameDescription);
                    read3.add(String.valueOf(salary));
                    read4.add(String.valueOf(mashineOperation));
                    read5.add(String.valueOf(mashineOperationSalary));
                    read6.add(String.valueOf(materials));
                    read7.add(String.valueOf(materialsTransport));
                    read8.add(String.valueOf(expenses));
                    read9.add(String.valueOf(profit));
                    read10.add(String.valueOf(mountEquipment));
                    read11.add(String.valueOf(mountEquipmentTransport));
                    read12.add(String.valueOf(othersMeans));
                    read13.add(String.valueOf(generalPrice));
                    read14.add(String.valueOf(laborIntensity));

                    j++;
                }
            } catch (NullPointerException | IllegalStateException | StringIndexOutOfBoundsException e) {
                i = i + 2;
            }
        }

        myExcelBook.close();

        //возврат списка
        return read;
    }
}

package utils;

import gui.GuiMain;
import xls.ReadFromExcel;
import xls.WriteIntoExcel;

import java.io.IOException;
import java.util.List;

/**
 * Выбор оборудования или материалов
 */

public class SelectEquipment {
    public static void select(String fileName1, String fileNameResult, boolean materialsEquipment,
                              boolean materialsAnalog, boolean dellAnalog) throws IOException {

        List<Read> read;

        //получение материала или оборудования
        System.out.println("start read");
        read = ReadFromExcel.readFromExcel(fileName1, materialsEquipment, true, true);
        System.out.println("stop read");


        System.out.println("start write");
        if (fileNameResult.isEmpty()) {
            fileNameResult = "./select.xls";
        }
        WriteIntoExcel.writeIntoExcel(fileNameResult + ".xls", read, materialsEquipment);
        System.out.println("stop write");

        if (GuiMain.fileAnalog) {
            //получение аналога
            System.out.println("start read");
            read = ReadFromExcel.readFromExcel(fileName1, materialsEquipment, true, false);
            System.out.println("stop read");


            System.out.println("start write");
            if (fileNameResult.isEmpty()) {
                fileNameResult = "./select-analog.xls";
            }
            //if (!read.isEmpty()) {
            WriteIntoExcel.writeIntoExcel(fileNameResult + "-analog.xls", read, materialsEquipment);
            System.out.println("stop write");
            //}
        }

    }


}

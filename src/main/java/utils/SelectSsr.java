package utils;

import gui.GuiSsr;
import xls.ReadFromSsrExcel;

import java.io.IOException;
import java.util.List;

/**
 * Выбор оборудования или материалов
 */

public class SelectSsr {
    public static void select(String fileName1, String fileNameResult) throws IOException {

        List<Read> read1, read2, read3, read4, read5, read6;

        //получение материала или оборудования
        System.out.println("start read");
        //read = ReadFromSsrExcel.readFromExcel(fileName1, materialsEquipment, true, true);
        read1 = ReadFromSsrExcel.readFromExcel(fileName1, "Итого  по  главе 2");
        System.out.println("stop read");
        System.out.println(read1);

        //получение материала или оборудования
        System.out.println("start read");
        //read = ReadFromSsrExcel.readFromExcel(fileName1, materialsEquipment, true, true);
        read2 = ReadFromSsrExcel.readFromExcel(fileName1, "Итого  по  главе 9");
        System.out.println("stop read");
        System.out.println(read2);

        //получение материала или оборудования
        System.out.println("start read");
        //read = ReadFromSsrExcel.readFromExcel(fileName1, materialsEquipment, true, true);
        read3 = ReadFromSsrExcel.readFromExcel(fileName1, "ИТОГО ПО ГЛАВАМ 1–9");
        System.out.println("stop read");
        System.out.println(read3);

        //получение материала или оборудования
        System.out.println("start read");
        //read = ReadFromSsrExcel.readFromExcel(fileName1, materialsEquipment, true, true);
        read4 = ReadFromSsrExcel.readFromExcel(fileName1, "Итого  по  главе 10");
        System.out.println("stop read");
        System.out.println(read4);

        //получение материала или оборудования
        System.out.println("start read");
        //read = ReadFromSsrExcel.readFromExcel(fileName1, materialsEquipment, true, true);
        read5 = ReadFromSsrExcel.readFromExcel(fileName1, "Средства на непредвиденные работы и затраты   3% K=0,8");
        System.out.println("stop read");
        System.out.println(read5);

        // 2 Основные здания им сооружения
        String outText1 = "Наименование  глав, объектных (локальных) смет (сметных расчётов), средств " + read1.get(1) + "\n" +
                "Заработная плата " + read1.get(2) + "\n" +
                "Эксплуатация  машин  и  механизмов " + read1.get(3) + "\n" +
                "Материалы,  изделия,  конструкции " + read1.get(5) + "\n" +
                "транспорт " + read1.get(6) + "\n" +
                "ОХР  и  ОПР " + read1.get(7) + "\n" +
                "Плановая прибыль " + read1.get(8) + "\n" +
                "Монтируемое оборудование, мебель " + read1.get(9) + "\n" +
                "транспорт " + read1.get(10) + "\n" +
                "Прочие средства " + read1.get(11) + "\n" +
                "Общая  стоимость, тысяч белорусских рублей " + read1.get(12) + "\n" +
                "Трудоемкость, человеко-часов " + read1.get(13) + "\n";
        String outTextPos1 = "Всего " + read1.get(12) + "\n" +
                "В том числе СМР " + (Float.parseFloat(read1.get(12).toString().replace("[", "").replace("]", ""))
                - Float.parseFloat(read1.get(9).toString().replace("[", "").replace("]", ""))) + "\n" +
                "Распределение объемов " + read1.get(12) + "\n";
        // 9 Прочие работы и расходы
        String outText2 = "Наименование  глав, объектных (локальных) смет (сметных расчётов), средств " + read2.get(1) + "\n" +
                "Заработная плата " + read2.get(2) + "\n" +
                "Эксплуатация  машин  и  механизмов " + read2.get(3) + "\n" +
                "Материалы,  изделия,  конструкции " + read2.get(5) + "\n" +
                "транспорт " + read2.get(6) + "\n" +
                "ОХР  и  ОПР " + read2.get(7) + "\n" +
                "Плановая прибыль " + read2.get(8) + "\n" +
                "Монтируемое оборудование, мебель " + read2.get(9) + "\n" +
                "транспорт " + read2.get(10) + "\n" +
                "Прочие средства " + read2.get(11) + "\n" +
                "Общая  стоимость, тысяч белорусских рублей " + read2.get(12) + "\n" +
                "Трудоемкость, человеко-часов " + read2.get(13) + "\n";
        String outTextPos2 = "Всего " + read2.get(12) + "\n" +
                "В том числе СМР " + (Float.parseFloat(read2.get(12).toString().replace("[", "").replace("]", "")) -
                Float.parseFloat(read2.get(11).toString().replace("[", "").replace("]", ""))) + "\n" +
                "Распределение объемов " + read2.get(12) + "\n";
        // 1 - 9 для авторского
        String outText3 = "Наименование  глав, объектных (локальных) смет (сметных расчётов), средств " + read3.get(1) + "\n" +
                "Заработная плата " + read3.get(2) + "\n" +
                "Эксплуатация  машин  и  механизмов " + read3.get(3) + "\n" +
                "Материалы,  изделия,  конструкции " + read3.get(5) + "\n" +
                "транспорт " + read3.get(6) + "\n" +
                "ОХР  и  ОПР " + read3.get(7) + "\n" +
                "Плановая прибыль " + read3.get(8) + "\n" +
                "Монтируемое оборудование, мебель " + read3.get(9) + "\n" +
                "транспорт " + read3.get(10) + "\n" +
                "Прочие средства " + read3.get(11) + "\n" +
                "Общая  стоимость, тысяч белорусских рублей " + read3.get(12) + "\n" +
                "Трудоемкость, человеко-часов " + read3.get(13) + "\n";
        String outTextPos3 = "Общая стоимость cт. (3 - 6) " + (Float.parseFloat(read3.get(2).toString().replace("[", "").replace("]", "")) +
                        Float.parseFloat(read3.get(3).toString().replace("[", "").replace("]", "")) +
                        Float.parseFloat(read3.get(5).toString().replace("[", "").replace("]", "")) +
                        Float.parseFloat(read3.get(6).toString().replace("[", "").replace("]", "")) +
                        Float.parseFloat(read3.get(7).toString().replace("[", "").replace("]", "")) +
                        Float.parseFloat(read3.get(8).toString().replace("[", "").replace("]", ""))) + "\n";
        // 10 Средства заказчика
        String outText4 = "Наименование  глав, объектных (локальных) смет (сметных расчётов), средств " + read4.get(1) + "\n" +
                "Заработная плата " + read4.get(2) + "\n" +
                "Эксплуатация  машин  и  механизмов " + read4.get(3) + "\n" +
                "Материалы,  изделия,  конструкции " + read4.get(5) + "\n" +
                "транспорт " + read4.get(6) + "\n" +
                "ОХР  и  ОПР " + read4.get(7) + "\n" +
                "Плановая прибыль " + read4.get(8) + "\n" +
                "Монтируемое оборудование, мебель " + read4.get(9) + "\n" +
                "транспорт " + read4.get(10) + "\n" +
                "Прочие средства " + read4.get(11) + "\n" +
                "Общая  стоимость, тысяч белорусских рублей " + read4.get(12) + "\n" +
                "Трудоемкость, человеко-часов " + read4.get(13) + "\n";
        String outTextPos4 = "Всего " + read4.get(12) + "\n" +
                "В том числе СМР " + (Float.parseFloat(read4.get(12).toString().replace("[", "").replace("]", "")) -
                Float.parseFloat(read4.get(11).toString().replace("[", "").replace("]", ""))) + "\n" +
                "Распределение объемов " + read4.get(12) + "\n";
        // Непредвиденные работы и затраты
        String outText5 = "Наименование  глав, объектных (локальных) смет (сметных расчётов), средств " + read5.get(1) + "\n" +
                "Заработная плата " + read5.get(2) + "\n" +
                "Эксплуатация  машин  и  механизмов " + read5.get(3) + "\n" +
                "Материалы,  изделия,  конструкции " + read5.get(5) + "\n" +
                "транспорт " + read5.get(6) + "\n" +
                "ОХР  и  ОПР " + read5.get(7) + "\n" +
                "Плановая прибыль " + read5.get(8) + "\n" +
                "Монтируемое оборудование, мебель " + read5.get(9) + "\n" +
                "транспорт " + read5.get(10) + "\n" +
                "Прочие средства " + read5.get(11) + "\n" +
                "Общая  стоимость, тысяч белорусских рублей " + read5.get(12) + "\n" +
                "Трудоемкость, человеко-часов " + read5.get(13) + "\n";
        String outTextPos5 = "Всего " + read5.get(12) + "\n" +
                "В том числе СМР " + (Float.parseFloat(read5.get(12).toString().replace("[", "").replace("]", "")) -
                Float.parseFloat(read5.get(11).toString().replace("[", "").replace("]", "")) -
                Float.parseFloat(read5.get(10).toString().replace("[", "").replace("]", "")) -
                Float.parseFloat(read5.get(9).toString().replace("[", "").replace("]", ""))) + "\n" +
                "Распределение объемов " + read5.get(12) + "\n";

        GuiSsr.gui(outText1 + "\n" + outTextPos1 + "\n" +
                outText2 + "\n" + outTextPos2 + "\n" +
                outText3 + "\n" + outTextPos3 + "\n" +
                outText4 + "\n" + outTextPos4 + "\n" +
                outText5 + "\n" + outTextPos5 + "\n");
    }

}

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    // 1. Приватный статический экземпляр
    private static Singleton instance;
    private List<String> sharedList;

    // 2. Приватный конструктор ограничивает создание других экземпляров
    private Singleton() {
        sharedList = new ArrayList<>();
        sharedList.add("Изначальное значение");
    }

    // 3. Глобальная точка доступа
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    // Геттеры и сеттеры
    public List<String> getSharedValue() {
        return sharedList;
    }

    public void setSharedValue(List<String> sharedValue) {
        this.sharedList = sharedValue;
    }

}



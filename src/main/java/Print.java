/**
 * Объект списка чтения
 */
public class Print {

    private final String currentName;
    private final int currentX;
    private final int currentY;

    public Print(String currentName, int currentX, int currentY) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.currentName = currentName;
    }


    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public String getCurrentName() {
        return currentName;
    }

    @Override
    public String toString() {
        return "Read{" +
                "currentName=" + currentName +
                ", currentX=" + currentX +
                ", currentY='" + currentY + '\'' +
                '}';
    }
}

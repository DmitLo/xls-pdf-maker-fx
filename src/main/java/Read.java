import java.util.ArrayList;
import java.util.List;

/**
 * Объект списка чтения
 */
public class Read {

        private final List<String> name;

        public Read(List<String> name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "" + name;
        }

        public List<String> rowTable() {
        return name;
    }

}

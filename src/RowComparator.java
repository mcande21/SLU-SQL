import java.util.Comparator;

public class RowComparator {

    public static class EqualComparator implements Comparator<TableEntry> {

        // returns 0 if the condition holds
        @Override
        public int compare(TableEntry o1, TableEntry o2) {
            return o1.compareTo(o2);
        }
    }

    public static class LTComparator implements Comparator<TableEntry> {

        // return 0 if the comparison < 0
        @Override
        public int compare(TableEntry o1, TableEntry o2) {
            if (o1.compareTo(o2) < 0) {
                // less then condition held
                return 0;
            } else {
                return 1; // garbage
            }
        }
    }

    public static class GTComparator implements Comparator<TableEntry> {

        // return 0 if the comparison > 0
        @Override
        public int compare(TableEntry o1, TableEntry o2) {
            if (o1.compareTo(o2) > 0) {
                // Greater than condition held
                return 0;
            } else {
                return 1; // garbage
            }
        }
    }
}

import java.util.ArrayList;
import java.util.Objects;

public class Row {
    private int RowSize;

    private ArrayList<TableEntry> elements;

    private int uniqueID;

    public Row (int rowsize, int uniqueID) {
        this.RowSize = rowsize;
        this.elements = new ArrayList<TableEntry>(rowsize);
        this.uniqueID = uniqueID;
    }

    public int size() {
        return RowSize;
    }

    public TableEntry getindex(int i) {
        return elements.get(i);
    }

    public void addInt(int nextInt) {
        elements.add(new TableEntry.IntTableEntry(nextInt));
    }

    public void addBool(boolean nextBoolean) {
        elements.add(new TableEntry.BoolTableEntry(nextBoolean));
    }

    public void addString(String next) {
        elements.add(new TableEntry.StringTableEntry(next));
    }

    public void addDouble(double nextDouble) {
        elements.add(new TableEntry.DoubleTableEntry(nextDouble));
    }

    public void Deleteme() {
        int loop = elements.size();
        if (loop > 0) {
            this.elements.subList(0, loop).clear();
            this.RowSize = 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return uniqueID == row.uniqueID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueID);
    }
}

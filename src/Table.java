import java.util.*;

public class Table {
    private String tablename;
    private ArrayList<TableEntry.Type> coltypes;
    private ArrayList<String> colnames;
    private ArrayList<Row> content;
    private Integer colidx;
    private String idxType;
    private Map<TableEntry, ArrayList<Row>> IdxMap;
    private boolean qmode;

    // use table entrey as keys, have to generate a hashcode and isequal method in Table entry to do so.
    // youve done the majority of this before
    // then on printing,you have to look into the logic to see < > or =, not just =.
    // array list in IDXmap should hold Row not Integers, so it holds pointers to all the rows
    // Your Row class needs an equals method to do so.
    // Add a unique

    public Table(String name, ArrayList<TableEntry.Type> types, ArrayList<String> colnames, ArrayList<Row> content, boolean qmode) {
        this.tablename = name;
        this.coltypes = types;
        this.colnames = colnames;
        this.content = content;
        this.qmode = qmode;
    }

    public boolean containsCol(String colname) {
        return colnames.contains(colname);
    }


    public void remove() {
        for (int i = this.coltypes.size() - 1; i >= 0; i--) {
            this.coltypes.remove(i);
        }
        for (int i = this.colnames.size() - 1; i >= 0; i--) {
            this.colnames.remove(i);
        }
    }

    public void add(ArrayList<Row> passthru) {

        int tmpsize = content.size();
        /*
        for (int i = content.size(); i < tmpsize + passthru.size(); i++){
            content.add(i, passthru.get(i));
         */
        content.addAll(passthru);
        System.out.printf("Added %d rows to %s from position %d to %d\n", passthru.size(), tablename, tmpsize, content.size() - 1);
    }

    public int numtypes() {
        return this.coltypes.size();
    }

    public void print(ArrayList<String> colnameprint) {
        // you need to pass through the numrows and repplace coltypes, for when you have to choose.
        // this is only set up for ALL
        ArrayList<Integer> print = new ArrayList<>();
        for (String s : colnameprint) {
            print.add(colnames.indexOf(s));
        }
        StringBuilder printme = new StringBuilder();
        for (String name : colnameprint) {
            printme.append(name + " ");
        }
        if (qmode)
            System.out.printf(printme + "\n");
        StringBuilder Rows = new StringBuilder();
        int printmesize = 0;
        for (int i = 0; i < content.size(); i++) {
            Row element = content.get(i);
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < print.size(); x++) {
                int index = print.get(x);
                row.append(element.getindex(index).getData()).append(" ");
            }
            printmesize++;
            Rows.append(row + "\n");
        }
        // does NOT print in quite mode
        if (qmode)
            System.out.printf(Rows + "");
        // prints in quite mode
        System.out.printf("Printed %d matching rows from %s\n", printmesize, tablename);

    }

    public TableEntry.Type type(int i) {
        return coltypes.get(i);
    }

    public TableEntry.Type colToCompare(String colToCompare) {
        int index = colnames.indexOf(colToCompare);
        return coltypes.get(index);
    }

    public void printWithCondition(String colToCompare, Comparator<TableEntry> cmp, TableEntry val, ArrayList<String> colstoprint, String compare) {
        // which columns are we comparing
        int colIdxToCompare = colnames.indexOf(colToCompare);
        ArrayList<Integer> colIdx = new ArrayList<>();
        for (String s : colstoprint) {
            colIdx.add(colnames.indexOf(s));
        }
        if (qmode) {
            for (String s : colstoprint) {
                System.out.print(s + " ");
            }
            System.out.print("\n");
        }
        // counting what's added
        int pritnmesize = 0;
        // if the table contains a hash and the cmp is equal
        if (IDXisnotempty() && colIdxToCompare == colidx && compare.equals("=")) {
            if (IdxMap.containsKey(val)) {
                StringBuilder printme = new StringBuilder();
                for (Row e : IdxMap.get(val)) {
                    for (int x : colIdx) {
                        printme.append(e.getindex(x).getData()).append(" ");
                    }
                    pritnmesize++;
                    printme.append("\n");
                }
                if (qmode)
                    System.out.print(printme);
            }
        } else if (IDXisnotempty() && colIdxToCompare == colidx && idxType.equals("bst")) {
            TreeMap<TableEntry, ArrayList<Row>> IdxMapBST = (TreeMap<TableEntry, ArrayList<Row>>) IdxMap;
                StringBuilder printme = new StringBuilder();
                if (compare.equals(">")) {
                    TableEntry current = IdxMapBST.higherKey(val);
                    while (current != null) {
                        for (Row e : IdxMapBST.get(current)) {
                            for (int x : colIdx) {
                                printme.append(e.getindex(x).getData() + " ");
                            }
                            pritnmesize++;
                            printme.append("\n");
                        }
                        current = IdxMapBST.higherKey(current);
                    }
                } else {
                    TableEntry current = IdxMapBST.firstKey();
                    while (current.compareTo(val) < 0) {
                        for (Row e : IdxMapBST.get(current)) {
                            for (int x : colIdx) {
                                printme.append(e.getindex(x).getData() + " ");
                            }
                            pritnmesize++;
                            printme.append("\n");
                        }
                        current = IdxMapBST.higherKey(current);
                    }
                }
                if (qmode)
                    System.out.print(printme);
        } else {
            for (Row r : content) {
                // check if this row meets the comparison condition
                if ((cmp.compare(r.getindex(colIdxToCompare), val)) == 0) {
                    for (int i : colIdx)
                        if (qmode)
                            System.out.print(r.getindex(i).getData() + " ");
                    if (qmode) {
                        System.out.print("\n");
                        pritnmesize++;
                    }
                }
            }
        }
        System.out.printf("Printed %d matching rows from %s\n", pritnmesize, tablename);
    }


    public void generateHashIdx(String colname, String indextype) {
        // error checking
        // if there is an existing hashmap, it is cleared and replaced
        if (this.IdxMap != null)
            IdxMap.clear();
        this.idxType = indextype;
        if (indextype.equals("hash")) {
            this.IdxMap = new HashMap<>();
        } else {
            this.IdxMap = new TreeMap<>();
        }
            this.colidx = this.colnames.indexOf(colname);
            for (Row x : content) {
                TableEntry key = x.getindex(colidx);
                if (!IdxMap.containsKey(key)) {
                    ArrayList<Row> addme = new ArrayList<>();
                    addme.add(x);
                    IdxMap.put(key, addme);
                } else {
                    IdxMap.get(key).add(x);
                }
            }
        }

    public void join(Table t2, Comparator<TableEntry> cmp, String table1Col,
                     String table2Col, ArrayList<String> colsToPrint, ArrayList<Boolean> printFromTable1) {
        // Error checking on column
        for (String value : colsToPrint) {
            if (!this.colnames.contains(value) && !t2.colnames.contains(value)) {
                System.err.printf("The column %s you wish to join does not exist in either table\n", value);
                System.exit(1);
            }
        }

        // build up the print indicies
        ArrayList<Integer> colIDXToPrint = new ArrayList<>(colsToPrint.size());
        for (int i = 0; i < colsToPrint.size(); i++) {
            // ternary operator (conditioon ? true expression : false expression
            ArrayList<String> targetTableColNames = printFromTable1.get(i) ? colnames : t2.colnames;
            colIDXToPrint.add(targetTableColNames.indexOf(colsToPrint.get(i)));
        }
        // build up the column comparison indicies
        int tabel1idx = colnames.indexOf(table1Col);
        int table2idx = t2.colnames.indexOf(table2Col);

        // printing out colnames
        if (qmode) {
            for (String s : colsToPrint) {
                System.out.print(s + " ");
            }
            System.out.print("\n");
        }
        int printsize = 0;

        // using index to join tables
        if (t2.IDXisnotempty() && table2idx == t2.colidx){
            for (Row e: content) {
                if (t2.IdxMap.containsKey(e.getindex(tabel1idx))) {
                    ArrayList<Row> t2row = t2.IdxMap.get(e.getindex((tabel1idx)));
                    for (Row x : t2row) {
                        printFromTwoRows(e, x, colIDXToPrint, printFromTable1);
                        printsize++;
                    }
                }
            }
        } else {
            for (Row r1 : content) {
                // loop through all the rows in table2
                for (Row r2 : t2.content) {
                    // compare the rows
                    if (cmp.compare(r1.getindex(tabel1idx), r2.getindex(table2idx)) == 0) {
                        // the rows matched
                        printFromTwoRows(r1, r2, colIDXToPrint, printFromTable1);
                        printsize++;
                    }
                }
            }
        }
        System.out.printf("Printed %d rows from joining %s to %s\n", printsize, this.tablename, t2.tablename);
    }

    private void printFromTwoRows(Row r1, Row r2, ArrayList<Integer> colIDXToPrint, ArrayList<Boolean> printFromTable1) {
        StringBuilder printme = new StringBuilder();
        for (int i = 0; i < colIDXToPrint.size(); i++) {
            if (printFromTable1.get(i)) {
                printme.append(r1.getindex(colIDXToPrint.get(i)).getData()).append(" ");
            } else {
                printme.append(r2.getindex(colIDXToPrint.get(i)).getData()).append(" ");
            }
        }
        if (qmode)
            System.out.print(printme + "\n");


    }

    public void deleteRow(String name, Comparator<TableEntry> cmp, TableEntry val) {

        int colidx1 = colnames.indexOf(name);
        int size = 0;
        for (int i = 0; i < content.size(); i++) {
            Row deleteme = content.get(i);
            if (cmp.compare(deleteme.getindex(colidx1), val) == 0) {
                if (IDXisnotempty() && IdxMap.containsKey(deleteme.getindex(colidx))) {
                    IdxMap.get(deleteme.getindex(colidx)).remove(deleteme);
                }
                deleteme.Deleteme();
                this.content.remove(i);
                i--;
                size++;
            }
        }
        System.out.printf("Deleted %d rows from %s\n", size, tablename);

    }

    public boolean IDXisnotempty() {
        if (IdxMap == null)
            return false;
        return !IdxMap.isEmpty();
    }

}

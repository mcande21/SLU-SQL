import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class Database {

    private HashMap<String, Table> mothership;
    private boolean qmode;
    private int ID = 0;

    public Database(boolean qmode) {
        this.qmode = qmode;
        mothership = new HashMap<>();
    }


    public void readCreateInput(Scanner in) {
        StringBuilder colNamesprint = new StringBuilder();
        String TableName = in.next();
        int TableSize = in.nextInt();
        // this is where we create the new table using the table class
        ArrayList<TableEntry.Type> colTypes = new ArrayList<>(TableSize);
        ArrayList<String> colName = new ArrayList<>(TableSize);
        ArrayList<Row> content = new ArrayList<>();
        for (int i = 0; i < TableSize; i++) {
            TableEntry.Type addmeType = getType(in.next());
            colTypes.add(i, addmeType);
        }
        for (int i = 0; i < TableSize; i++) {
            String addMeName = in.next();
            colName.add(i, addMeName);
            colNamesprint.append(addMeName).append(" ");
        }
        Table addme = new Table(TableName, colTypes, colName, content, qmode);
        if (mothership.containsKey(TableName)) {
            System.err.printf("A table named %s already exists in the database", TableName);
            System.exit(1);
        }
        mothership.put(TableName, addme);

        // Print out of table created with rows
        System.out.printf("New table " + TableName + " with column(s) " + colNamesprint + "created\n");
    }

    static TableEntry.Type getType(String type) {
        switch (type) {
            case "string":
                return TableEntry.Type.String;
            case "bool":
                return TableEntry.Type.Bool;
            case "int":
                return TableEntry.Type.Int;
            case "double":
                return TableEntry.Type.Double;
            default:
                return null;
        }
    }


    public void readRemoveInput(Scanner in) {

        String TableName = in.next();
        // error checking
        if (!mothership.containsKey(TableName)) {
            System.err.printf("%s is not the name of a table in the database", TableName);
            System.exit(1);
        }
        // removing the columns and rows from the Table. Resetting Tablename to empty
        mothership.get(TableName).remove();
        // Removing Table from Database
        mothership.remove(TableName);

        System.out.printf("Table %s deleted\n", TableName);
    }

    public void readInsertInput(Scanner in) {
        // Assuming there is "Insert Into"
        in.next();
        String TableName = in.next();
        Table insertme = mothership.get(TableName);
        int numRows = in.nextInt();
        // skip over ROWS
        in.next();
        if (!mothership.containsKey(TableName)) {
            System.err.printf("%s is not the name of a table in the database", TableName);
            System.exit(1);
        }
        // this is where we create new rows to be added in
        ArrayList<Row> passthru = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            Row colName = new Row(insertme.numtypes(), ID++);
            for (int x = 0; x < insertme.numtypes(); x++) {
                switch (insertme.type(x)){
                    case Int:
                        colName.addInt(in.nextInt());
                        break;
                    case Bool:
                        colName.addBool(in.nextBoolean());
                        break;
                    case String:
                        colName.addString(in.next());
                        break;
                    case Double:
                        colName.addDouble(in.nextDouble());
                }
            }
            passthru.add(i, colName);
        }
        mothership.get(TableName).add(passthru);
    }

    public void readPrintInput(Scanner in) {
        // FROM<tablename><N><print_colname1><print_colname2> ...<print_colnameN> [WHERE <colname> <OP> <value> | ALL ]
        // skipping FOR
        in.next();
        String TableName = in.next();
        Table printme = mothership.get(TableName);
        int numRows = in.nextInt();
        ArrayList<String> colnameprint = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            colnameprint.add(i, in.next());
        }
        // handling the ALL or WHERE
        if (in.next().equals("ALL")){
            // this is all for conditions
            printme.print(colnameprint);
        } else {
            String colToCompare = in.next();
            String op = in.next();
            TableEntry val = TableEntry.stringtoTE(in.next(), printme.colToCompare(colToCompare));
            Comparator<TableEntry> cmp;
            switch (op) {
                case "=":
                    cmp = new RowComparator.EqualComparator();
                    break;
                case "<":
                    cmp = new RowComparator.LTComparator();
                    break;
                default:
                    // >
                    cmp = new RowComparator.GTComparator();

            }
            printme.printWithCondition(colToCompare, cmp, val, colnameprint, op);

        }
    }

    public void readGenerateInput(Scanner in) {
        // to skip over FOR
        in.next();
        String Tablename = in.next();
        String idxtype = in.next();
        // skipping over "INDEX ON"
        in.next(); in.next();
        String colname = in.next();
        //Everything is captures and ready to get passed through after some error checking
        if (!mothership.containsKey(Tablename)) {
            System.err.printf("%s is not the name of a table in the database\n", Tablename);
            System.exit(1);
        } if (!mothership.get(Tablename).containsCol(colname)) {
            System.err.printf("%s is not the name of a column in the Table %s\n", colname, Tablename);
            System.exit(1);
        }
        // Calling on table to work their magic
        mothership.get(Tablename).generateHashIdx(colname, idxtype);

        System.out.printf("Created %s index for table %s on column %s\n",idxtype, Tablename, colname);
    }

    public void readJoinIput(Scanner in) {
        // JOIN <tablename1> AND <tablename2> WHERE <colname1>=<colname2> AND
        //PRINT<N> <print_colname1> <1|2> <print_colname2> <1|2> ... <print_colnameN>
        //<1|2>

        String Table1 = in.next();
        // Skipping AND
        in.next();
        String Table2 = in.next();
        // Skiiping WHERE
        in.next();
        String Table1Col = in.next();
        // Skipping =
        in.next();
        String Table2Col = in.next();
        // Skipping AND PRINT
        in.next();
        in.next();
        int numColsToPrint = in.nextInt();

        // two lists: one for the names and one for the source table
        ArrayList<String> colsToPrint = new ArrayList<>(numColsToPrint);
        // true if table1, false if table2
        ArrayList<Boolean> colFromTable1 = new ArrayList<>(numColsToPrint);

        for(int i = 0; i < numColsToPrint; i++) {
            colsToPrint.add(in.next());
            if (in.nextInt() == 1) {
                colFromTable1.add(true);
            } else {
                colFromTable1.add(false);
            }
        }
        // Error checking

        if (!mothership.containsKey(Table1)) {
            System.err.printf("%s is not the name of a table in the database\n", Table1);
            System.exit(1);
        } else if (!mothership.containsKey(Table2)) {
            System.err.printf("%s is not the name of a table in the database\n", Table2);
            System.exit(1);
        }
        Table t1 = mothership.get(Table1);
        Table t2 = mothership.get(Table2);

        Comparator<TableEntry> joinComparator = new RowComparator.EqualComparator();

        t1.join(t2, joinComparator, Table1Col, Table2Col, colsToPrint, colFromTable1);
    }

    public void readDeleteInput(Scanner in) {
        // Skipping over From
        in.next();
        String Tablename = in.next();
        Table Deleteme = mothership.get(Tablename);
        // Skipping over WHERE
        in.next();
        String colname = in.next();
        String op = in.next();
        Comparator<TableEntry> cmp;
        TableEntry val = TableEntry.stringtoTE(in.next(), Deleteme.colToCompare(colname));

        switch (op) {
            case "=":
                cmp = new RowComparator.EqualComparator();
                break;
            case "<":
                cmp = new RowComparator.LTComparator();
                break;
            default:
                // >
                cmp = new RowComparator.GTComparator();
        }
        Deleteme.deleteRow(colname, cmp, val);
    }
}
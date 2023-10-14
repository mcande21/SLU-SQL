import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // check config
        Config config = new Config(args);

        Database db = new Database(config.qmode);
        //command processing loop
        Scanner in = new Scanner(System.in);

        // prompt the user
        System.out.print("% ");

        // loop while there is more data to read
        while(in.hasNext()) {
            // read the next string
            String cmd = in.next();

            switch(cmd.charAt(0)) {

                case 'Q':
                    System.out.println("Goodbye, SLU!");
                    System.exit(0);
                    break;
                case '#':
                    // ignore the next line
                    in.nextLine();
                    break;
                case 'C':
                    db.readCreateInput(in);
                    break;
                case 'R':
                    db.readRemoveInput(in);
                    break;
                case 'I':
                    db.readInsertInput(in);
                    break;
                case 'P':
                    db.readPrintInput(in);
                    break;
                case 'D':
                    db.readDeleteInput(in);
                    break;
                case 'G':
                    db.readGenerateInput(in);
                    break;
                case 'J':
                    db.readJoinIput(in);
                    break;
                default:
                    System.err.printf("Unknown command: %s\n", cmd);
            }

            // re-prompt for the next command
            System.out.print("% ");
        }
    }


}


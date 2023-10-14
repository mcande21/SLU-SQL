import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;


public class Config {

    boolean help;
    boolean qmode = true;
    LongOpt[] longOptions = {
            new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
            new LongOpt("quiet", LongOpt.NO_ARGUMENT, null, 'q'),
    };

    public Config(String [] args) {


        Getopt g = new Getopt("Project3", args, "hq", longOptions);
        int choice = 0;
        while( (choice = g.getopt()) != -1) {
            switch (choice) {
                case 'h':
                    help = true;
                    break;
                case 'q':
                    qmode = false;
                    break;
                default:
                    System.out.println("Command arguments not set up yet");
            }
        }




    }
}

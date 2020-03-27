import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class Driver {

    public static void main (String[] args){
        //MersenneTwister mt = new MersenneTwister();
        //System.out.println(mt.nextGaussian() + ": "+args[0]);

        if (args[0].equals("-configuration")){
            // Run individual whoose name is args[1]
            String name = args[1] ;
            System.out.printf("Loading and running %s now\n", name);
            // find file name and run

            // Make report
            try{
                generateReport(true, name.substring(0, name.length()-5));
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }else if(args[0].equals("-search_best_configuration")){
            // Run all possible configurations and identity best
            System.out.printf("Running all default config's and finding Best.");

            String file_name = "ga|pso|sa" +"_best.json";
            // Write best one to .json
        }

    }

    private static void generateReport(boolean writeToFile, String fileName) throws IOException {
        String withTime, withoutTime;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        withTime = sdf.format(new Date());

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        withoutTime = sdf.format(new Date());

        if (writeToFile == false){

            System.out.println("Evaluation  |  " + withTime);
            System.out.println("Configuration:  " + fileName+".json");
            System.out.println(); // Print specific specs
            System.out.println("==============================================");

        }else{

            String fName = "report_" +  fileName +"_"+ withoutTime;
            File file = new File(fName+ ".txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("Evaluation  |  " + withTime + "\n");
            bw.write("Configuration:  " + fileName+".json"+ "\n");

            String algorithm_info = "GA | #10000 | RWS | 2PX (0.7) | EXM (0.003)";
            bw.write("                " + algorithm_info+ "\n");
            bw.write("===================================================================================="+ "\n");
            bw.write(String.format("%-8s %-8s %-8s %-8s %s", "#", "bweight", "bvalue", "squality", "knapsack")+ "\n");
            bw.write("------------------------------------------------------------------------------------"+ "\n");
            //for loop
            bw.write("*** For Loop Stuff ***"+ "\n");
            bw.write("------------------------------------------------------------------------------------"+ "\n");
            bw.write("[Statistics]"+ "\n");
            int runtime = 12322;
            bw.write("Runtime" + String.format("%15d", runtime)+ "\n");
            bw.newLine();

            bw.write(String.format("%-20s %-8s %-8s %-8s %-8s", "Convergence", "#", "bweight", "bvalue", "squality") + "\n");
            //
            bw.newLine();
            bw.newLine();



            float platAverage = 3;
            int startPlat = 443;
            int endPlat = 472;
            bw.write("Plateau  |  Longest sequence " +  String.valueOf(startPlat) +"-"+ String.valueOf(endPlat) +
                    " with improvement less average "  + String.valueOf(platAverage) + "%."+ "\n");

            bw.close();


        }
    }
}

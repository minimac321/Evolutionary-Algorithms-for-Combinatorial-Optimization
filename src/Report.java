import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {

    public Report(boolean writeToFile, String fileName, String config, SolutionInstance[] bestSolutions,
                               double runtime) throws IOException {
        String withTime, withoutTime;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        withTime = sdf.format(new Date());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        withoutTime = sdf.format(new Date());

        if (!writeToFile){
            System.out.println("Evaluation  |  " + withTime);
            System.out.println("Configuration:  " + config);
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

            bw.write("                " + config+ "\n");
            bw.write("===================================================================================="+ "\n");
            bw.write(String.format("%-8s %-8s %-8s %-8s %s", "#", "bweight", "bvalue", "squality", "knapsack")+ "\n");
            bw.write("------------------------------------------------------------------------------------"+ "\n");

            for (int i = 0; i < bestSolutions.length; i++) {
                SolutionInstance s = bestSolutions[i];
                double sq = s.fitness*1.0 / Driver.best_optimum;
                bw.write(String.format("%-8d %-8d %-8d %-8fs %s\n", (i+1), s.totalWeight, s.fitness, sq,
                        s.toString()));

            }
            bw.write("------------------------------------------------------------------------------------"+ "\n");
            bw.write("[Statistics]"+ "\n");

            bw.write("Runtime" + String.format("%15f", runtime)+ "\n");
            bw.newLine();

            bw.write(String.format("%-20s %-8s %-8s %-8s %-8s", "", "#", "bweight", "bvalue", "squality") + "\n");
            // 4 lines
            SolutionInstance s = bestSolutions[2499];
            double sq = s.fitness*1.0 / Driver.best_optimum;
            bw.write(String.format("%-20s %-8d %-8d %-8d %-8f\n", "", 2500, s.totalWeight, s.fitness, sq,
                    s.toString()));
            s = bestSolutions[4999];
            sq = s.fitness*1.0 / Driver.best_optimum;
            bw.write(String.format("%-20s %-8d %-8d %-8d %-8f\n", "", 5000, s.totalWeight, s.fitness, sq,
                    s.toString()));
            s = bestSolutions[7499];
            sq = s.fitness*1.0 / Driver.best_optimum;
            bw.write(String.format("%-20s %-8d %-8d %-8d %-8f\n", "", 7500, s.totalWeight, s.fitness, sq,
                    s.toString()));
            s = bestSolutions[9999];
            sq = s.fitness*1.0 / Driver.best_optimum;
            bw.write(String.format("%-20s %-8d %-8d %-8d %-8f\n", "", 10000, s.totalWeight, s.fitness, sq) );

            //
            bw.newLine();
            bw.newLine();

            int startPlat = 0;
            int endPlat = 0;
            int plat_length = 0;

            int startPlatF = Integer.MIN_VALUE;
            int endPlatF = Integer.MIN_VALUE;
            int plat_lengthF = Integer.MIN_VALUE;
            float platAverage = 0;
            int count = 1;

//            while (count < bestSolutions.length-1){
//                if (bestSolutions[count].fitness == bestSolutions[count+1].fitness){
//                    startPlat = count;
//                    for (int j = count; j < bestSolutions.length-1; j++) {
//                        if (bestSolutions[count].fitness != bestSolutions[j].fitness){
//                            endPlat = j;
//                            break;
//                        }
//                    } // end for
//                    plat_length = endPlat - startPlat;
//
//                    if (plat_length > plat_lengthF){
//                        startPlatF = startPlat;
//                        endPlatF = endPlat;
//                        plat_lengthF = plat_length;
//                        count+=plat_lengthF;
//                    }else {
//                        count+=plat_length;
//                    }
//                }
//                else count++;
//            }

            bw.write("Plateau  |  Longest sequence " +  String.valueOf(startPlat) +"-"+ String.valueOf(endPlat) +
                    " with improvement less average "  + String.valueOf(platAverage) + "%.\n");

            bw.close();
        } // end else

    } // generateReport
}

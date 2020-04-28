import org.json.JSONTokener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

public class Report {

    public Report(boolean writeToFile, String fileName, String config, SolutionInstance[] bestSolutions,
                               double runtime) throws IOException {
        String withTime, withoutTime;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        withTime = sdf.format(new Date());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        withoutTime = sdf.format(new Date());

        if (writeToFile == false){
            System.out.println("Evaluation  |  " + withTime);
            System.out.println("Configuration:  " + config);
            System.out.println("==============================================");
            System.out.printf("Finished with Simulation after %d iterations\n", bestSolutions.length);
            System.out.printf("Runtime: %8f\n", runtime);
            System.out.printf("Optimal Output: fitness = %5d | weight = %5d\n",
                    bestSolutions[9999].fitness, bestSolutions[9999].weight);

        }else{
            String fName = "report_" +  fileName +"_"+ withoutTime;
            String algorithmName ="";
            if (fileName.contains("ga")) algorithmName = "ga";

            else if (fileName.contains("pso")) algorithmName = "pso";
            else if (fileName.contains("sa")) algorithmName = "sa";

            File file = new File("Default_Output/"+ algorithmName + "/" + fName+ ".txt");

            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("Evaluation  |  " + withTime + "\n");
            bw.write("Configuration:  " + fileName+".json"+ "\n");

            bw.write("                " + config+ "\n");
            bw.write("===================================================================================="+ "\n");
            bw.write(String.format("%-8s %-8s %-8s %-10s %s", "#", "bweight", "bvalue", "squality", "knapsack")+ "\n");
            bw.write("------------------------------------------------------------------------------------"+ "\n");

            for (int i = 0; i < bestSolutions.length; i++) {
                SolutionInstance s = bestSolutions[i];
                double sq = s.fitness*1.0 / Driver.best_optimum;
                bw.write(String.format("%-8d %8d %8d %8.2f%% %s\n", (i+1), s.weight, s.fitness, sq,
                        s.toString()));

            }
            bw.write("------------------------------------------------------------------------------------"+ "\n");
            bw.write("[Statistics]"+ "\n");

            bw.write("Runtime" + String.format("%12.2f ms", runtime)+ "\n");
            bw.newLine();

            if (bestSolutions.length == 10000){
                bw.write(String.format("%-20s %-8s %8s %8s %10s", "Convergence", "#", "bweight",
                        "bvalue",
                        "squality") +
                        "\n");

                // 4 lines
                SolutionInstance s = bestSolutions[2499];
                double sq = s.fitness*1.0 / Driver.best_optimum;
                bw.write(String.format("%-20s %-8d %8d %8d %8.2f%%\n", "", 2500, s.weight, s.fitness, sq,
                        s.toString()));
                s = bestSolutions[4999];
                sq = s.fitness*1.0 / Driver.best_optimum;
                bw.write(String.format("%-20s %-8d %8d %8d %8.2f%%\n", "", 5000, s.weight, s.fitness, sq,
                        s.toString()));
                s = bestSolutions[7499];
                sq = s.fitness*1.0 / Driver.best_optimum;
                bw.write(String.format("%-20s %-8d %8d %8d %8.2f%%\n", "", 7500, s.weight, s.fitness, sq,
                        s.toString()));
                s = bestSolutions[9999];
                sq = s.fitness*1.0 / Driver.best_optimum;
                bw.write(String.format("%-20s %-8d %8d %8d %8.2f%%\n", "", 10000, s.weight, s.fitness, sq) );

                //
                bw.newLine();
                bw.newLine();
            }else{
                bw.write(String.format("%-20s %-8s %8s %8s %10s", "", "#", "bweight",
                        "bvalue",
                        "squality") +
                        "\n");
                int half = (int)(bestSolutions.length/2);
                SolutionInstance s = bestSolutions[half];
                double sq = s.fitness*1.0 / Driver.best_optimum;
                bw.write(String.format("%-20s %-8d %8d %8d %8.2f%%\n", "", half, s.weight, s.fitness, sq,
                        s.toString()));
            }

            int[] fitnesses = new int[bestSolutions.length];
            for (int i = 0; i < bestSolutions.length; i++) {
                fitnesses[i] = bestSolutions[i].fitness;
            }

            LinkedHashMap<Integer, Integer> lhm = new LinkedHashMap<Integer, Integer>();
            for (int i = fitnesses.length-1; i >= 0; i--) {
                lhm.put(fitnesses[i], i);
            }
            Set<Integer> keys = lhm.keySet();
            ArrayList<Integer> values = new ArrayList<>();
            ArrayList<Integer> key = new ArrayList<>();
            lhm.forEach((x,y)-> {
                key.add((x));
                values.add(y);
            });

            Iterator I = keys.iterator();
            int diff = 0;
            int pos = -1;
            int fStart = 0;
            int fEnd = 0;
            int start = lhm.get(I.next());

            for (int i = 1; i < keys.size(); i++) {
                int next = lhm.get(I.next());
                int tmp = start - next ;
                if (tmp > diff){
                    diff = tmp;
                    pos = i;
                    fStart = start;
                    fEnd = next;
                    //System.out.printf("Longest Plat %d @ pos: %d. Between %d and %d\n", diff, pos, fStart, fEnd);
                }
                start = next;
            }

            double dStart = key.get(pos);
            double dEnd = key.get(pos-1);
            double platAvg = (1- ((1.0* dStart )/dEnd)) *100;

            bw.write("Plateau  |  Longest sequence " +  String.valueOf(fEnd) +"-"+ String.valueOf(fStart) +
                    " with improvement average "  + String.format("%.2f", platAvg) + "%.\n");

            bw.close();
            System.out.println("Report Generated.");
        } // end else
    } // generateReport
}

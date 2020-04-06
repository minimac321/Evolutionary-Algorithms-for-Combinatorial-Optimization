import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class Driver {

    public static item[] Items;
    public static final int max_iterations = 10000;


    public static void main (String[] args) {

        PopulateItems(150);

//        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(10000, 0.0001, "Ayy");
//        MersenneTwister randomGenerator = new MersenneTwister(System.currentTimeMillis());
//
//        SolutionInstance currentInstance = new SolutionInstance(Items, 150, 822);
//        currentInstance.Solution[5] = true;
//        System.out.println(currentInstance.toString());
//
//        try {
//            SolutionInstance c = currentInstance.clone();
//            System.out.println("Clone method: " + c.toString());
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//
//
//        boolean[] b = new boolean[150];
//        Arrays.fill(b, false);
//        b[1] = true;
//
//
//        SolutionInstance nextInstance = new SolutionInstance(Items, 150, 822, b);
//        System.out.println(nextInstance.toString());
//
//
//
//
//        double actualEnergy = simulatedAnnealing.getEnergy(currentInstance);
//        double newEnergy = simulatedAnnealing.getEnergy(nextInstance);
//
//        double actualW = simulatedAnnealing.getWeight(currentInstance);
//        double newW = simulatedAnnealing.getWeight(nextInstance);
//
//        System.out.printf("New energy = %6f; Old Energy = %6f\n", newEnergy, actualEnergy);
//        System.out.printf("New weight = %6f; Old weight = %6f\n", newW, actualW);
//
//        double check = simulatedAnnealing.AcceptanceProbability(actualEnergy, newEnergy, 10000) ;
//        double rnd =  randomGenerator.nextDouble();
//
//
//        System.out.printf(" %6f compared to %6f\n", check, rnd);
//
//        if (check  > rnd) {
//            System.out.println("Accepted");
//            //currentInstance = nextInstance;
//        }
//
//        double check2 = simulatedAnnealing.AcceptanceProbability(newEnergy, actualEnergy, 10000) ;
//        System.out.println(check2);
//
//        //
//        System.exit(0);

        if (args[0].equals("-configuration")){

            String name = args[1] ;
            System.out.printf("Loading and running: %s now\n", name);

            if (name.contains("ga")){
                runGA(name);
            }
            else if (name.contains("sa")){
                runSA(name);
            }
            else if (name.contains("pso")){
                runPSO(name);
            }

            // Make report
//            try{
//                generateReport(true, name.substring(0, name.length()-5));
//            }
//            catch (IOException e){
//                e.printStackTrace();
//            }

        }else if(args[0].equals("-search_best_configuration")){
            // Run all possible configurations and identity best
            System.out.printf("Running all default config's and finding Best.");

            String file_name = "ga|pso|sa" +"_best.json";
            // Write best one to .json
        }

    }

    private static void runPSO(String name) {
        try{
            // find file name and run
            FileReader file = new FileReader("C:/Users/Shawn Cole/Documents/UCT '20/Evolutionary " +
                    "Computing/Assignment/pso_default/" + name + ".json");
            BufferedReader br = new BufferedReader(file);

            // JSON is all on 1 line
            String json = br.readLine();
            System.out.println(json);

            JSONObject obj = new JSONObject(json);
            int particleNums = obj.getInt("number_particles");
            int minVelocity = obj.getInt("minimum_velocity");
            int maxVelocity = obj.getInt("maximum_velocity");
            double c1 =obj.getDouble("c1");
            double c2 = obj.getDouble("c2");
            double inertia =obj.getDouble("inertia");
            String config = obj.getString("configuration");

            ParticleSwarmOptimization PSO = new ParticleSwarmOptimization(particleNums, minVelocity,maxVelocity, c1,
                    c2, inertia, config, max_iterations);

            PSO.execute();


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void runSA(String name) {
        try{
            // find file name and run
            FileReader file = new FileReader("C:/Users/Shawn Cole/Documents/UCT '20/Evolutionary " +
                    "Computing/Assignment/sa_default/" + name + ".json");
            BufferedReader br = new BufferedReader(file);

            // JSON is all on 1 line
            String json = br.readLine();
            System.out.println(json);

            JSONObject obj = new JSONObject(json);
            double temperature = Double.parseDouble(obj.getString("initial_temperature"));
            double coolRate = Double.parseDouble(obj.getString("cooling_rate"));
            String config = obj.getString("configuration");

            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(temperature, coolRate, config);
            simulatedAnnealing.execute();


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void runGA(String name) {
        ;
    }

    private static void PopulateItems(int size) {
        Items = new item[size];
        try{
            FileReader file = new FileReader("C:/Users/Shawn Cole/Documents/UCT '20/Evolutionary " +
                    "Computing/Assignment/data/knapsack_instance.csv");
            BufferedReader br = new BufferedReader(file);
            // Skip column headers
            String[] line = br.readLine().split(";");

            for (int i = 0; i<Items.length; i++){
                line = br.readLine().split(";");
                int w = Integer.parseInt(line[1]);
                int v = Integer.parseInt(line[2]);

                Items[i] = new item(i, w, v);
                //System.out.printf("Item created with Id: %d, weight: %d, value: %d\n", i, w, v); //trace
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

import org.json.JSONObject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Driver {
    public static item[] Items;
    public static final int max_iterations = 10000;
    public static final int num_of_items = 150;
    public static final int max_capacity = 822;
    public static final int best_optimum = 997;

    public static void main (String[] args) {

        PopulateItems(150);
        //System.out.println(Items[0].toString());
        //System.out.println(Items[149].toString());


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
        try{
            // find file name and run
            FileReader file = new FileReader("C:/Users/Shawn Cole/Documents/UCT '20/Evolutionary " +
                    "Computing/Assignment/ga_default/" + name + ".json");
            BufferedReader br = new BufferedReader(file);

            // JSON is all on 1 line
            String json = br.readLine();
            System.out.println(json);

            JSONObject obj = new JSONObject(json);
            String selection_method = obj.getString("selection_method");
            double mutation_ratio = obj.getDouble("mutation_ratio");
            double crossover_ratio =obj.getDouble("crossover_ratio");
            String crossover_method = obj.getString("crossover_method");
            String mutation_method =obj.getString("mutation_method");
            String config = obj.getString("configuration");

            GeneticAlgorithm GA = new GeneticAlgorithm(selection_method, mutation_ratio, crossover_ratio,
                    crossover_method, mutation_method, config, max_iterations);
            GA.execute();

        }catch(IOException e){
            e.printStackTrace();
        }
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
            } // end for
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
        } // end else

    } // generateReport
}

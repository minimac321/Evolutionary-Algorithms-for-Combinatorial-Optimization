import org.json.JSONObject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;

public class Driver {
    public static item[] Items;
    public static final int max_iterations = 10000;
    public static final int num_of_items = 150;
    public static final int max_capacity = 822;
    public static final int best_optimum = 997;

    public static JSONObject obj;

    public static void main (String[] args) throws IOException {
        SolutionInstance result = null;

        PopulateItems(150);

        if (args[0].equals("-configuration")){

            String name = args[1] ;
            if (!name.contains(".json")){
                System.out.println("File error, Try add .json to the file name");
                System.exit(0);
            }
            name = name.substring(0, name.length()-5);

            System.out.printf("Loading and running: %s now\n", name);

            if (name.contains("ga")){
                result = runGA(name, true);
            }
            else if (name.contains("sa")){
                result = runSA(name, true);
            }
            else if (name.contains("pso")){
                result = runPSO(name, true);
            }


        }else if(args[0].equals("-search_best_configuration")){
            String algorithm_name = args[1];
            int num_of_cases = 0;

            if (algorithm_name.equals("ga")) num_of_cases = 28;
            else num_of_cases = 25;

            // Run all possible configurations and identity best
            //System.out.printf("Running all default %s configurations\n", algorithm_name);
            ArrayList<SolutionInstance> bestResults = new ArrayList<>();

            for (int test = 1; test <= num_of_cases; test++) {
                String num_value = "";
                if (test < 10) num_value += "0";
                num_value += String.valueOf(test);
                String name = algorithm_name + "_default_" + num_value;

                System.out.printf("Loading and running: %s now\n", name);

                if (algorithm_name.equals("ga")){
                    result = runGA(name, true);
                    bestResults.add(result);
                }
                else if (algorithm_name.equals("sa")){
                    result = runSA(name, true);
                    bestResults.add(result);
                }
                else if (algorithm_name.equals("pso")){
                    result = runPSO(name, true);
                    bestResults.add(result);
                }
            }

            int max = Integer.MIN_VALUE;
            int iPos = -1;
            int[] arr_final = new int[bestResults.size()];
            for (int i = 0; i < bestResults.size(); i++) {
                if (max < bestResults.get(i).fitness){
                    max = bestResults.get(i).fitness;
                    iPos = i;
                }
                arr_final[i] = bestResults.get(i).fitness;
            }

            String num = "";
            if (iPos+1 < 10) num += "0";
            num += String.valueOf(iPos+1);


            System.out.printf("Best of each: %s\n", Arrays.toString(arr_final));
            System.out.printf("Best Solution is: %s\nScore - %d\n", num, max);

            makeBestJSONFile(algorithm_name, num);
        }
        else{
            System.out.println("Invalid Input");
            System.exit(0);
        }

    }

    public static void makeBestJSONFile(String algorithm_name, String number) {
        try{
            FileReader file = new FileReader("C:/Users/Shawn Cole/Documents/UCT '20/Evolutionary " +
                    "Computing/Assignment/" + algorithm_name + "_default/" + algorithm_name + "_default_" + number + ".json");
            BufferedReader br = new BufferedReader(file);

            String json = br.readLine();
            obj = new JSONObject(json);

            FileWriter f = new FileWriter("Best_Output/"+ algorithm_name + "_best.json");

            f.write(obj.toString());
            f.close();
            System.out.println("System printed: " + obj.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static SolutionInstance runPSO(String name, boolean generateReport) {
        try{
            // find file name and run
            FileReader file = new FileReader("C:/Users/Shawn Cole/Documents/UCT '20/Evolutionary " +
                    "Computing/Assignment/pso_default/" + name + ".json");
            BufferedReader br = new BufferedReader(file);

            // JSON is all on 1 line
            String json = br.readLine();
            //System.out.println(json);

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
            SolutionInstance instance = PSO.execute(generateReport);
            return instance;

        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("PSO - Error in Returning a final Solution Instance");
        return null;
    }

    private static SolutionInstance runSA(String name, boolean generateReport) {
        try{
            // find file name and run
            FileReader file = new FileReader("C:/Users/Shawn Cole/Documents/UCT '20/Evolutionary " +
                    "Computing/Assignment/sa_default/" + name + ".json");
            BufferedReader br = new BufferedReader(file);

            // JSON is all on 1 line
            String json = br.readLine();
            //System.out.println(json);

            JSONObject obj = new JSONObject(json);
            double temperature = Double.parseDouble(obj.getString("initial_temperature"));
            double coolRate = Double.parseDouble(obj.getString("cooling_rate"));
            String config = obj.getString("configuration");

            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(temperature, coolRate, config);

            SolutionInstance instance = simulatedAnnealing.execute(generateReport);
            return instance;

        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("SA - Error in Returning a final Solution Instance");
        return null;
    }

    private static SolutionInstance runGA(String name, boolean generateReport) {
        try{
            // find file name and run
            FileReader file = new FileReader("C:/Users/Shawn Cole/Documents/UCT '20/Evolutionary " +
                    "Computing/Assignment/ga_default/" + name + ".json");
            BufferedReader br = new BufferedReader(file);

            // JSON is all on 1 line
            String json = br.readLine();
            //System.out.println(json);

            JSONObject obj = new JSONObject(json);
            String selection_method = obj.getString("selection_method");
            double mutation_ratio = obj.getDouble("mutation_ratio");
            double crossover_ratio =obj.getDouble("crossover_ratio");
            String crossover_method = obj.getString("crossover_method");
            String mutation_method =obj.getString("mutation_method");
            String config = obj.getString("configuration");

            GeneticAlgorithm GA = new GeneticAlgorithm(selection_method, mutation_ratio, crossover_ratio,
                    crossover_method, mutation_method, config, max_iterations);
            SolutionInstance instance = GA.execute(generateReport);
            return instance;

        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("GA - Error in Returning a final Solution Instance");
        return null;
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

}

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SimulatedAnnealing{
    public SolutionInstance currentInstance;
    public SolutionInstance bestInstance;
    public double maxTemp;
    public double coolRate;
    public String config;
    public String reportString;

    public final double minTemp = 1;

    // Random Number Generator
    public static MersenneTwister randomGenerator = new MersenneTwister();

    public ArrayList<SolutionInstance> report_array;


    public SimulatedAnnealing(double maxTemp, double coolRate, String config){
        report_array = new ArrayList<>();
        this.maxTemp = maxTemp;
        this.coolRate = coolRate;
        this.config = config;
        this.reportString =
                "SA  |  Max Temp (" + String.valueOf(maxTemp) + ")  |  Cooling Rate (" + String.valueOf(coolRate) + ")";
    }

    public SolutionInstance execute(boolean generateReport) throws IOException {
        double temperature = maxTemp;

        currentInstance = generateRandomInstance(0.2);

        try {
            bestInstance = currentInstance.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        int iCounter = 0;
        long runtimeStart = System.currentTimeMillis();

        // I added this loop in addition because just the SA algorithm only got values from
        // 500 - 700. This just repeats the algorithm using the best found optimum of the previous
        // loop as the starting Solution instance.
        while (iCounter < Driver.max_iterations) {

            while (temperature > minTemp) {
                //System.out.printf("Current Temp %5.2f\n", temperature);
                SolutionInstance neighbour = getNeighbourInstance(currentInstance);
                //System.out.printf("Neighbour weight - %d, fitness - %d\n", neighbour.weight, neighbour.fitness);

                double currentFitness = currentInstance.calculateFitness();
                double neighbourFitness = neighbour.calculateFitness();
                double delta = currentFitness - neighbourFitness;
                //System.out.printf("Current Fitness - %f, Neighbour Fitness - %f, Delta Value - %f, Temp - %5f\n",
                // currentFitness, neighbourFitness, delta, temperature);

                // If new is greater than old; replace old by new
                if (delta <= 0) {
                    try {
                        currentInstance = neighbour.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (AcceptanceProbability(currentFitness, neighbourFitness, temperature) >= randomGenerator.nextDouble()) {
                        try {
                            currentInstance = neighbour.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                }
                if (currentInstance.calculateFitness() > bestInstance.calculateFitness()) {
                    try {
                        bestInstance = currentInstance.clone();
                        //System.out.printf("New Best: temperature = %5.2f | weight = %5d | fitness = %5d \n",
                        // temperature , bestInstance.calculateWeight(), bestInstance.fitness );
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    report_array.add(bestInstance.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                //System.out.printf("Best Solution: Fitness - %d, Weight - %d\n", bestInstance.fitness, bestInstance
                // .weight);
                temperature = temperature * coolRate;
                iCounter++;
            }
            try {
                currentInstance = bestInstance.clone();
                temperature = maxTemp;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } // End for
        long totalTime = System.currentTimeMillis() - runtimeStart;

        ArrayList<SolutionInstance> arr_report = new ArrayList<>(Driver.max_iterations);
        for (int q = 0; q < Driver.max_iterations; q++) {
            arr_report.add(report_array.get(q));
        }
        SolutionInstance[] list_reportArray = arr_report.toArray(new SolutionInstance[arr_report.size()]);
        new Report(generateReport, config, reportString, list_reportArray, totalTime);
        return bestInstance;
    }

    public SolutionInstance generateRandomInstance(double startThreshold) {
        SolutionInstance s = new SolutionInstance(Driver.Items, Driver.num_of_items, Driver.max_capacity);
        boolean[] pos = new boolean[Driver.num_of_items];
        Arrays.fill(pos, false);

        double thresh = startThreshold;
        do {
            for (int c = 0; c < Driver.num_of_items; c++) {
                if (SimulatedAnnealing.randomGenerator.nextDouble() < thresh) pos[c] = true;
                else pos[c] = false;
            }
            s.setPosition(pos);
            thresh = Math.max(0, thresh*0.9);
        }while (s.isTooHeavy());
        return s;
    }

    public double AcceptanceProbability(double currentFitness, double neighbourFitness, double temperature) {
        double delta =  currentFitness - neighbourFitness;
        return Math.exp( -1*delta / temperature);
    }

    public SolutionInstance getNeighbourInstance(SolutionInstance s) {
        boolean[] bool_arr = s.Solution.clone();
        SolutionInstance neighbour = new SolutionInstance(s.Items, s.numItems, s.capacity, bool_arr);

        // Play with random algorithm - not just a single bit
        int iterations = randomGenerator.nextInt(1, 3);
        int bit;

        for (int i = 0; i < iterations; i++) {
            do {
                bit = randomGenerator.nextInt(0, 149);
            }while (neighbour.getBit(bit)) ;
            bool_arr[bit] = true;
        }
        neighbour.setPosition(bool_arr);

        while (neighbour.isTooHeavy()){
            bit = randomGenerator.nextInt(0, 149);
            bool_arr[bit] = false;
            neighbour.setPosition(bool_arr);
        }
        return neighbour;
    }
}
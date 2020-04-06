public class SimulatedAnnealing {

    public SolutionInstance currentInstance;
    public SolutionInstance bestInstance;
    public double maxTemp;
    public double coolRate;
    public String config;

    public final double minTemp = 1;

    // Random Number Generator
    public MersenneTwister randomGenerator = new MersenneTwister();

    public SimulatedAnnealing(double maxTemp, double coolRate, String config){
        this.maxTemp = maxTemp;
        this.coolRate = coolRate;
        this.config = config;
    }

    public int getEnergy(SolutionInstance x) {
        return x.calculateFitness();
    }

    public int getWeight(SolutionInstance x) {
        return x.calculateWeight();
    }


    public double AcceptanceProbability(double OldEnergy, double newEnergy, double temperature) {
        double minus_delta = newEnergy - OldEnergy;
        return Math.exp( minus_delta / temperature);
    }

    // We want to find the highest fitness, while still having a low enough weight
    // fitness ~= energy
    public void execute(){
        double temperature = maxTemp;

        // Make a starting instance for
        currentInstance = new SolutionInstance(Driver.Items, 150, 822);
        System.out.println("Initial Solution: " + currentInstance.toString());

        try {
            bestInstance = currentInstance.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        int iCounter = 0;
        System.out.println("\nStart S.A.");
        while (temperature > minTemp) {
            iCounter ++;
            System.out.printf("Current Temp %5.2f\n", temperature);

            SolutionInstance nextInstance = getRandomInstance(currentInstance);
            System.out.println("Random Instance: " + nextInstance.toString());

            double actualEnergy = getEnergy(currentInstance);
            double newEnergy = getEnergy(nextInstance);
            double delta = actualEnergy - newEnergy;

            // If new is greater than old; replace old by new
            if (delta <= 0){
                try {
                    currentInstance = nextInstance.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }else{

                if (AcceptanceProbability(actualEnergy, newEnergy, temperature) >= randomGenerator.nextDouble()) {
                    try {
                        currentInstance = nextInstance.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (getEnergy(currentInstance) > getEnergy(bestInstance)) {
                try {
                    bestInstance = currentInstance.clone();
                    System.out.printf("New Best: temperature = %5.2f | weight = %5d | fitness = %5d \n", temperature,
                            bestInstance.calculateWeight(), bestInstance.fitness );
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

            }

            temperature -= temperature *(1 - coolRate);
        }

        System.out.printf("Finished with Simulation after %d iterations\n", iCounter);
        System.out.printf("Optimal Output: temp = %5.2f | weight = %5d | fitness = %5d \n", temperature,
                bestInstance.calculateWeight(), bestInstance.fitness );
        System.out.println(bestInstance.toString());



    }

    public SolutionInstance getRandomInstance(SolutionInstance s) {
        boolean[] previousArr = s.getBoolArray();
        SolutionInstance neighbour = new SolutionInstance(s.Items, s.numItems, s.capacity, previousArr);

        // Get random Bit
        MersenneTwister mt = new MersenneTwister(System.currentTimeMillis());
        int bit = mt.nextInt(0, 149);

        if (neighbour.Solution[bit]){
            neighbour.Solution[bit] = false;
        }
        else {
            neighbour.Solution[bit] = true;
        }
        neighbour.calculateFitness();
        neighbour.calculateWeight();


        while (neighbour.isTooHeavy()){
            System.out.println("Is Too Heavy");
            bit = mt.nextInt(0, 149);
            if (neighbour.Solution[bit]) {
                neighbour.Solution[bit] = false;
            }

            neighbour.calculateWeight();
        }

        return neighbour;
    }
}
import java.io.IOException;
import java.util.Arrays;

public class GeneticAlgorithm {
    public static MersenneTwister randomGenerator = new MersenneTwister();

    double currentBestFitness = Integer.MIN_VALUE;
    public Chromosome[] population;
    private int numberOfCrossoverOperations = 0;
    private int numberOfMutationOperations = 0;

    public String selectionMethod;
    public double mutation_ratio;
    public double crossover_ratio;
    public String crossover_method;
    public String mutation_method;
    public String config;
    public String reportString;
    public int maxIterations;

    // What is this used for ??
    private double elitismRatio;

    public SolutionInstance[] report_array = new SolutionInstance[Driver.max_iterations];

    public GeneticAlgorithm(String selectionMethod, double mutation_ratio, double crossover_ratio,
                            String crossover_method, String mutation_method, String config, int max_iterations){
        this.selectionMethod = selectionMethod;
        this.mutation_ratio = mutation_ratio;
        this.crossover_ratio = crossover_ratio;
        this.crossover_method = crossover_method;
        this.mutation_method = mutation_method;

        this.config = config;
        this.maxIterations = max_iterations;

        this.reportString =
                "GA  |  #10000  |  " + selectionMethod + "  |  " + crossover_method + " (" + String.valueOf(crossover_ratio)+
                        ")   |  " + mutation_method +  " (" + String.valueOf(mutation_ratio) + ")";

        population = new Chromosome[Driver.num_of_items];
    }

    public void RandomInitialization() {
        for (int i = 0; i < population.length; i++) {
            population[i] = new Chromosome(new SolutionInstance(Driver.Items, Driver.num_of_items, Driver.max_capacity));;
            population[i].generateRandom(0.25); // Make zero for Zero Initializer
        }
    }

    public SolutionInstance execute(boolean generateReport) throws IOException {
        RandomInitialization();

        // Sort DESCENDING array according to fitness => comparable already added
        printPopulation(population);
        Arrays.sort(population);
        printPopulation(population);

        Chromosome bestChromosome = null;
        try {
            int j = 0;
            do{
                bestChromosome = population[j].clone();
                bestChromosome.updateMetrics();
                if (j>1) System.out.println("Zero value @ start");
            }while (population[j++].getFitness() == 0);

            currentBestFitness = bestChromosome.getFitness();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        long runtimeStart = System.currentTimeMillis();
        int iter = 0;

        while ( iter < Driver.max_iterations ) {
            System.out.printf("Best Chromosome: %d\n", bestChromosome.getFitness());
            try {
                report_array[iter] = bestChromosome.gene.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            population = evolve();
            Arrays.sort(population); // sorts descending

            try {
                int j = 0;
                do{
                    bestChromosome = population[j].clone();
                    if (j>1) System.out.println("Zero value @  in While loop");
                }while (population[j++].getFitness() == 0);

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                System.exit(0);
            }
            if (bestChromosome.getFitness() > currentBestFitness && bestChromosome.getWeight() <= Driver.max_capacity) {
                currentBestFitness = bestChromosome.getFitness();
                //System.out.printf("generation %d, Fitness - %d, Weight - %d\n", iter, bestChromosome.getFitness(),
                // bestChromosome.getWeight());
            }
            iter++;

        }// End While loop

        long totalTime = System.currentTimeMillis() - runtimeStart;

        //System.out.println("generation                  : " + String.valueOf(i));
        System.out.println("best fitness                : " + String.valueOf(bestChromosome.getFitness()) );
        //System.out.println("best weight                : " + String.valueOf(bestChromosome.getWeight()) );
        System.out.println("runtime                     : " + totalTime + " ms");
        System.out.println("numberOfCrossoverOperations : " + numberOfCrossoverOperations);
        System.out.println("numberOfMutationOperations  : " + numberOfMutationOperations);
        //System.out.println(bestChromosome.gene.toString());

        new Report(generateReport, config, reportString, report_array, totalTime);
        return bestChromosome.gene;
    }

    private void printPopulation(Chromosome[] population) {
        String s="";
        for (Chromosome c: population) {
            s+= String.valueOf(c.getFitness()) + ", ";
        }
        System.out.println(s);
    }


    // *******Make evolve tooHeavy safe and has no chromosomes that are too heavy***********
    public Chromosome[] evolve() {
        Chromosome[] new_Population = new Chromosome[population.length];
        int index = 20;

        int a = 0;
        while (a < index){
            try {
                new_Population[a] = population[a].clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            a++;
        }

        // Fill the rest of new_population with selection, crossover&mutation.
        while (index < population.length){

            // Check for cross-over or not => produce 2 children
            if (randomGenerator.nextDouble() < crossover_ratio){
                numberOfCrossoverOperations++;

                // Select Parents
                Chromosome[] parentArray = null;
                if (selectionMethod.equals("RWS")) {
                    parentArray = RouletteWheel.selectParents(population);
                } else {
                    parentArray = Tournament.selectParents(population, 30);
                }
                // recombine pairs of parents to make offspring
                Chromosome[] children = new Chromosome[2];
                if (crossover_method.equals("1PX"))
                {
                    int cross_point = randomGenerator.nextInt(1, 148);
                    children[0] = rollBack(OnePointX.cross(parentArray, cross_point, 0));
                    children[1] = rollBack(OnePointX.cross(parentArray, cross_point, 1));

                } else if (crossover_method.equals("2PX")){
                    int cross_point1 = randomGenerator.nextInt(1, 147);
                    int cross_point2 = randomGenerator.nextInt(cross_point1+1, 148);
                    children[0] = rollBack(TwoPointX.cross(parentArray, cross_point1, cross_point2, 0));
                    children[1] = rollBack(TwoPointX.cross(parentArray, cross_point1, cross_point2, 1));
                }
                else{
                    System.out.println("Input Error for GA");
                }

                // Mutate first child
                if (randomGenerator.nextDouble() < mutation_ratio) {
                    numberOfMutationOperations++;
                    Chromosome c = null;
                    if (mutation_method.equals("BFM")) {
                        c = Mutation.BFM(children[0]);
                    } else if (mutation_method.equals("EXM")) {
                        c = Mutation.EXM(children[0]) ;
                    } else if (mutation_method.equals("IVM")) {
                        c = Mutation.IVM(children[0]);
                    } else if (mutation_method.equals("ISM")) {
                        c = Mutation.ISM(children[0]);
                    } else if (mutation_method.equals("DPM")) {
                        c = Mutation.DPM(children[0]);
                    } else {
                        System.out.println("Mutation Error in String - 1");
                    }
                    new_Population[index] = rollBack(c);
                }else{
                    new_Population[index] = rollBack(children[0]);
                }
                index++;

                if (index < population.length){
                    // Mutate Second child
                    if (randomGenerator.nextDouble() < mutation_ratio) {
                        numberOfMutationOperations++;
                        Chromosome c = null;

                        if (mutation_method.equals("BFM")) {
                            c = Mutation.BFM(children[1]);
                        } else if (mutation_method.equals("EXM")) {
                            c = Mutation.EXM(children[1]);
                        } else if (mutation_method.equals("IVM")) {
                            c = Mutation.IVM(children[1]);
                        } else if (mutation_method.equals("ISM")) {
                            c = Mutation.ISM(children[1]);
                        } else if (mutation_method.equals("DPM")) {
                            c = Mutation.DPM(children[1]);
                        } else {
                            System.out.println("Mutation Error in String - 2");
                        }
                        new_Population[index] = rollBack(c);
                    }else{
                        new_Population[index] = rollBack(children[1]);
                    }
                }


            } else if (randomGenerator.nextDouble() < mutation_ratio){
                numberOfMutationOperations++;
                Chromosome c = null;

                if (mutation_method.equals("BFM")){
                    c = Mutation.BFM(population[index]);
                }
                else if (mutation_method.equals("EXM")){
                    c = Mutation.EXM(population[index]);
                }
                else if (mutation_method.equals("IVM")){
                    c = Mutation.IVM(population[index]);
                }
                else if (mutation_method.equals("ISM")){
                    c = Mutation.ISM(population[index]);
                }
                else if(mutation_method.equals("DPM")){
                    c = Mutation.DPM(population[index]);
                }
                else {
                    System.out.println("Mutation Error in String");
                }
                new_Population[index] = rollBack(c);
            }else{
                // In the case where no cross over or mutations performed
                new_Population[index] = population[index];
            }
            index++;
        } // end while loop

        for (Chromosome c: new_Population){
            c.updateMetrics();
        }

        return new_Population.clone();
    }

    public static Chromosome rollBack(Chromosome new_chromosome) {
        //return new_chromosome;
        new_chromosome.updateMetrics();
        if (new_chromosome.getWeight()<= 822) return new_chromosome;

        Chromosome rolledChromosome = new Chromosome(new SolutionInstance(Driver.Items, Driver.num_of_items,
                Driver.max_capacity, new_chromosome.gene.Solution));
        int count = 0;
        do{
            int rand = GeneticAlgorithm.randomGenerator.nextInt(0, 149);
            if (rolledChromosome.gene.getBit(rand) ){
                rolledChromosome.gene.Solution[rand] = false;
            }
            count++;
            rolledChromosome.updateMetrics();
        } while (rolledChromosome.getWeight() > 822);

        //System.out.printf("Rolled Back: %d times\n", count);
        return rolledChromosome;
    }

}

import java.text.DecimalFormat;
import java.util.Arrays;

public class GeneticAlgorithm {
    public static MersenneTwister randomGenerator = new MersenneTwister();
    public static DecimalFormat dFormat = new DecimalFormat("000000");

    double currentBestFitness = Double.MIN_VALUE;
    public Chromosome[] population;
    private int numberOfCrossoverOperations = 0;
    private int numberOfMutationOperations = 0;

    public String selectionMethod;
    public double mutation_ratio;
    public double crossover_ratio;
    public String crossover_method;
    public String mutation_method;
    public String config;
    public int maxIterations;

    // What is this used for ??
    private double elitismRatio;

    public GeneticAlgorithm(String selectionMethod, double mutation_ratio, double crossover_ratio,
                            String crossover_method, String mutation_method, String config, int max_iterations){
        this.selectionMethod = selectionMethod;
        this.mutation_ratio = mutation_ratio;
        this.crossover_ratio = crossover_ratio;
        this.crossover_method = crossover_method;
        this.mutation_method = mutation_method;

        this.config = config;
        this.maxIterations = max_iterations;

        population = new Chromosome[Driver.num_of_items];
    }

    public void RandomInitialization() {
        for (int i = 0; i < population.length; i++) {
            Chromosome chromosome = new Chromosome(new SolutionInstance(Driver.Items, Driver.num_of_items, Driver.max_capacity));
            population[i] = chromosome;
            population[i].generateRandom();
            population[i].updateMetrics();
        }
    }

    public void execute(){

        //System.out.println(config);
        RandomInitialization();

        // Sort DESCENDING array according to fitness => comparable already added
        Arrays.sort(population);

        Chromosome bestChromosome = population[0];
        currentBestFitness = bestChromosome.getFitness();
        System.out.printf("Best Fitness : %d\n", bestChromosome.getFitness());
        System.out.printf("Worst Fitness: %d\n", population[149].getFitness());

        long runtimeStart = System.currentTimeMillis();
        int i = 0;


        while ( (i++ <= Driver.max_iterations) && (bestChromosome.getFitness() != 0) ) {
            evolve();
            bestChromosome = population[0];
            if (bestChromosome.getFitness() > currentBestFitness ) {
                currentBestFitness = bestChromosome.getFitness();
                System.out.printf("generation %d, Fitness - %d\n", i, bestChromosome.getFitness());
            }
        }

        System.out.println("generation                  : " + String.valueOf(i) + " : " + bestChromosome.getGene());
        System.out.println("runtime                     : " + (System.currentTimeMillis() - runtimeStart) + " ms");
        System.out.println("numberOfCrossoverOperations : " + numberOfCrossoverOperations);
        System.out.println("numberOfMutationOperations  : " + numberOfMutationOperations);

    }

    // *******Make evolve tooHeavy safe and has no chromosomes that are too heavy***********
    public void evolve() {
        Chromosome[] new_Population = new Chromosome[population.length];

        double index_percentage = 0.1;
        int index = (int) Math.round(index_percentage * population.length);
        System.arraycopy(population, 0, new_Population, 0, index);

        while (index < population.length){

            // Check for cross-over or not
            if (randomGenerator.nextDouble() < crossover_ratio) {
                numberOfCrossoverOperations++;

                // Select Parents
                Chromosome[] parentArray = null;
                if (selectionMethod.equals("RWS")) {
                    parentArray = RouletteWheel.selectParents(population);
                } else {                                                              // Could be 40 ****
                    parentArray = Tournament.selectParents(population, 30);
                }
                // recombine pairs of parents to make offspring
                Chromosome[] children = new Chromosome[2];
                if (crossover_method.equals("1PX")) {
                    int cross_point = randomGenerator.nextInt(1, 148);
                    children = OnePointX.cross(parentArray, cross_point);
                }else{
                    int cross_point1 = randomGenerator.nextInt(1, 148);
                    int cross_point2 = randomGenerator.nextInt(1, 148);
                    if (cross_point1 < cross_point2) children = TwoPointX.cross(parentArray, cross_point1, cross_point2);
                    else children = TwoPointX.cross(parentArray, cross_point2, cross_point1);
                }
            }

            if (randomGenerator.nextDouble() < crossover_ratio){
                numberOfMutationOperations++;

                // mutate resulting offspring
                if (randomGenerator.nextDouble() < mutation_ratio){

                }else{
                    //new_Population[index]
                }
            }
            //evaluate new candidates
            //select individuals for next generation

            // Offspring production

            //Offspring mutation
            if (randomGenerator.nextDouble() < mutation_ratio){
                numberOfMutationOperations++;
                Chromosome c = null;

                if (mutation_method.equals("BFM")){
                    c = Mutation.BFM(population[index]);
                    new_Population[index] = c;
                }
                else if (mutation_method.equals("EXM")){
                    c = Mutation.EXM(population[index]);
                    new_Population[index] = c;
                }
                else if (mutation_method.equals("IVM")){
                    c = Mutation.IVM(population[index]);
                    new_Population[index] = c;
                }
                else if (mutation_method.equals("ISM")){
                    c = Mutation.ISM(population[index]);
                    new_Population[index] = c;
                }
                else if (mutation_method.equals("DPM")){
                    c = Mutation.DPM(population[index]);
                    new_Population[index] = c;
                }

            }else{
                new_Population[index] = population[index];
            }


            index++;
        }

        population = new_Population;

    }

//    private Chromosome[] selectParents() {
//        Chromosome[] parentArray = new Chromosome[2];
//
//        for (int i = 0; i < 2; i++) {
//            parentArray[i] = population[Configuration.instance.randomGenerator.nextInt(population.length)];
//            for (int j = 0; j < 3; j++) {
//                int index = Configuration.instance.randomGenerator.nextInt(population.length);
//                if (population[index].compareTo(parentArray[i]) < 0) {
//                    parentArray[i] = population[index];
//                }
//            }
//        }
//
//        return parentArray;
//    }

}

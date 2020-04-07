
public class RouletteWheel {

    public static Chromosome[] selectParents(Chromosome[] population) {
        Chromosome[] parents = new Chromosome[2];

        int totalSum = 0;
        double totalSum_probabilities = 0;

        for (Chromosome c : population) {
            totalSum+=c.getFitness();
        }
        // Creates a monotonic increasing function
        double[] roulette = populateRoulette(population, totalSum);

        double rand1 = GeneticAlgorithm.randomGenerator.nextDouble();
        double rand2 = GeneticAlgorithm.randomGenerator.nextDouble();
        // Choose first parent
        for (int r = 0; r < roulette.length; r++) {
            if (rand1 < roulette[r]){
                parents[0] = population[r];
                break;
            }
        }
        // Choose second parent
        for (int r = 0; r < roulette.length; r++) {
            if (rand2 < roulette[r]){
                parents[1] = population[r];
                break;
            }
        }
        return parents;
    }
    // create array for cumulative probabilities
    private static double[] populateRoulette(Chromosome[] population, int totalSum) {
        double[] roulette = new double[population.length];
        double cumulative = 0;
        double chances;

        for (int i = 0; i < population.length; i++) {
            chances     = cumulative + (double) (population[i].getFitness() / totalSum);
            roulette[i] = chances;
            cumulative += chances;
        }
        return roulette;
    }
}

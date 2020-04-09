import java.util.ArrayList;
import java.util.Arrays;

public class RouletteWheel {

    public static Chromosome[] selectParents(Chromosome[] population) {
        Chromosome[] parents = new Chromosome[2];
        int totalSum = 0;

        for (Chromosome c: population) {
            totalSum += c.getFitness();
        }
        // Creates a monotonic decreasing function
        double[] roulette = populateRoulette(population, totalSum);
        // Make it increasing
        Arrays.sort(roulette);
        //System.out.println(Arrays.toString(roulette));

        double rand1 = GeneticAlgorithm.randomGenerator.nextDouble();
        double rand2 = GeneticAlgorithm.randomGenerator.nextDouble();
        // Choose first parent
        for (int r = 0; r < roulette.length; r++) {
            if (rand1 <= roulette[r]){
                //System.out.println(population[r]);
                parents[0] = population[r];
                break;
            }
        }
        // Choose second parent
        for (int d = 0; d < roulette.length; d++) {
            if (rand2 <= roulette[d]){
                //System.out.println(population[d]);
                parents[1] = population[d];
                break;
            }
        }

        // Found NextDouble produced 1 which was bigger than 0.999999 sometime
        if (parents[0] == null || parents[1] == null) {
            System.out.println("Null error @ RWS");

            System.out.printf("Size of population: %d\n", population.length);
            System.out.printf("Rand1: %f\n", rand1);
            System.out.printf("Rand2: %f\n", rand2);
            System.out.println(Arrays.toString(roulette));
            System.exit(0);
        }


        return parents;
    }
    // create array for cumulative probabilities
    private static double[] populateRoulette(Chromosome[] population, int totalSum) {
        double[] roulette = new double[population.length];
        float cumulative = 0;
        double chances;

        for (int i = 0; i < population.length; i++) {
            chances     =  (population[i].getFitness()*1.0 / totalSum*1.0) ;
            //System.out.printf("%2.6f / %d  = %2.6f\n", population[i].getFitness()*1.0, totalSum, chances);
            cumulative += chances;
            roulette[i] = cumulative;
        }
        roulette[149] = 1;
        return roulette;
    }
}

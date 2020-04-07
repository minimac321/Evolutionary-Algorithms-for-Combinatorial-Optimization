public class Tournament {
    public static Chromosome[] selectParents(Chromosome[] population, int num_parents) {
        Chromosome[] parents = new Chromosome[2];

        // Get first parent
        Chromosome best = null;
        // Choose best out of given number of parents = 20%
        for (int i = 0; i < num_parents; i++) {
            int rand_value1 = GeneticAlgorithm.randomGenerator.nextInt(0, 149);
            Chromosome c1 = population[rand_value1];

            if (best == null || (c1.getFitness() > best.getFitness())){
                best = c1;
            }
            
        }
        parents[0] = best;

        // Get second parent
        best = null;
        for (int j = 0; j < num_parents; j++) {
            int rand_value2 = GeneticAlgorithm.randomGenerator.nextInt(0, 149);
            Chromosome c2 = population[rand_value2];

            if (best == null || (c2.getFitness() > best.getFitness())){
                best = c2;
            }

        }
        parents[1] = best;

        return parents;
    } // end select parent
}

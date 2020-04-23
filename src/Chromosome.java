import java.util.Arrays;

public class Chromosome implements Comparable<Chromosome>, Cloneable {
    public SolutionInstance gene;
    public String Position;
    private int fitness;
    private int weight;

    public Chromosome(SolutionInstance gene) {
        try {
            this.gene = gene.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Position = gene.stringSolution();
        fitness = gene.calculateFitness();
        weight = gene.calculateWeight();
    }

    @Override
    protected Chromosome clone() throws CloneNotSupportedException {
        return (Chromosome) super.clone();
    }

    public void generateRandom(double threshold) {
        do{
            boolean[] pos = new boolean[Driver.num_of_items];
            Arrays.fill(pos, false);

            for(int c = 0; c < Driver.num_of_items; c++) {
                if (GeneticAlgorithm.randomGenerator.nextDouble() < threshold) pos[c] = true;
                else pos[c] = false;
            }
            this.gene.setPosition(pos);
        } while (this.gene.isTooHeavy());

    }

    public void updateMetrics(){
        weight  = this.gene.calculateWeight();
        fitness = this.gene.calculateFitness();
    }

    public String getGene() {
        return gene.stringSolution();
    }

    public int getFitness() {
        return gene.fitness;
    }

    public int getWeight() {
        return gene.weight;
    }

    public int compareTo(Chromosome chromosome) {
        // Descending sorting, flipped minus sign
        if (this.fitness < chromosome.fitness) {
            return 1;
        }
        if (this.fitness > chromosome.fitness) {
            return -1;
        }
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Chromosome)) {
            return false;
        }

        Chromosome chromosome = (Chromosome) o;

        return (gene.equals(chromosome.gene)) && (fitness == chromosome.fitness);
    }

}
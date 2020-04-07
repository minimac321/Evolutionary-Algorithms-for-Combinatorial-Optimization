import java.util.Arrays;

public class Chromosome implements Comparable<Chromosome> {
    public SolutionInstance gene;
    public String Position;
    private int fitness;
    private int weight;

    public Chromosome(SolutionInstance gene) {
        this.gene = gene;
        Position = gene.stringSolution();
        fitness = gene.calculateFitness();
    }

    public void generateRandom() {
        boolean[] pos = new boolean[Driver.num_of_items];
        Arrays.fill(pos, false);

        for(int c = 0; c < Driver.num_of_items; c++) {
            if (GeneticAlgorithm.randomGenerator.nextDouble() < 0.2) pos[c] = true;
            else pos[c] = false;
        }
        this.gene.setPosition(pos);
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
        return gene.totalWeight;
    }

//    public Chromosome[] doCrossover(Chromosome chromosome) {
//        char[] charArray01 = gene.toCharArray();
//        char[] charArray02 = chromosome.gene.toCharArray();
//
//        int pivot = Configuration.instance.randomGenerator.nextInt(charArray01.length);
//        char[] child01 = new char[gene.length()];
//        char[] child02 = new char[gene.length()];
//
//        System.arraycopy(charArray01, 0, child01, 0, pivot);
//        System.arraycopy(charArray02, pivot, child01, pivot, child01.length - pivot);
//        System.arraycopy(charArray02, 0, child02, 0, pivot);
//        System.arraycopy(charArray01, pivot, child02, pivot, child02.length - pivot);
//
//        return new Chromosome[]{new Chromosome(String.valueOf(child01)),
//                new Chromosome(String.valueOf(child02))};
//    }
//
//    public Chromosome doMutation() {
//        char[] charArray = this.gene.toCharArray();
//        int index = Configuration.instance.randomGenerator.nextInt(charArray.length);
//        int delta = Configuration.instance.randomGenerator.nextInt() % 90 + 32;
//        charArray[index] = ((char) ((charArray[index] + delta) % 122));
//        return new Chromosome(String.valueOf(charArray));
//    }

    public int compareTo(Chromosome chromosome) {
        // Descending sorting, flipped minus sign
        if (this.fitness < chromosome.fitness) {
            return 1;
        }
        if (this.fitness > chromosome.gene.fitness) {
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
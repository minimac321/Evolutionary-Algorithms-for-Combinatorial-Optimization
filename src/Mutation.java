import java.util.ArrayList;
import java.util.Arrays;

public class Mutation {


    public static Chromosome BFM(Chromosome chromosome) {
        Chromosome new_chromosome = null;
        SolutionInstance s = null;
        boolean[] bfm = chromosome.gene.Solution;
        int size = chromosome.Position.length();

        int random = GeneticAlgorithm.randomGenerator.nextInt(0, size-1);
        bfm[random] = flip(bfm[random]);

        try {
            s = chromosome.gene.clone();
            s.setPosition(bfm);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        new_chromosome = new Chromosome(s);

        return new_chromosome;
    }

    public static Chromosome EXM(Chromosome chromosome) {
        Chromosome new_chromosome = null;
        SolutionInstance s = null;

        boolean[] exm = chromosome.gene.Solution;
        int size = chromosome.Position.length();
        int random1 = GeneticAlgorithm.randomGenerator.nextInt(0, size-1);
        int random2 = GeneticAlgorithm.randomGenerator.nextInt(0, size-1);

        boolean bTmp =  exm[random1];
        exm[random1] = exm[random2];
        exm[random2] = bTmp;

        try {
            s = chromosome.gene.clone();
            s.setPosition(exm);
            new_chromosome = new Chromosome(new SolutionInstance(Driver.Items, Driver.num_of_items,
                    Driver.max_capacity, s.Solution));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return new_chromosome;
    }

    public static Chromosome IVM(Chromosome chromosome) {
        Chromosome new_chromosome = null;
        SolutionInstance s = null;
        boolean[] ivm = chromosome.gene.Solution;
        int size = chromosome.Position.length();

        int random1 = GeneticAlgorithm.randomGenerator.nextInt(0, size-1);
        int random2 = GeneticAlgorithm.randomGenerator.nextInt(0, size-1);

        if (random1 > random2){
            int tmp = random2;
            random2 = random1;
            random1 = tmp;
        }

        for (int i = random1+1; i < random2 ; i++) {
            ivm[i] = flip(ivm[i]);
        }

        try {
            s = chromosome.gene.clone();
            s.setPosition(ivm);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        new_chromosome = new Chromosome(s);

        return new_chromosome;
    }

    // Moves a randomly chosen allen to another randomly chosen place
    public static Chromosome ISM(Chromosome chromosome) {
        Chromosome new_chromosome = null;
        SolutionInstance s = null;
        boolean[] ism = chromosome.gene.Solution;

        ArrayList<Boolean> inbetween = convertBoolean(ism);

        int size = chromosome.Position.length();
        int random1 = GeneticAlgorithm.randomGenerator.nextInt(0, size - 1);
        int random2 = GeneticAlgorithm.randomGenerator.nextInt(0, size - 2);

        if (random1 > random2) {
            int tmp = random2;
            random2 = random1;
            random1 = tmp;
        }
        // Handles shifts
        inbetween.add(random1, inbetween.get(random2));
        inbetween.remove(random2 + 1);

        ism = convert_boolean(inbetween);

        try {
            s = chromosome.gene.clone();
            s.setPosition(ism);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        new_chromosome = new Chromosome(s);

        return new_chromosome;
    }

    // Displacement mutation inserts a random string of genes in another random place
    // Displace between 1 - 10 alleles
    public static Chromosome DPM(Chromosome chromosome) {
        Chromosome new_chromosome = null;
        SolutionInstance s = null;
        boolean[] dpm = chromosome.gene.Solution;
        int size = chromosome.Position.length();

        ArrayList<Boolean> inbetween = convertBoolean(dpm);
        int random1 = GeneticAlgorithm.randomGenerator.nextInt(0, size - 1);
        int random2 = GeneticAlgorithm.randomGenerator.nextInt(0, size - 1);

        if (random1 > random2) {
            int tmp = random2;
            random2 = random1;
            random1 = tmp;
        }

        ArrayList<Boolean> subArray = new ArrayList<Boolean>(inbetween.subList(random1, random2 + 1));
        inbetween.remove(subArray);

        int randomIndex = GeneticAlgorithm.randomGenerator.nextInt(0, inbetween.size() - 1);
        inbetween.addAll(randomIndex, subArray);

        dpm = convert_boolean(inbetween);
        try {
            s = chromosome.gene.clone();
            s.setPosition(dpm);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        new_chromosome = new Chromosome(s);
        return new_chromosome;
    }
    public static boolean flip(boolean b){
        if (b) return false;
        else return true;
    }

    // Questions here??
    public static ArrayList<Boolean> convertBoolean(boolean[] b){
        ArrayList<Boolean> arr = new ArrayList<Boolean>();
        for (boolean val : b) {
            arr.add( Boolean.valueOf(String.valueOf(b)) );
        }
        return arr;
    }

    private static boolean[] convert_boolean(ArrayList<Boolean> inbetween) {
        boolean[] arr = new boolean[inbetween.size()];
        for (int i = 0; i < inbetween.size() ; i++) {
            arr[i] = inbetween.get(i);
        }
        return arr;
    }
}

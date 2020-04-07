import java.util.Arrays;

public class OnePointX {
    public static Chromosome[] cross(Chromosome[] parentArray, int cross_point) {
        Chromosome[] Children = new Chromosome[2];

        boolean[] p1 = parentArray[0].gene.Solution;
        boolean[] p2 = parentArray[1].gene.Solution;
        boolean[] child1 = new boolean[150];
        boolean[] child2 = new boolean[150];

        for (int i = 0; i < cross_point; i++) {
            child1[i] = p1[i];
            child2[i] = p2[i];
        }

        for (int i = cross_point; i < 150; i++) {
            child1[i] = p2[i];
            child2[i] = p1[i];
        }
        Children[0] = new Chromosome(new SolutionInstance(Driver.Items, Driver.num_of_items, Driver.max_capacity,
                child1));
        Children[1] = new Chromosome(new SolutionInstance(Driver.Items, Driver.num_of_items, Driver.max_capacity,
                child2));

        return Children;
    }
}

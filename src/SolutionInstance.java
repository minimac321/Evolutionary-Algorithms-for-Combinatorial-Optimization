import java.util.Arrays;

public class SolutionInstance implements Cloneable {

    public item[] Items;
    public int numItems;
    public int capacity;
    public boolean[] Solution;

    public int totalWeight;
    public int fitness;

    public SolutionInstance(){

    }

    public SolutionInstance(item[] Items, int numItems, int capacity){
        this.Items = Items;
        this.numItems = numItems;
        this.capacity = capacity;
        this.Solution = new boolean[numItems];

        Arrays.fill(Solution, false);

        totalWeight = calculateWeight();
        fitness = calculateFitness();
    }


    public SolutionInstance(item[] Items, int numItems, int capacity, boolean[] b){
        this.Items = Items;
        this.numItems = numItems;
        this.capacity = capacity;
        this.Solution = new boolean[numItems];

        Solution = b;

        totalWeight = calculateWeight();
        fitness = calculateFitness();
    }

//    public SolutionInstance(SolutionInstance s){
//        this.Items = s.Items;
//        this.numItems = s.numItems;
//        this.capacity = s.capacity;
//        this.Solution = s.Solution;
//
//        totalWeight = calculateWeight();
//        fitness = calculateFitness();
//    }


    public boolean getBit(int iPos){
        return Solution[iPos];
    }

    public boolean[] getBoolArray(){
        return Solution;
    }


    @Override
    protected SolutionInstance clone() throws CloneNotSupportedException {
        return (SolutionInstance) super.clone();
    }

    public boolean isTooHeavy(){
        return (totalWeight >= capacity);
    }

    public int calculateWeight() {
        int weight = 0;

        for (int i = 0; i < numItems; i++) {
            if (Solution[i] == true) {
                weight += Items[i].getWeight();
            }
        }
        totalWeight = weight;
        return weight;
    }

    public int calculateFitness() {
        int profit = 0;

        for (int i = 0; i < numItems; i++) {
            if (Solution[i]) profit += Items[i].getValue();
        }
        fitness = profit;
        return profit;
    }


    @Override
    public String toString() {
        String sKnapsack = "";

        for (boolean x: Solution){
            if(x) sKnapsack+="1";
            else sKnapsack+="0";
        }

        return '[' +
                sKnapsack+
                ']';
    }
}

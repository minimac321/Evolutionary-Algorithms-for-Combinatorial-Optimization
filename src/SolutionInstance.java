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


    public boolean getBit(int iPos){
        return Solution[iPos];
    }

    public boolean[] getBoolArray(){
        return Solution;
    }

    public void setPosition(boolean[] pos){
        this.Solution = pos.clone();
        calculateFitness();
        calculateWeight();
    }

    public String stringSolution(){
        String s = "";

        for (boolean x: Solution){
            if(x) s +="1";
            else s +="0";
        }
        return s;
    }

    @Override
    protected SolutionInstance clone() throws CloneNotSupportedException {
        return (SolutionInstance) super.clone();
    }

    public boolean isTooHeavy(){
        return (totalWeight > capacity);
    }

    public int calculateWeight() {
        int weight = 0;

        for (int i = 0; i < Driver.num_of_items; i++) {
            if (Solution[i] == true) {
                weight += Items[i].getWeight();
            }
        }
        totalWeight = weight;
        return weight;
    }

    public int calculateFitness() {
        int profit = 0;

        for (int i = 0; i < Driver.num_of_items; i++) {
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

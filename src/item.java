public class item {

    private int Id;
    private int Weight;
    private int Value;

    public item(int id, int weight, int value){
        this.Id = id;
        this.Weight = weight;
        this.Value = value;
    }

    public int getId(){ return Id; }
    public int getWeight(){ return Weight; }
    public int getValue(){ return Value; }

    @Override
    public String toString() {
        return "item{" +
                "Id= " + Id +
                ", Weight= " + Weight +
                ", Value= " + Value +
                '}';
    }
}

public class TwoPointX {
    public static Chromosome[] cross(Chromosome[] parentArray, int cross_point1, int cross_point2) {
        Chromosome[] firstCross = new Chromosome[2];
        firstCross = OnePointX.cross(parentArray, cross_point1);

        Chromosome[] secondCross = new Chromosome[2];
        secondCross = OnePointX.cross(firstCross, cross_point2);

        return secondCross;
    }
}

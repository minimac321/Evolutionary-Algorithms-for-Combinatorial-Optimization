// Two point cross over working correctly, Tested.

public class TwoPointX {
    public static Chromosome cross(Chromosome[] parentArray, int cross_point1, int cross_point2, int number) {

        Chromosome[] firstCross = new Chromosome[2];
        firstCross[0] = OnePointX.cross(parentArray, cross_point1, 0);
        firstCross[1] = OnePointX.cross(parentArray, cross_point1, 1);

        if (firstCross[0] == null || firstCross[1] == null) {
            System.out.println("Null error @ TwoPoint firstCross");
            System.exit(0);
        }

        //System.out.println("After First Cross @ position - " + String.valueOf(cross_point1));
        //System.out.println(firstCross[0].gene.toString());
        //System.out.println(firstCross[1].gene.toString());

        Chromosome[] secondCross = new Chromosome[2];
        secondCross[0] = OnePointX.cross(firstCross, cross_point2, 0);
        secondCross[1] = OnePointX.cross(firstCross, cross_point2, 1);

//        System.out.println("After First Cross @ position - " + String.valueOf(cross_point2));
//        System.out.println(secondCross[0].gene.toString());
//        System.out.println(secondCross[1].gene.toString());

        if (secondCross[0] == null || secondCross[1] == null) {
            System.out.println("Null error @ TwoPoint secondCross");
            System.exit(0);
        }

        return secondCross[number];
    }
}

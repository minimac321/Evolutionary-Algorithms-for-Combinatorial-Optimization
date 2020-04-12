import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ParticleSwarmOptimization {
    public int particleNums;
    public int minVelocity;
    public int maxVelocity;
    public double c1;
    public double c2;
    public double inertia;
    public String config;
    public String reportString;
    public int maxIterations;

    public Particle[] Swarm;
    // Can call Driver.Items fro knapsack items

    public static MersenneTwister randomGenerator = new MersenneTwister();

    public SolutionInstance[] report_array = new SolutionInstance[Driver.max_iterations];


    public ParticleSwarmOptimization(int particleNums, int minVelocity, int maxVelocity, double c1, double c2,
                                     double inertia, String config, int max_iterations) {
        this.particleNums = particleNums;
        this.minVelocity = minVelocity;
        this.maxVelocity = maxVelocity;
        this.c1 = c1;
        this.c2 = c2;
        this.inertia = inertia;
        this.config = config;
        this.reportString =
                "PSO  |  #10000  |  " + String.valueOf(particleNums) + "  |  " + String.valueOf(minVelocity) + "  |  " + String.valueOf(maxVelocity)+
                        "  |  c1 (" + String.valueOf(c1) + ")  |  c2 (" + String.valueOf(c2) + ")  |  inertia (" + String.valueOf(inertia) + ")";

        this.maxIterations = max_iterations;

        Swarm = new Particle[particleNums];
    }

    // Best answer so far is 812
    public SolutionInstance execute(boolean generateReport) throws IOException {
        boolean bChange;
        ArrayList<Integer> gBestArray= new ArrayList<Integer>();

        ZeroInitialize(); // Initialize with zero fitness (no items in knapsack)

        // Must also make first addition in all arrays
        int iMax = Integer.MIN_VALUE;
        long runtimeStart = System.currentTimeMillis();


        for (int iter = 0; iter < 2000; iter++){

            //System.out.printf("***************** Iteration %d *****************\n", iter);

            // for each particle:
            for (int a = 0; a < particleNums; a++ ) {
                // Evaluate each particle's objective function
                Swarm[a].updateMetrics();
                // Update best Solution
                Swarm[a].updatePbestSolution();
            }

            bChange = false;
            SolutionInstance tmp = null;

            // Update gBest for all particles
            for (int p = 0; p < particleNums; p++ ) {
                if ( (Swarm[p].getFitness() > iMax) && !(Swarm[p].currentInstance.isTooHeavy())){
                    try{
                        bChange = true;
                        tmp = Swarm[p].currentInstance.clone();
                        //System.out.printf("New global Maximum. Previous = %d, New = %d\n", iMax, tmp.fitness);
                        iMax = tmp.fitness;
                    } catch (CloneNotSupportedException e) {
                        System.out.println("Error in clone somewhere");
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
            }

            // Update best Global Position
            if (bChange) updateGbest(tmp);

            // Update positions
            for (int j = 0; j < particleNums; j++ ) {
                // Update velocities
                //System.out.printf("Prev Pos: %s\n", Swarm[p].currentInstance.toString());
                Swarm[j].getNewPositions();
                //System.out.printf("New  Pos: %s\n", Swarm[p].currentInstance.toString());
            }

            gBestArray.add(Swarm[0].gBestInstance.fitness);
            try {
                report_array[iter] = Swarm[0].gBestInstance.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }// end iteration loop

        long totalTime = System.currentTimeMillis() - runtimeStart;

        SolutionInstance bestSolution = Swarm[0].gBestInstance;
        //System.out.printf("Knapsack: %s\n" + "Best solution value is: %d\n", bestSolution.toString(), bestSolution
        // .fitness);
        //System.out.println(gBestArray);

        new Report(generateReport, config, reportString, report_array, totalTime);

        return bestSolution;
    }

    public void ZeroInitialize() {
        for (int i = 0; i < particleNums; i++) {
            Particle particle = new Particle(minVelocity, maxVelocity,c1, c2, inertia,
                    new SolutionInstance(Driver.Items, Driver.num_of_items, Driver.max_capacity));
            Swarm[i] = particle;
        }
    }

    public void updateGbest(SolutionInstance s){
        for(int i=0; i<particleNums; i++){
            Swarm[i].setGbest(s);
        }
    }

}

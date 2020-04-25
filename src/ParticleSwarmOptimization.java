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

    public ArrayList<SolutionInstance> report_array;


    public ParticleSwarmOptimization(int particleNums, int minVelocity, int maxVelocity, double c1, double c2,
                                     double inertia, String config, int max_iterations) {
        report_array = new ArrayList<>();
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
        int mostRecentGBest = 0;
        boolean bChange;
        ArrayList<Integer> gBestArray= new ArrayList<Integer>();

        RandomInitialize(0.4);



        // Must also make first addition in all arrays
        int iMax = 0;
        long runtimeStart = System.currentTimeMillis();


        for (int iter = 0; iter < Driver.max_iterations; iter++){

            if (iter%100 == 0){
                printSwarm();
                System.out.printf("***************** Iteration %d *****************\n", iter);
            }

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
                if ( (Swarm[p].getFitness() > iMax) && (Swarm[p].currentInstance.fitness != 0)){
                    try{
                        bChange = true;
                        tmp = Swarm[p].currentInstance.clone();
                        System.out.printf("New global Maximum. Previous = %d, New = %d\n", iMax, tmp.fitness);
                        iMax = tmp.fitness;
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Update best Global Position
            if (bChange) {
                mostRecentGBest = iter;
                updateGbest(tmp);
            }

            // Update positions
            for (int j = 0; j < particleNums; j++ ) {
                // Update velocities
                //System.out.printf("Prev Pos: %s\n", Swarm[p].currentInstance.toString());
                Swarm[j].getNewPositions();
                //System.out.printf("New  Pos: %s\n", Swarm[p].currentInstance.toString());
            }
            // If stagnanent for 500 iterations, reshuffle all particles
            if (iter - mostRecentGBest > 500){
                reshuffled();
                System.out.println("============= Reshuffled =============");
                mostRecentGBest = iter;
            }

            gBestArray.add(Swarm[0].gBestInstance.fitness);
            try {
                 report_array.add(Swarm[0].gBestInstance.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }// end iteration loop

        long totalTime = System.currentTimeMillis() - runtimeStart;

        SolutionInstance bestSolution = Swarm[0].gBestInstance;

        if (!generateReport) {
            System.out.printf("Optimal Output: fitness = %5d | weight = %5d\n", bestSolution.fitness,
                    bestSolution.weight);
        }

        SolutionInstance[] list_reportArray = report_array.toArray(new SolutionInstance[report_array.size()]);
        new Report(generateReport, config, reportString, list_reportArray, totalTime);
        return bestSolution;
    }

    public void printSwarm(){
        String sOut = "";
        for (int i = 0; i < Swarm.length; i++) {
            sOut+= String.valueOf(Swarm[i].currentInstance.fitness) +", ";
        }
        System.out.println(sOut);
    }

    public void reshuffled() {
        for (int i = 0; i < particleNums; i++) {
            Swarm[i] = generateRandomInstance(Swarm[i]);
        }

    }
    public Particle generateRandomInstance(Particle particle) {
        try {
            Particle p = particle.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        SolutionInstance s = particle.currentInstance;

        do{
            boolean[] pos = new boolean[Driver.num_of_items];
            Arrays.fill(pos, false);

            for(int c = 0; c < Driver.num_of_items; c++) {
                if (SimulatedAnnealing.randomGenerator.nextDouble() < 0.3) pos[c] = true;
                else pos[c] = false;
            }
            s.setPosition(pos);
        } while (s.isTooHeavy());

        particle.setCurrentInstance(s);
        return particle;
    }

    public void RandomInitialize(double threshold){
        for (int i = 0; i < particleNums; i++) {
            boolean[] pos = new boolean[Driver.num_of_items];
            Arrays.fill(pos, false);

            for(int c = 0; c < Driver.num_of_items; c++) {
                if (ParticleSwarmOptimization.randomGenerator.nextDouble() < threshold) pos[c] = true;
                else pos[c] = false;
            }
            SolutionInstance s = new SolutionInstance(Driver.Items, Driver.num_of_items, Driver.max_capacity, pos);
            Swarm[i] = new Particle(minVelocity, maxVelocity,c1, c2, inertia, s);
        }
    }

    public void updateGbest(SolutionInstance s){
        for(int i=0; i<particleNums; i++){
            Swarm[i].setGbest(s);
        }
    }

}

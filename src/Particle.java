import javafx.geometry.Pos;

import java.util.Arrays;

public class Particle {
    public String Position;
    public double[] Velocity;
    //public double[] fitness;
    // //////////////
    public int minVelocity;
    public int maxVelocity;
    public double c1;
    public double c2;
    public double inertia;
    // ////////////
    public SolutionInstance currentInstance;
    public SolutionInstance pBestInstance;
    public SolutionInstance gBestInstance;

    public Particle(int minVelocity, int maxVelocity, double c1, double c2, double inertia, SolutionInstance solution) {

        this.minVelocity = minVelocity;
        this.maxVelocity = maxVelocity;
        this.c1 = c1;
        this.c2 = c2;
        this.inertia = inertia;

        this.currentInstance = solution;

        Velocity = new double[solution.numItems];

        this.pBestInstance = currentInstance;
        this.gBestInstance = currentInstance;

        generateRandomVel();
        generatePosValues();

        // Size is solution.numItems
        Position = solution.stringSolution();
    }

    private void generatePosValues() {
        boolean[] pos = new boolean[150];
        Arrays.fill(pos, false);

        for(int v = 0; v < Velocity.length; v++) {
                                                                    // sigmoid(Velocity[v])
            if (ParticleSwarmOptimization.randomGenerator.nextDouble() < 0.2) pos[v] = true;
            else pos[v] = false;
        }

        currentInstance.setPosition(pos);
        //System.out.println(String.valueOf(currentInstance.calculateFitness()) + " - " + String.valueOf
        // (currentInstance.calculateWeight()));

    }

    public void updatePbestSolution() {

        if (pBestInstance.fitness < currentInstance.fitness && !currentInstance.isTooHeavy()){
            try {
                pBestInstance = currentInstance.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    public double sigmoid(double d)
    {
        double answer = 0.0;
        answer = 1/ (1 + Math.exp(-d));
        return answer;

    }

    private void generateRandomVel() {

//        for(int v = 0; v < Velocity.length; v++) {
//            // create number between 0-1 and * VMAX
//            double vel = ParticleSwarmOptimization.randomGenerator.nextDouble() * maxVelocity;
//            Velocity[v] = checkBoundaries(vel);
//        }
        Arrays.fill(Velocity, minVelocity);
    }

    private double checkBoundaries(double v) {
        if (v > maxVelocity) return maxVelocity;
        if (v < minVelocity) return minVelocity;

        return v;
    }

    public void setGbest(SolutionInstance s){
        try {
            this.gBestInstance = s.clone();
            this.gBestInstance.calculateFitness();
            this.gBestInstance.calculateWeight();
        } catch (CloneNotSupportedException e) {
            System.out.println("Error in clone somewhere");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void updateMetrics(){
        this.currentInstance.calculateFitness();
        this.currentInstance.calculateWeight();
        this.pBestInstance.calculateFitness();
        this.pBestInstance.calculateWeight();
        this.gBestInstance.calculateFitness();
        this.gBestInstance.calculateWeight();
    }

    public int getFitness(){
        return currentInstance.fitness;
    }

    public int getWeight(){
        return currentInstance.totalWeight;
    }

    public void getNewPositions(){
        double r1, r2, threshold;
        int current, pBest, gBest;
        boolean[] pos;

        for (int i = 0; i < Velocity.length; i++) {
            r1 = ParticleSwarmOptimization.randomGenerator.nextDouble();
            r2 = ParticleSwarmOptimization.randomGenerator.nextDouble();

            current = (int)(currentInstance.stringSolution().charAt(i));
            pBest = (int)(pBestInstance.stringSolution().charAt(i));
            gBest = (int)(gBestInstance.stringSolution().charAt(i));

            Velocity[i] = Velocity[i]*inertia + c1*r1*(pBest - current ) + c2*r2*(gBest - current);

            // trace
            //System.out.println("Velocities: " + Arrays.toString(Arrays.copyOf(Velocity, 5)));

            pos = new boolean[150];
            threshold = 0.25;

            // ISSUE HERE // - find correct algorithm for when to choose
            for(int v = 0; v < Velocity.length; v++) {
                // sigmoid(Velocity[v])
                if (ParticleSwarmOptimization.randomGenerator.nextDouble() < threshold){
                    pos[v] = true;
                }
                else{
                    pos[v] = false;
                }
            }
            //System.out.printf("Before: %d - %s\n", currentInstance.fitness, currentInstance.stringSolution());
            currentInstance.setPosition(pos);
            currentInstance.calculateWeight();
            //System.out.printf("After: %d - %s\n", currentInstance.calculateFitness(), currentInstance.stringSolution());

            int iRetry = 0;
            while (currentInstance.isTooHeavy()){
                iRetry++;
                threshold-=0.03;

                for(int v = 0; v < Velocity.length; v++) {
                    // sigmoid(Velocity[v])
                    if (ParticleSwarmOptimization.randomGenerator.nextDouble() < threshold) pos[v] = true;
                    else pos[v] = false;
                }
                currentInstance.setPosition(pos);

            } // end while
            //trace
            //System.out.printf("Retry occurred %d times\n", iRetry);

        }
    }

}
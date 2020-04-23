import java.util.Arrays;

public class Particle implements Cloneable{
    public String Position;
    public double[] Velocity;

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

        this.minVelocity = minVelocity*-1;
        this.maxVelocity = maxVelocity;
        this.c1 = c1;
        this.c2 = c2;
        this.inertia = inertia;

        this.currentInstance = solution;

        Velocity = new double[solution.numItems];

        this.pBestInstance = currentInstance;
        this.gBestInstance = currentInstance;

        generateRandomVel();
        generatePosValues(0.2);

        // Size is solution.numItems
        Position = solution.stringSolution();
    }
    @Override
    protected Particle clone() throws CloneNotSupportedException {
        return (Particle) super.clone();
    }

    private void generatePosValues(double threshold) {
        boolean[] pos = new boolean[150];
        Arrays.fill(pos, false);

        for(int v = 0; v < Velocity.length; v++) {
            if (ParticleSwarmOptimization.randomGenerator.nextDouble() < threshold) pos[v] = true;
            else pos[v] = false;
        }
        currentInstance.setPosition(pos);
    }

    public void setCurrentInstance(SolutionInstance s){
        try {
            this.currentInstance = s.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void updatePbestSolution() {
        if ( (pBestInstance.fitness < currentInstance.fitness) && (currentInstance.fitness != 0) ){
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

        for(int v = 0; v < Velocity.length; v++) {
            // create number between 0-1 and * VMAX
            double vel = checkBoundaries(ParticleSwarmOptimization.randomGenerator.nextDouble() * maxVelocity) ;

            if (!ParticleSwarmOptimization.randomGenerator.nextBoolean()) {
                Velocity[v] = vel * -1;
            }else{
                Velocity[v] = vel;
            }
        }
    }

    private double checkBoundaries(double v) {
        if (v > maxVelocity) return maxVelocity;
        if (v < minVelocity) return minVelocity;

        return v;
    }

    public void setGbest(SolutionInstance s){
        try {
            this.gBestInstance = s.clone();
            this.gBestInstance.calculateWeight();
            this.gBestInstance.calculateFitness();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void updateMetrics(){
        this.currentInstance.calculateWeight();
        this.currentInstance.calculateFitness();
        this.pBestInstance.calculateWeight();
        this.pBestInstance.calculateFitness();
        this.gBestInstance.calculateWeight();
        this.gBestInstance.calculateFitness();
    }

    public int getFitness(){
        return currentInstance.fitness;
    }

    public int getWeight(){
        return currentInstance.weight;
    }

    public void getNewPositions(){
        double r1, r2, threshold;
        int current, pBest, gBest;
        boolean[] pos;

        for (int i = 0; i < Velocity.length; i++) {
            r1 = ParticleSwarmOptimization.randomGenerator.nextDouble();
            r2 = ParticleSwarmOptimization.randomGenerator.nextDouble();

            current = Character.getNumericValue(currentInstance.stringSolution().charAt(i));
            pBest = Character.getNumericValue(pBestInstance.stringSolution().charAt(i));
            gBest = Character.getNumericValue(gBestInstance.stringSolution().charAt(i));

            Velocity[i] =
                    checkBoundaries(Velocity[i] * inertia + c1 * r1 * (pBest - current) + c2 * r2 * (gBest - current));
        }

        pos = new boolean[150];
        for(int v = 0; v < Velocity.length; v++) {
            if (ParticleSwarmOptimization.randomGenerator.nextDouble() < sigmoid(Velocity[v])){
                pos[v] = true;
            }
            else{
                pos[v] = false;
            }
        }
        //System.out.printf("Before: %d - %s\n", currentInstance.fitness, currentInstance.stringSolution());
        currentInstance.setPosition(pos);
        //System.out.printf("After: %d - %s\n", currentInstance.calculateFitness(), currentInstance.stringSolution());

    }

}
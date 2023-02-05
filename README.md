# Knapsack-Solver

Solving the NP-complete Knapsack problem (150 Items) using Evolutionary Algorithms.  


#### What is the Knapsack Combinatorial Optimization Problem:
Given a set of items, each with a weight and a value, determine which items to include in the collection so that the total weight is less than or equal to a given limit and the total value is as large as possible


The 3 algorithms implemented are:
  - [Simulated Annealing](https://en.wikipedia.org/wiki/Simulated_annealing) (SA)
    - is a probabilistic technique for approximating the global optimum of a given function.
  - [Genetic Algorithm](https://en.wikipedia.org/wiki/Genetic_algorithm) (GA)
    - is a method for solving both constrained and unconstrained optimization problems that is based on natural selection, the process that drives biological evolution.
  - [Particle Swarm Optimization](https://en.wikipedia.org/wiki/Particle_swarm_optimization) (PSO)
    -  is a computational method that optimizes a problem by iteratively trying to improve a candidate solution with regard to a given measure of quality.


Each configuration is run for 10000 iterations and compared to the best know optimum of 997 in the report.  

For a single configuration of a specified algorithm [ga | sa | pso] and configuration number (between 1 and 25) run this command line with a file name
```-configuration [name]_default_[configuration-number].json```
where the name could be: "ga_default_01"

```
-configuration ga_default_01.json
```

A report will be generated into `Default_Output` folder.  


To search for the best configuration of ALL algorithm configurations, run this command:  
`-search_best_configuration [ga | sa | pso]`

```-search_best_configuration ga```  

Once the best is found, it will be written into a JSON file called [ga | sa | pso]_best.json under the `Best Output` folder  


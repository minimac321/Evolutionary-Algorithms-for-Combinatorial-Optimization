# Knapsack-Solver
Solve the Knapsack problem (150 Items) using Evolutionary Algorithms.
The 3 algorithms implemented are Simulated Annealing(SA), Genetic Algorithm(GA), and Particle Swarm Optimization(PSO).
Each configuration is run for 10000 iterations and compared to the best know optimum of 997 in the report.

To run a single configuration, input:
    "-configuration [name].json"
which will run the specified algorithm and generate a report in the Default_Output folder.
e.g. "-configuration ga_default_01.json"

To search for the best configuration of any algorithm, input:
    "-search_best_configuration [ga | sa | pso]"
which will run all the selected algorithm's configurations to find the best. Once the best is found, it will be written into a JSON file
called [ga | sa | pso]_best.json under the Best Output folder
e.g. "-search_best_configuration ga"

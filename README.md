This project was initiated at IFMA during my engineering studies. 
As I majored in manufacturing management, this is about plant optimization. 
Initially written in C++, I wanted to have fun and write it in Scala.

## Context
There's a workshop, organized in flexible cells (5 machines per cell). 
Given 3 products with different routes, given 7 different environments, we want to minimize traffic between cells, whatever the environment.
The project is composed of 3 different programs: PATH MAKER, OPTIMIZER and EVALUATION, each needing to be executed in that order.

### 1. PATH MAKER
Generates traffic matrices: one for each environment. 
Besides, there is a set of nominal environments (Canonical), and a set of uncommon ones (Disrupted). 
Path Maker outputs 14 different traffic files.

### 2. OPTIMIZER
Optimizer takes one traffic file at a time and selects the setup (Individual) that minimizes traffic, based on a genetic algorithm.
Optimizer outputs 1 setup (Individual) file per traffic file.

### 3. EVALUATION
Not implemented yet

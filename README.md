### Brief Introduction
&emsp;&emsp;The project is the course project of Parallel Computing 2018 Autumn in Nanjing University. The requirement of it is to implement the serial and parallel Java programs of several sort algorithms like Quicksort, Enumsort and Mergesort. What's more, it's necessary to compare the running time of these programs and analyze the advantage, bottleneck and drawback of parallel algorithms. Here, I implement the above algorithms and try different ways of implementation of the same algorithm.

---
### Inputs & Outputs
&emsp;&emsp;The input dataset is stored in `random.txt` with 3000 unordered integers ranging from -10000 to 10000, seperated by blank space. The six different algorithms seperately prints their outputs to `order*.txt` where `*` ranges from 1 to 6.

---
### The APIs used when implementing multi-thread version
- `Thread` class and `start`, `join` methods
- `RecursiveAction`, `ForkJoinPool` classes and `invoke`, `invokeAll` methods
- `ExecutorService`, `Future` classes and `submit`, `shutdown` methods

Here I implement Quicksort and MergeSort with different APIs.

---
### How to compile & run?
- Since the project is build by Intellij IDEA, you can import it into your IJ or open it in IJ, and click `make project` or `run`. 
- I provide `ReadMe.txt` to describe the compiling and running command of the project.You can input `$ bash run.sh`

---

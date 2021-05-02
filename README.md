# CompositeKernel

This project contains my implementation of an Evolutionary Algorithm to optimize finding the "best" composite kernel for a support vector machine.  See our [AAAI Paper](https://www.aaai.org/Library/AAAI/2005/aaai05-115.php) for more details.

The evolutionary algorithm created linear combinations of existing kernel functions, and then depended on distributed execution of the 10-way cross validation used to evaluate the "fitness" of each gene.


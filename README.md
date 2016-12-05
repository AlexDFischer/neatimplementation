# NEAT
In this project you will be creating a graph data structure. Specifically, you will be creating a directed graph data structure that represents a neural network.
## Background on Neural Networks
Neural networks are objects that are used in artificial intelligence. They are loosely modelled after animal neural systems. They are used to compute some value, or a set of values, from a set of inputs. Their advantage lies in the fact that they can be taught in the sense that there are ways to systematically improve what they compute so that it matches a desired value. Fundamentally, neural networks are a series of **nodes**, some of which are **inputs**, some of which are **outputs**, some of which are **biases**, and the rest of which are **hidden**. Nodes are connected to eachother with **connections**, and each connection has a **weight**.

![neural network](https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Colored_neural_network.svg/300px-Colored_neural_network.svg.png)

Each nodes in the network has inputs, which are the nodes that are connected to it, and outputs, which are the nodes that it is connected to. In order to compute the **activation** of a node, you start with the node's inputs. For each input connection, multiply the connection weight by the activation of the node that is connected to it. Add up all those weighted inputs together. Then apply a **transfer function** to that sum. The activation of the node is just the output of the transfer function.

![neural network](http://3.bp.blogspot.com/-7RWgohC4pYE/VhtQ8IELsLI/AAAAAAAAA6I/_XFhMbjpcCY/s1600/Simple%2BNeural%2BNetwork.png)

The transfer function we will use is a modified form of a **sigmoid** function. It is defined to be f(x)=1/(1+e^(-4.9x)). The reason why we use this function is rather opaque. If you want to learn more about neural networks, I have some recommended links at the bottom of this section.

When computing a function with a neural network, you start by having the activation of the input nodes be the given inputs, and the activation of the bias nodes be 1. Then you compute the activation of the output nodes of the network. The activation of the output nodes of the network is the output of the network. If this does not make sense to you, I reccomend reading the "perceptrons" and "sigmoid neurons" sections of [this book](http://neuralnetworksanddeeplearning.com/chap1.html).

To read more about neural networks, see http://neuralnetworksanddeeplearning.com/chap1.html

To read more about the algorithm we will be implementing, see http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf

## NeuroEvolution of Augmenting Topologies
NeuroEvolution of Augmenting Topologies, or NEAT, is one algorithm that evolves neural networks and "teaches" them to solve problems. In this algorithm, each network is represented by a list of **node genes**, which represent the nodes in the network, and a series of **connection genes** that represent the connections between the nodes. Each node gene can be an input, a bias, an output, or a hidden node. Each connection has an input node, and output node, a weight, a boolean indicating if it is expressed or not, and an innovation number, which you don't have to worry about. The connection is only active if the expressed boolean is true.

## What you have to do
You will be implementing several methods in the `Genome` class. This class represents one neural network. You already have the classes `NodeGene`, `ConnectionGene`, and the enum `NodeType`, which do what you would expect them to do.

### clone
You will write a `clone` method that makes a deep copy of this network. The copy will be deep in the sense that the new genome will have node genes and connection genes that are cloned, not the same.

### distance
This one is harder. The distance between two genomes is used in determining what species they are in, and if the genomes should mate with each other, which is one way new genomes are created. The distance between two genomes is defined to be (c1×E+c2×D)/N+c3×W where E is the number of excess genes, D the number of disjoint genes, N is the number of connections in the larger genome, and W is the average absolute value of the difference in weights among matching genes.

Genes in the two genomes are said to be matching if they share the same innovation number. A gene in a genome is considered to be disjoint with another genome if its innovation number is not the same as that of any other gene in the other genome, but it is less than some gene in the other genome. A gene in a genome is considered to be excess with another genome if its innovation number is greater than that of any other gene in the other genome. c1, c2, and c3 are just constants that are given.

Write a `distance` method that computes the distance between two genomes given the above definition. The constants c1, c2, and c3 are given in the function call.

### propogate
This one is harder yet. This method takes in inputs and computes the output of the network. Use the function `sigmoid` in the `Static` class as the transfer function, and return an array of outputs of the neural network.

### wouldMakeRecurrent
This one is interesting because there are many ways to do it. For a variety of reasons (that should become clear once you complete some of the other methods), we can't have the network be recurrent (at least we won't; there are some implementations of NEAT and other neural network algorithms that use recurrent neural networks. But I digress). A network is recurrent if there are loops in it, or if it is possible to get from a node back to the same node following connections. You will write a method `wouldMakeRecurrent` that determines whether connecting two nodes would make the network recurrent. The input and output nodes of the proposed new connection are given in the function call.

## Hints
For `propogate`, start at the outputs and work your way back, not the other way around. Also, use dynamic programming.

For `wouldMakeRecurrent`, use a depth first search and travel along the connections.

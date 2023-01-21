// Centrality Measures API implementation
// COMP2521 Assignment 2

#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "CentralityMeasures.h"
#include "Dijkstra.h"
#include "Graph.h"

double *closenessCentrality(Graph g) {
	// Getting number of vertices
    int v = GraphNumVertices(g);

    // Mallocing vSet/closeness array
    double *vSet = calloc(v, sizeof(double));
    
    // For every vertex, gets shortest distance and number reachable
    // using Dijkstra function
    for (int i = 0; i < v; i++) {
        NodeData *result = dijkstra(g, i);
        
        double distance_sum = 0;
        double n = 0;
        double N = v;
        // Goes through list and gets total distance and number
        // reachable
        for (int j = 0; j < v; j++) {
            if (result[j].dist != INFINITY) {
                distance_sum += result[j].dist;
                n++;
            }
        }
        if (distance_sum != 0) {
            vSet[i] = ((n-1)/(N-1)) * ((n-1)/(distance_sum));
        }
        freeNodeData(result, v);
    }
    
    return vSet;
}

double *betweennessCentrality(Graph g) {
	// Getting number of vertices
    int v = GraphNumVertices(g);

    // Mallocing Betweenness results array
    double *b_results = calloc(v, sizeof(double));
    
    
    NodeData *all_results[v];
    
    // For each vertex, puts resulting node array into array all_results
    for (int i = 0; i < v; i++) {
        all_results[i] = dijkstra(g, i);
    }
    
    // Setting up value to iterate through to each node array in all_results
    for (int i = 0; i < v; i++) {
        // Setting up value to iterate through to each node's predecessor array 
        // in a single Dijkstra node array
        for (int j = 0; j < v; j++) {

            PredNode *current = all_results[i][j].pred;

            // Gets number of shortest paths to vertex from current node
            double num_paths = 0;
            double increment = 0;
            while (current != NULL) {
                num_paths++;
                current = current->next;
            }
            if (num_paths != 0) {
                increment = 1/num_paths;
            }
            
            current = all_results[i][j].pred;

            // Traverses each shortest path, incrementing betweenness value
            // of each node along the way
            for (int k = 0; k < num_paths; k++) {
                Vertex curr = current->v;
                while (curr != j && curr != i) {
                    b_results[curr] += increment;
                    curr = all_results[i][curr].pred->v;
                }

                current = current->next;
            }
        }
    }
    
    // Frees each vertex's resulting node array
    for (int i = 0; i < v; i++) {
        freeNodeData(all_results[i], v);
    }
    
    return b_results;
}
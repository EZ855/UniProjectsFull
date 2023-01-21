
#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"
int DoDFS(Graph g, int notInCycle[], int num_ver, int start_ver, int probe_ver);

// Worst case time complexity of this solution: O(n^2)
void nodesNotInCycle(Graph g, int notInCycle[]) {
    // TODO

    int num_ver = GraphNumVertices(g);
    for (int i = 0; i < num_ver; i++) {
        for (int j = 0; j < num_ver; j++) {
            // If there is an edge going from probe_ver to j
            if (GraphIsAdjacent(g, i, j)) {
                if(DoDFS(g, notInCycle, num_ver, i, j) == 1){
                    notInCycle[j] = 0;
                }
            }
        }
    }

    for (int i = 0; i < num_ver; i++) {
        if (notInCycle[i] == -1) {
            notInCycle[i] = 1;
        }
    }
}

int DoDFS(Graph g, int notInCycle[], int num_ver, int start_ver, int probe_ver) {
    if (probe_ver == start_ver) {
        return 1;
    }
    int yes = 0;
    for (int j = 0; j < num_ver; j++) {
        // If there is an edge going from probe_ver to j
        if (GraphIsAdjacent(g, probe_ver, j)) {

            if(DoDFS(g, notInCycle, num_ver, start_ver, j) == 1){
                notInCycle[probe_ver] = 0;
                yes = 1;
            }
        }
    }
    if (yes == 1 && notInCycle[probe_ver] == 0) {
        return 1;
    }
    return 0;
}
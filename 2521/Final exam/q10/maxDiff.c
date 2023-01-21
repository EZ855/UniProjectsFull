
#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "BSTree.h"
int FindNumNodes(BSTree t, int *curr_max);

int maxDiff(BSTree t) {
    // TODO

    int max = 0;

    FindNumNodes(t, &max);

    return max;
}

int FindNumNodes(BSTree t, int *curr_max) {
    if (t == NULL) {
        return 0;
    }
    else if (left(t) == NULL && right(t) == NULL) {
        return 1;
    }
    else {
        int num_left = FindNumNodes(left(t), curr_max);
        int num_right = FindNumNodes(right(t), curr_max);
        int curr_diff = abs(num_left - num_right);
        if (curr_diff > *curr_max) {
            *curr_max = curr_diff;
        }
        return num_left + num_right + 1;
    }
}
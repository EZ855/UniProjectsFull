// exam_q1.c
//
// This program was written by z5383657
// on 18/08/2021

#include <stdio.h>

// Return the number of pairs with multiples in the array.
int count_multiples(int size, int array[size][2]) {
    int num_multiples = 0;
    int i = 0;
    while (i < size) {
        if (array[i][1] % array[i][0] == 0) {
            if (array[i][0] <= array[i][1]) {
                num_multiples++;
            }
        }
        i++;
    }
    return num_multiples;
}


// This is a simple main function which could be used
// to test your count_multiples function.
// It will not be marked.
// Only your count_multiples function will be marked.

#define TEST_ARRAY_SIZE 1

int main(void) {
    int test_array[TEST_ARRAY_SIZE][2] = {
        {8, 4}
    };

    int result = count_multiples(TEST_ARRAY_SIZE, test_array);
    printf("%d\n", result);

    return 0;
}

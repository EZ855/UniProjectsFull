// exam_q3.c
//
// This program was written by z5383657
// on 18/08/2021

#include <stdio.h>

#define MAX_ARRAY_SIZE 10000

int main(void) {
  
    int i = 0;
    int array[MAX_ARRAY_SIZE] = {1};
    
    scanf("%d", &array[i]);
    i++;
    
    while (array[i-1] != 0) {
        scanf("%d", &array[i]);
        i++;
    }
    
    int k = 0;
    while (k < i-1) {
        printf("%d ", array[k]);
        k += 2;
    }
    
    k = 1;
    while (k < i-1) {
        printf("%d ", array[k]);
        k += 2;
    }
    
    printf("\n");
    
    return 0;
}

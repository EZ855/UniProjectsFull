#include <stdio.h>
#include <stdlib.h>

#define SERIES_MAX 46

int fibonacci (int n, int array[]);

int main(void) {
    int already_computed[SERIES_MAX + 1] = { 0, 1 };

	int u_input;
	while (scanf("%d", &u_input) != EOF) {
	    printf("%d\n", fibonacci(u_input, already_computed));
	}
	

    return EXIT_SUCCESS;
}

int fibonacci (int n, int array[]) {
    
    if(n == 0) {
        return array[0];
    }
    else if(n == 1) {
        return array[1];
    }
    else if (array[n] != 0 && array[n] != 1) {
        return array[n];
    }
    else {
        array[n] = fibonacci(n-1, array) + fibonacci(n-2, array);
        return (array[n]);
        
    }
}


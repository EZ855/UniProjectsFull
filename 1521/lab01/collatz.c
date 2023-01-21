#include <stdio.h>
#include <stdlib.h>

void collatz(int number);

int main(int argc, char **argv) {

    if (argc == 1) {
	    printf("Usage: ./collatz NUMBER\n");
	    exit(EXIT_SUCCESS);
	}
	else {
	    int number = atoi(argv[1]);
	    printf("%d\n", number);
	    collatz(number);
	}
    
	return EXIT_SUCCESS;
}


void collatz (int number) {
    int number1 = number;
    if (number1 != 1) {
        if (number1 % 2 == 0) {
            number1 = number1/2;
            printf("%d\n", number1);
            collatz(number1);
        }
        else {
            number1 = number1 * 3 + 1;
            printf("%d\n", number1);
            collatz(number1);
        }
    }
}

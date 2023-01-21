// z5383657
// Evan Zhang
// 28/06/2021

#include <stdio.h>

#define MAX_DIGITS 10

int main(void) {
    int pi[MAX_DIGITS] = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
    int user_specified_digits;
    printf("How many digits of pi would you like to print? ");
    scanf("%d", &user_specified_digits);
    
    int loop_counter = 0;
    while (user_specified_digits > loop_counter) {
        if (loop_counter == 1) {
            printf(".");
        }
        printf("%d", pi[loop_counter]);
        loop_counter++;    
    }
    printf("\n");
    return 0;
}

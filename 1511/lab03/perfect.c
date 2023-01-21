//z5383657
//Evan Zhang
//18/06/2021

#include <stdio.h>

int main (void) {
    int number;
    printf("Enter number: ");
    scanf("%d", &number);
    printf("The factors of %d are:\n", number);
    
    int counter = 1;
    int factor_sum = 0;
    while (number >= counter) {
        if (number % counter == 0) {
            printf("%d\n", counter);
            factor_sum = factor_sum + counter;
        }       
        counter++;
    }
    
    printf("Sum of factors = %d\n", factor_sum);
    
    if ((factor_sum - number) == number) {
        printf("%d is a perfect number\n", number);
    }
    else {
        printf("%d is not a perfect number\n", number);
    }
    return 0;
}

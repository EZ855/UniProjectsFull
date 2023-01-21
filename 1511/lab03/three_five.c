//z5383657
//Evan Zhang
//18/06/2021

#include <stdio.h>

int main (void) {
    int number;
    printf("Enter number: ");
    scanf("%d", &number);
    
    int counter = 1;
    while (number > counter) {
        if (((counter % 3) == 0) || ((counter % 5) == 0)) {
            printf("%d\n", counter);
        }
        counter++;
    }
    return 0;
}

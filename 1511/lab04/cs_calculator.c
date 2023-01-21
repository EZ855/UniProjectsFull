//z5383657
// Evan Zhang
// 28/06/2021

#include <stdio.h>
int square(int integer1);
int power(int integer1, int integer2);

int main(void) {

    int one_or_two = 0;
    int integer1 = 0;
    int integer2 = 0;
    int scan_status = 1;
    
    printf("Enter instruction: ");
        
    scan_status = scanf("%d", &one_or_two);
    
    while (scan_status == 1) {
        if (one_or_two == 1) {
            scan_status = scanf("%d", &integer1);
            printf("%d\n", square(integer1));
        }
        else if (one_or_two == 2) {
            scan_status = scanf("%d%d", &integer1, &integer2);
            printf("%d\n", power(integer1, integer2));
        }
        printf("Enter instruction: ");
        
        scan_status = scanf("%d", &one_or_two);
    }
    return 0;
}

//finds the square of the second integer provided for when the first number is one
int square (int integer1) {
    int square = integer1 * integer1;
    return square;
}

//finds integer1 to the power of integer2 for when the first number is two
int power (int integer1, int integer2) {
    int power = integer1;
    if (integer2 == 0) {
        power = 1;
        return power;
    }
    int loop_counter = 1;
    while (integer2 > loop_counter) {
        power = power * integer1;
        loop_counter++;
    }
    
    return power;
}

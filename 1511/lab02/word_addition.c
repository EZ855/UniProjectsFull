//z5383657
//Evan Zhang
//11/06/2021

#include <stdio.h>

int main(void) {
    int integer1;
    int integer2;
    
    printf("Please enter two integers:");
    scanf("%d %d", &integer1, &integer2);
    int integersum = 0;
    integersum = integer1 + integer2;
    
    if (integer1 > -10) {
        if (integer2 > -10) {
            if (integersum > -10) {
                printf("%d + %d = %d", integer1, integer2, intergersum);
            }
            else if (integersum == -10) {
                printf("%d + %d = negative ten", integer1, integer2);
            }
            else if (integersum == -9) {
                printf("%d + %d = negative nine", integer1, integer2);
            }
            else if (integersum == -8) {
                printf("%d + %d = negative eight", integer1, integer2);
            }
            else if (integersum == -7) {
                printf("%d + %d = negative seven", integer1, integer2);
            }
            else if (integersum == -6) {
                printf("%d + %d = negative six", integer1, integer2);
            }
            else if (integersum == -5) {
                printf("%d + %d = negative five", integer1, integer2);
            }
            else if (integersum == -4) {
                printf("%d + %d = negative four", integer1, integer2);
            }
            else if (integersum == -3) {
                printf("%d + %d = negative three", integer1, integer2);
            }
            else if (integersum == -2) {
                printf("%d + %d = negative two", integer1, integer2);
            }
            else if (integersum == -1) {
                printf("%d + %d = negative one", integer1, integer2);
            }
            else if (integersum == 0) {
                printf("%d + %d = zero", integer1, integer2);
            }
            else if (integersum == 1) {
                printf("%d + %d = one", integer1, integer2);
            }
            else if (integersum == 2) {
                printf("%d + %d = two", integer1, integer2);
            }
            else if (integersum == 3) {
                printf("%d + %d = three", integer1, integer2);
            }
            else if (integersum == 4) {
                printf("%d + %d = four", integer1, integer2);
            }
            else if (integersum == 5) {
                printf("%d + %d = five", integer1, integer2);
            }
            else if (integersum == 6) {
                printf("%d + %d = six", integer1, integer2);
            }
            else if (integersum == 7) {
                printf("%d + %d = seven", integer1, integer2);
            }
            else if (integersum == 8) {
                printf("%d + %d = eight", integer1, integer2);
            }
            else if (integersum == 9) {
                printf("%d + %d = nine", integer1, integer2);
            }
            else if (integersum == 10) {
                printf("%d + %d = ten", integer1, integer2);
            }
        }
    }
    return 0;
}

// z5383657
// Evan Zhang
// 23/06/2021

#include <stdio.h>

int main (void) {
    printf("Please enter three numbers: ");
    
    double double1;
    double double2;
    double double3;
    
    scanf("%lf %lf %lf", &double1, &double2, &double3);
    
    if (double1 > double2 && double2 > double3) {
        printf("down\n");
    }
    
    else if (double1 < double2 && double2 < double3) {
        printf("up\n");
    }
    
    else {
        printf("neither\n");
    }

    return 0;
}

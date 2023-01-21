// z5383657
// Evan Zhang
// 28/06/2021

// This program prints out facts about a circle given its radius,
// using functions.

#include <stdio.h>
#include <math.h>

double area(double radius);
double circumference(double radius);
double diameter(double radius);

// DO NOT CHANGE THIS MAIN FUNCTION
int main(void) {
    double radius;

    printf("Enter circle radius: ");
    scanf("%lf", &radius);

    printf("Area          = %lf\n", area(radius));
    printf("Circumference = %lf\n", circumference(radius));
    printf("Diameter      = %lf\n", diameter(radius));

    return 0;
}


// Calculate the area of a circle, given its radius.
double area(double radius) {
    double area = M_PI * radius * radius;
    return area;
}

// Calculate the circumference of a circle, given its radius.
double circumference(double radius) {
    double circumference = 2.0 * M_PI * radius;
    return circumference;
}

// Calculate the diameter of a circle, given its radius.
double diameter(double radius) {
    double diameter = 2.0 * radius;
    return diameter;
}



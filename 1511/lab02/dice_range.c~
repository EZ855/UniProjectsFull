// z5383657
// Evan Zhang
// 11/06/2021

#include <stdio.h>

int main(void) {
    int number_of_sides;
    int number_of_dice;
    
    printf("Enter the number of sides on your dice:");
    scanf("%d", &number_of_sides);
    printf("Enter the number of dice being rolled:");
    scanf("%d", &number_of_dice);
    
    if (number_of_sides < 1 || number_of_dice < 1) {
        printf("These dice will not produce a range.\n");
    }
    else {
        int range_floor = number_of_dice;
        int range_ceiling = number_of_dice*number_of_sides;
        double average_value = (range_floor + range_ceiling) * 0.5;
    
        printf("Your dice range is %d to %d.\n", range_floor, range_ceiling);
        printf("The average value is %lf\n", average_value);
    }
    return 0;
}

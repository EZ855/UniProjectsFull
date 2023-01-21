//z5383657
//Evan Zhang
//01/07/2021

#include <stdio.h>

int main(void) {
    
    int number_of_numbers;
    
    printf("How many numbers: ");
    scanf("%d", &number_of_numbers);
    
    int loop_counter = 0;
    int number_temp_storage;
    int number_sum = 0;
    
    while (loop_counter < number_of_numbers) {
    
        scanf("%d", &number_temp_storage);
        
        number_sum = number_sum + number_temp_storage;
        
        loop_counter++;
    }

    printf("The sum is: %d\n", number_sum);

    return 0;
}

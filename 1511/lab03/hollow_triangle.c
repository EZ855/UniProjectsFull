//z5383657
//Evan Zhang
//18/06/2021

#include <stdio.h>

int main (void) {
    int overall_size;
    printf("Enter size: ");
    scanf("%d", &overall_size);
    
    int row_counter = 0;
    while (row_counter < overall_size - 1) {
    
        int column_counter = 0;
        while (column_counter < overall_size) {
        
            if (column_counter == row_counter) {
                printf("*\n");
            }
            else if (column_counter == 0) {
                printf("*");
            }
            else if (column_counter < row_counter) {
                printf(" ");
            }
            column_counter++;
        }
        row_counter++;
        if (row_counter == overall_size - 1) {
        
            int last_line_counter = 0;
            while (last_line_counter < overall_size) {
            
                printf("*");
                last_line_counter++;
            }
        }
    }    
    printf("\n");
    return 0;
}

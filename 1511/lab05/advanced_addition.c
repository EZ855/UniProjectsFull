//z5383657
//Evan Zhang
//04/07/2021

// Add two numbers together, but in an array.

#include <stdio.h>
#include <assert.h>

#define MAX_SIZE 101

int sum(int num_lines, int num_digits, int array[MAX_SIZE][MAX_SIZE]);

// DO NOT CHANGE THIS MAIN FUNCTION
int main(void) {
    int array[MAX_SIZE][MAX_SIZE] = {0};

    // Get the array size.
    int num_digits, num_rows;
    printf("Enter the number of rows (excluding the last): ");
    scanf("%d", &num_rows);
    assert(num_rows > 0 && num_rows < 100);

    printf("Enter the number of digits on each row: ");
    scanf("%d", &num_digits);
    assert(num_digits > 0 && num_digits < MAX_SIZE);

    // Scan in values for the array.
    printf("Enter 2D array values:\n");
    int i = 0;
    while (i < num_rows) {
        int j = 0;
        while (j < num_digits) {
            assert(scanf("%d", &array[i][j]) == 1);
            if (array[i][j] < 0 || array[i][j] > 9) {
                printf("You entered a value not between 0 and 9.\n");
                return 1;
            }
            j++;
        }
        i++;
    }

    int carry = sum(num_rows, num_digits, array);

    int j = 0;
    while (j < num_digits) {
        printf("%d ", array[num_rows][j]);
        j++;
    }
    printf("\n");

    if (carry > 0) {
        printf("Carried over: %d\n", carry);
    }

    return 0;
}

// Put the sum of the lines in the array into the last line
// accounting for carrying. Return anything you did not carry.
//
// NOTE: num_lines is the number of lines you are adding together. The
// array has an extra line for you to put the result.
int sum(int num_lines, int num_digits, int array[MAX_SIZE][MAX_SIZE]) {
    // Put your code here.
    int carry = 0;
    int i = num_digits - 1;
    // Starting from the last digit (i.e. the ones) and finishing with 
    // num_digits - 1 (i.e. all the digits meant to be added)
    while (i >= 0) {
        int array_column_sum = 0;
        int j = 0;
        //Starting from the first line to the second last line (i.e.
        // all the lines including the last final sum), do the following 
        while (j <= num_lines) {
            array_column_sum = array_column_sum + array[j][i];
            j++;
        }
        
        array[j-1][i] = array_column_sum % 10;
        
        if (i > 0) {
            array[j-1][i-1] = array_column_sum/10;
        }
        else {
            carry = array_column_sum/10;
            
        }
        i--;
    }
    return carry;
}

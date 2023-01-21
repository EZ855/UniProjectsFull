#include <stdio.h>
#include <stdlib.h>

// print the locations of a specified byte sequence in a file
// the first command line argument is a filname
// other command line arguments are integers specifying a byte sequence
// all positions the byte sequence occurs in the file are printed

int main(int argc, char *argv[]) {

    FILE *input_stream = fopen(argv[1], "r");
    if (input_stream == NULL) {
        perror(argv[1]);
        return 1;
    }

    int sequence_array[argc - 2];
    for (int i = 2; i < argc; i++ ) {
        sequence_array[i-2] = atoi(argv[i]);
    }
    int found = 0;
    int more_than_1 = 0;
    int c = 0;
    while ((c = fgetc(input_stream)) != EOF && found == 0) {
        int i = 0;
        while (c == sequence_array[i]) {
            if (i == argc - 3) {
                found = 1;
                break;
            }
            c = fgetc(input_stream);
            more_than_1 = 1;
            i++;
        }
    }
    
    if (found == 1 && more_than_1 == 1) {
        int current_pos = ftell(input_stream) - argc + 1;
        printf("Sequence found at byte position: %d\n", current_pos);
    }
    else if (found == 1 && more_than_1 == 0) {
        int current_pos = ftell(input_stream) - argc + 2;
        printf("Sequence found at byte position: %d\n", current_pos);
    }
    else {
        printf("Sequence not found\n");
    }
    return 0;
}

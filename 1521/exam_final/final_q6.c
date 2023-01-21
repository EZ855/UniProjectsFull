#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

// print a specified byte from a file
//
// first command line argument specifies a file
// second command line argument specifies a byte location
//
// output is a single line containing only the byte's value
// printed as a unsigned decimal integer (0..255)
// if the location does not exist in the file
// a single line is printed saying: error

int main(int argc, char *argv[]) {

    
    FILE *output_stream = fopen(argv[1], "r");
    if (output_stream == NULL) {
        perror(argv[1]);
        return 1;
    }
    
    int offset = atoi(argv[2]);
    
    if (offset >= 0) {
        if (fseek(output_stream, offset, SEEK_SET) == -1) {
            printf("error\n");
            return 0;
        }
    }
    else {
        if (fseek(output_stream, offset, SEEK_END) == -1) {
            printf("error\n");
            return 0;
        }
    }
    int byte = fgetc(output_stream);
    
    printf("%d\n", byte);
    
    fclose(output_stream);
    
    return 0;
}

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

int main(int argc, char*argv[]) {
    if (argc <= 0) {
        printf("Wrong number of arguments.\n");
        return 0;
    }
    
    FILE *output_stream = fopen(argv[1], "w");
    if (output_stream == NULL) {
        perror(argv[1]);
        return 1;
    }
    for (int i = 2; i < argc; i++) {
        int character = atoi(argv[i]);
        if (character >= 0 && character <= 255) {
            fputc(character, output_stream);
        }
    }
    
    

    // fclose will flush data to file
    // best to close file ASAP
    // but doesn't matter as file autoamtically closed on exit
    fclose(output_stream);

    return 0;
}

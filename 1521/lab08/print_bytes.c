#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

int main(int argc, char*argv[]) {
    if (argc != 2) {
        printf("Wrong number of arguments.\n");
        return 0;
    }
    
    FILE *output_stream = fopen(argv[1], "r");
    if (output_stream == NULL) {
        perror(argv[1]);
        return 1;
    }
    long int i = 0;
    int character = 0;
    do {
        character = fgetc(output_stream);
        if (character == EOF) {
            break;
        }
        printf("byte %4ld: %3d 0x%02x", i, character, character);
        if (isprint(character) != 0) {
            printf(" '%c'", character);
        }
        printf("\n");
        i++;
    }
    while (character != EOF);
    
    

    // fclose will flush data to file
    // best to close file ASAP
    // but doesn't matter as file autoamtically closed on exit
    fclose(output_stream);

    return 0;
}

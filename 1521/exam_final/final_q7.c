#include <stdio.h>

// copy file specified as command line argument 1 to
// file specified as command line argument 2
// convert UTF8 to ASCII by replacing multibyte UTF8 characters with '?'

int main(int argc, char *argv[]) {

    FILE *input_stream = fopen(argv[1], "r");
    if (input_stream == NULL) {
        perror(argv[1]);
        return 1;
    }
    
    FILE *output_stream = fopen(argv[2], "w");
    if (output_stream == NULL) {
        perror(argv[1]);
        return 1;
    }
    int c = 0;
    while ((c = fgetc(input_stream)) != EOF) {
        if ((c & 240) == 240) {
            c = 63;
            fseek(input_stream, 3, SEEK_CUR);
        }
        else if ((c & 224) == 224) {
            c = 63;
            fseek(input_stream, 2, SEEK_CUR);
        }
        else if ((c & 192) == 192) {
            c = 63;
            fseek(input_stream, 1, SEEK_CUR);
        }
        fputc(c, output_stream);
    }

    return 0;
}

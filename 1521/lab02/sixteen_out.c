// Convert a 16-bit signed integer to a string of binary digits

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

#define N_BITS 16

char *sixteen_out(int16_t value);

int main(int argc, char *argv[]) {

    for (int arg = 1; arg < argc; arg++) {
        long l = strtol(argv[arg], NULL, 0);
        assert(l >= INT16_MIN && l <= INT16_MAX);
        int16_t value = l;

        char *bits = sixteen_out(value);
        printf("%s\n", bits);

        free(bits);
    }

    return 0;
}

// given a signed 16 bit integer
// return a null-terminated string of 16 binary digits ('1' and '0')
// storage for string is allocated using malloc
char *sixteen_out(int16_t value) {
    
    char *string_pointer;
    string_pointer = malloc(sizeof(char[17]));
    string_pointer[16] = '\0';
    
    for (int i = 0; i < 16; i++) {
        if ((value & (1 << i)) == 1 << i) {
            string_pointer[15 - i] = '1';
        }
        else {
            string_pointer[15 - i] = '0';
        }
    }

    return string_pointer;

}


#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>

int main(void) {
    
    while (1 == 1) {
        char character = 0;
        while (character != '\n') {
            character = getchar();
            if (character == -1) {
                exit(0);
            }
            else if (character > 64 && character < 91) {
                character = character + 32;
                putchar(character);
            }
            else {
                putchar(character);
            }
        }
    }
	return 0;
}

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main(void) {

    while (1 == 1) {
        char character = 0;
        while (character != '\n') {
            int exi = scanf("%c", &character);
            if (exi == -1) {
                exit(0);
            }
            else if (character != 'A' &&
                character != 'E' &&
                character != 'I' &&
                character != 'O' &&
                character != 'U' &&
                character != 'a' &&
                character != 'e' &&
                character != 'i' &&
                character != 'o' &&
                character != 'u') {
                printf("%c", character); 
            }
        }
    }
    
	return 0;
}

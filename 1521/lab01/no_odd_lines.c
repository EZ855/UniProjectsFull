#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main(void) {
    
    while (1 == 1) {
        char string[1024];
        char *exi = fgets(string, 1024, stdin);
        if (exi == NULL) {
            exit(0);
        }
        else if (strlen(string) % 2 == 0) {
            fputs(string, stdout);
        }
    }
	return 0;
}

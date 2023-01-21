// z5383657
// Evan Zhang
// 18/07/2021

#include <stdio.h>
#include <stdlib.h>

#define CAPITAL 999
#define LOWERCASE 998

int what_case(int character);

int main(int argc, char *argv[]){

    

    int character = getchar();
    while (character != EOF) {
        if (what_case(character) == CAPITAL) {
            putchar(argv[1][character - 'A'] - 32);
        }
        else if (what_case(character) == LOWERCASE) {
            putchar(argv[1][character - 'a']);
        }
        else {
            putchar(character); 
        }
        character = getchar();   
    }
    return 0;
}

int what_case(int character) {
    if (character >= 'A' && character <= 'Z') {
        return CAPITAL; 
    }
    else if (character >= 'a' && character <= 'z') {
        return LOWERCASE;
    }
    else {
        return 0;
    }
}

// z5383657
// Evan Zhang
// 18/07/2021

#include <stdio.h>
#define NOT_VOWEL 1
#define IS_VOWEL 0


int is_vowel(int character);

int main(void){
    int character = getchar();
    while (character != EOF) {
        if (is_vowel(character) == NOT_VOWEL) {
            putchar(character);     
        }
        character = getchar();   
    }
    return 0;
}

int is_vowel(int character) {
    if (character == 'a' || character == 'e' || character == 'i') {
        return IS_VOWEL;
    }
    else if (character == 'o' || character == 'u') {
        return IS_VOWEL;
    }
    else {
        return NOT_VOWEL;
    }
}

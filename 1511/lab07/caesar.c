// z5383657
// Evan Zhang
// 18/07/2021

#include <stdio.h>
#include <stdlib.h>


int encrypt(int character, int shift);

int main(int argc, char *argv[]){

    int shift = atoi(argv[1]) % 26;

    int character = getchar();
    while (character != EOF) {
        putchar(encrypt(character, shift));
        character = getchar();   
    }
    return 0;
}

int encrypt(int character, int shift) {
    if (character >= 'A' && character <= 'Z') {
        if (shift < 0) {
            if (shift + character + 26 > 'Z') {
                return (shift + character + 26) % 'Z' + 'A' - 1;
            }
            else {
                return shift + character + 26;
            }
        }
        else if (shift > 0) {
            if (shift + character > 'Z') {
                return (shift + character) % 'Z' + 'A' - 1;
            }
            else {
                return shift + character;
            }
        }
    }
    if (character >= 'a' && character <= 'z') {
        if (shift < 0) {
            if (shift + character + 26 > 'z') {
                return (shift + character + 26) % 'z' + 'a' - 1;
            }
            else {
                return shift + character + 26;
            }
        }
        else if (shift > 0) {
            if (shift + character > 'z') {
                return (shift + character) % 'z' + 'a' - 1;
            }
            else {
                return shift + character;
            }
        }
    }
    else {
        return character;
    }
    return character;
}

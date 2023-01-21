// z5383657
// Evan Zhang
// 18/07/2021

#include <stdio.h>

int swap_case(int character);

int main(void){
    int character = getchar();
    while (character != EOF) {
        putchar(swap_case(character));
        character = getchar();   
    }
    return 0;
}

int swap_case(int character) {
    if (character >= 'A' && character <= 'Z') {
        return character - 'A' + 'a'; 
    }
    else if (character >= 'a' && character <= 'z') {
        return character - 'a' + 'A';
    }
    else {
        return character;
    }
}

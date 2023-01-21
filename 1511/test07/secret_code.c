// z5383657
// Evan Zhang
// 21/07/2021

#include <stdio.h>

int main (void) {
    
    char word[256] = {0};
    
    int letter1 = 0;
    int letter2 = 0;
    
    int i = 0;
    
    letter1 = getchar();
    
    while (letter2 != EOF && letter1 != EOF) {
        
        letter2 = getchar();
        
        if (letter1 >= letter2 && letter2 != EOF) {
            word[i] = letter2;
            i++;
        }
        else if (letter2 != EOF) {
            word[i] = letter1;
            i++;
        }
        
        if (letter2 != EOF) {
            letter1 = getchar();
        }
    }
        
    printf("%s\n", word);

    return 0;
}

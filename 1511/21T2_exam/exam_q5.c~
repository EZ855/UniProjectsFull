// exam_q5.c
//
// This program was written by z5383657
// on 18/08/2021


#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LINE_SIZE 1024 

int main(int argc, char *argv[]) {
  
    char input_word[MAX_LINE_SIZE] = {"example"};
    int i = 0;
    while (input_word[i] != EOF) {
        i = 0;
        input_word[i] = getchar();
        i++;
        while (input_word[i-1] != '\n'){
            input_word[i] = getchar();
            i++;
        }
        i--;
        input_word[i] = '\0';
        int j = 1;
        while (j < argc) {
            int k = 0;
            int argv_length = strlen(argv[j]);
            int l = 0;
 //           printf("%s", argv[j]);
            while (k < argv_length) {
                if (argv[j][k] == input_word[i - argv_length + k]) {
                   l++;
//                   printf("EL++");
                }
                k++;
            }
            if (l == argv_length) {
                printf("%s\n", input_word);
                j = argc;
            }
            j++;
        }
    } 
    return 0;
}

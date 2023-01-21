// COMP1511 Week 7 Test: Interject
//
// This program was written by EVAN ZHANG (z5383657)
// on 21/07/2021
//
// This program adds interjections to strings.

#include <stdio.h>

#define MAX_SIZE 1002

// Modify str so that it contains interject at the given index.
void interject(char *str, char *interject, int index) {
    int loop1 = 0;
    if (str[index] == '\0') {
        while (interject[loop1] != '\0') {
            str[index + loop1] = interject[loop1];
            loop1++;
            if (interject[loop1] == '\0') {
                str[index + loop1] = '\0';
            }
        }
    }
    else {
        int i = 0;
        while (interject[i] != '\0') {
            i++;
        }
        int j = 0;
        char storage[MAX_SIZE];
        while (str[index + j] != '\0') {
            storage[j] = str[index + j];
            j++;
            if (str[index + j] == '\0') {
                storage[j] = '\0';
            }
        }
        int loop = 0;
        while (interject[loop] != '\0') {
            str[index + loop] = interject[loop];
            loop++;
        }
        int l = 0;
        while (storage[l] != '\0') {
            str[index + i + l] = storage[l];
            l++;
            if (storage[l] == '\0') {
                str[index + i + l] = '\0';
            }
        }
    }    
}


// This is a simple main function that you can use to test your interject
// function.
// It will not be marked - only your interject function will be marked.
//
// Note: the autotest does not call this main function!
// It calls your interject function directly.
// Any changes that you make to this function will not affect the autotests.

int main(void) {
    char str1[MAX_SIZE] = "Comp Science";
    printf("%s -> ", str1);
    interject(str1, "uter", 12);
    printf("%s\n", str1);

    char str2[MAX_SIZE] = "Beginnings";
    printf("%s -> ", str2);
    interject(str2, "", 0);
    printf("%s\n", str2);

    char str3[MAX_SIZE] = "The End!";
    printf("%s -> ", str3);
    interject(str3, " Is Nigh", 7);
    printf("%s\n", str3);

    char str4[MAX_SIZE] = "UNSW Other Unis";
    printf("%s -> ", str4);
    interject(str4, "> ", 5);
    printf("%s\n", str4);

    return 0;
}

// String Equality
// Created by Evan Zhang
// z5383657
// Created on 27/07/2021

#include <stdio.h>
#include <assert.h>
// You cannot include string.h in this file.


int strings_equal(char *string1, char *string2);

int main(int argc, char *argv[]) {

    // Some simple assert-based tests.
    // You probably want to write some more.
    assert(strings_equal("", "") == 1);
    assert(strings_equal(" ", "") == 0);
    assert(strings_equal("", " ") == 0);
    assert(strings_equal(" ", " ") == 1);
    assert(strings_equal("\n", "\n") == 1);
    assert(strings_equal("This is 17 bytes.", "") == 0);
    assert(strings_equal("", "This is 17 bytes.") == 0);
    assert(strings_equal("This is 17 bytes.", "This is 17 bytes.") == 1);
    assert(strings_equal("Here are 18 bytes!", "This is 17 bytes.") == 0);

    printf("All tests passed.  You are awesome!\n");

    return 0;
}


// Takes two strings, and if they are the same,
// returns 1, or 0 otherwise.
int strings_equal(char *string1, char *string2) {
    int i = 0;
    while (string1[i] != '\0' && string2[i] != '\0') {
        if (string1[i] != string2[i]) {
            return 0;
        }
        i++;
    }
    if (string1[i] != string2[i]) {
        return 0;
    }
    else {
        return 1;
    }
}



#include <stdio.h>

// read two integers and print all the integers which have their bottom 2 bits set.

int main(void) {
    int x, y;

    scanf("%d", &y);
    scanf("%d", &x);

    // PUT YOUR CODE HERE
    
    y++;
    
    while (y < x) {
        if ((y & 3) == 3) {
            printf("%d\n", y);
        }
        y++;
    }

    return 0;
}

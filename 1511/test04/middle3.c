//z5383657
//Evan Zhang
//01/07/2021

#include <stdio.h>

int main(void) {
    
    int user_integer1;
    int user_integer2;
    int user_integer3;
    
    printf("Enter integer: ");
    scanf("%d", &user_integer1);
    
    printf("Enter integer: ");
    scanf("%d", &user_integer2);
    
    printf("Enter integer: ");
    scanf("%d", &user_integer3);

    if (user_integer1 > user_integer2 && user_integer2 > user_integer3) {
        printf("Middle: %d\n", user_integer2);
    }
    else if (user_integer3 > user_integer2 && user_integer2 > user_integer1) {
        printf("Middle: %d\n", user_integer2);
    }
    else if (user_integer2 > user_integer3 && user_integer3 > user_integer1) {
        printf("Middle: %d\n", user_integer3);
    }
    else if (user_integer1 > user_integer3 && user_integer3 > user_integer2) {
        printf("Middle: %d\n", user_integer3);
    }
    else if (user_integer3 > user_integer1 && user_integer1 > user_integer2) {
        printf("Middle: %d\n", user_integer1);
    }
    else if (user_integer2 > user_integer1 && user_integer1 > user_integer3) {
        printf("Middle: %d\n", user_integer1);
    }
    else if (user_integer1 == user_integer2 && user_integer2 > user_integer3) {
        printf("Middle: %d\n", user_integer2);
    }
    else if (user_integer1 == user_integer2 && user_integer2 < user_integer3) {
        printf("Middle: %d\n", user_integer2);
    }
    else if (user_integer3 == user_integer2 && user_integer2 > user_integer3) {
        printf("Middle: %d\n", user_integer2);
    }
    else if (user_integer3 == user_integer2 && user_integer2 < user_integer3) {
        printf("Middle: %d\n", user_integer2);
    }
    else if (user_integer3 == user_integer1 && user_integer1 > user_integer2) {
        printf("Middle: %d\n", user_integer1);
    }
    else if (user_integer3 == user_integer1 && user_integer1 < user_integer2) {
        printf("Middle: %d\n", user_integer1);
    }
    else if (user_integer1 == user_integer2 && user_integer2 == user_integer3) {
        printf("Middle: %d\n", user_integer2);
    }

    return 0;
}

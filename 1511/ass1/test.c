#include <stdio.h>
#define SIZE 10
void test();
void testtest(int array[SIZE]);

int main (void){
    int array[SIZE] = {0};
    test();
    printf("%d", array[0]);
    return 0;
}

void test(){
    if (SIZE == 10) {
        printf("%d", SIZE);
    }
}

void testtest(int array[SIZE]){
    array[0] = 1;
}

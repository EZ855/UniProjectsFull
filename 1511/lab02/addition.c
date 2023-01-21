#include <stdio.h>

int main(void){
    int students;
    int tutors;
    printf("Please enter the number of students and tutors:");
    scanf("%d %d", &students, &tutors);
    int total = students + tutors;
    printf("%d + %d = %d\n", students, tutors, total);
    return 0;
}

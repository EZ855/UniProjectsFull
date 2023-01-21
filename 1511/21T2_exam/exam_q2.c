// exam_q2.c
//
// This program was written z5383657
// on 18/08/2021

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

struct node {
    struct node *next;
    int          data;
};

// compare should return -1 if head1 is a shorter list than head2
// compare should return  1 if head1 is a longer list than head2
// compare should return  0 if head1 is the same length as head2
int compare(struct node *head1, struct node *head2) {

    int list1_length = 0;
    int list2_length = 0;
    
    struct node *current = head1;
    while (current != NULL) {
        list1_length++;
        current = current->next;
    }
    
    current = head2;
    while (current != NULL) {
        list2_length++;
        current = current->next;
    }
    
    if (list1_length < list2_length) {
        return -1;
    }
    else if (list1_length == list2_length) {
        return 0;
    }
    else {
        return 1;
    }
}


///////////////////////////////////////////////////
//
// YOU DO NOT NEED TO UNDERSTAND CODE BELOW THIS COMMENT
// 
// DO NOT CHANGE CODE BELOW THIS COMMENT
//
// IF YOU THINK YOU NEED TO CHANGE ANYTHING BELOW THIS COMMENT,
// YOU HAVE MISUNDERSTOOD THE QUESTION
//
///////////////////////////////////////////////////

struct node *strings_to_list(int len, char *strings[]);

// DO NOT CHANGE THIS MAIN FUNCTION
int main(int argc, char *argv[]) {
    // create two linked lists from command line arguments
    int dash_arg = argc - 1;
    while (dash_arg > 0 && strcmp(argv[dash_arg], "-") != 0) {
        dash_arg = dash_arg - 1;
    }
    struct node *head1 = strings_to_list(dash_arg - 1, &argv[1]);
    struct node *head2 = strings_to_list(argc - dash_arg - 1, &argv[dash_arg + 1]);

    int result = compare(head1, head2);
    printf("%d\n", result);

    return 0;
}


// DO NOT CHANGE THIS FUNCTION
// create linked list from array of strings
struct node *strings_to_list(int len, char *strings[]) {
    struct node *head = NULL;
    for (int i = len - 1; i >= 0; i = i - 1) {
        struct node *n = malloc(sizeof (struct node));
        assert(n != NULL);
        n->next = head;
        n->data = atoi(strings[i]);
        head = n;
    }
    return head;
}

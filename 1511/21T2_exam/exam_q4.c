// z5383657
// 18/08/2021

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

struct node {
    struct node *next;
    int          data;
};


// fix_counting is given a pointer to a linked list containing consecutive integers
// except there may be one missing integer
// fix_counting should return a pointer to a list
// with the missing integer added (if there was a missing integer)
struct node *fix_counting(struct node *head) {

    struct node *current = head;
    struct node *previous = NULL;
    if (current == NULL || current->next == NULL) {
        return head;
    }
    else {
        previous = current;
        current = current->next;
    }
    while (current != NULL) {
        if (previous->data + 1 != current->data) {
            struct node *new_node = malloc(sizeof(struct node));
            new_node->data = previous->data + 1;
            previous->next = new_node;
            new_node->next = current;
            previous = new_node;
        }
        else {
            previous = current;
            current = current->next;
        }
        
    }

    return head;
}


///////////////////////////////////////////////////////////////
//
// YOU DO NOT NEED TO UNDERSTAND CODE BELOW THIS COMMENT
// 
// DO NOT CHANGE CODE BELOW THIS COMMENT
//
// IF YOU THINK YOU NEED TO CHANGE ANYTHING BELOW THIS COMMENT,
// YOU HAVE MISUNDERSTOOD THE QUESTION
//
///////////////////////////////////////////////////////////////

// DO NOT CHANGE THIS MAIN FUNCTION

struct node *strings_to_list(int len, char *strings[]);
void print_list(struct node *head);

int main(int argc, char *argv[]) {

    // create linked list from command line arguments
    struct node *head = NULL;
    if (argc > 1) {
        // list has elements
        head = strings_to_list(argc - 1, &argv[1]);
    }

    struct node *new_head = fix_counting(head);
    print_list(new_head);

    return 0;
}


// DO NOT CHANGE THIS FUNCTION
// create linked list from array of strings
struct node *strings_to_list(int len, char *strings[]) {
    struct node *head = NULL;
    int i = len - 1;
    while (i >= 0) {
        struct node *n = malloc(sizeof (struct node));
        assert(n != NULL);
        n->next = head;
        n->data = atoi(strings[i]);
        head = n;
        i -= 1;
    }   
    return head;
}

// DO NOT CHANGE THIS FUNCTION
// print linked list
void print_list(struct node *head) {
    printf("[");    
    struct node *n = head;
    while (n != NULL) {
        // If you're getting an error here,
        // you have returned an invalid list
        printf("%d", n->data);
        if (n->next != NULL) {
            printf(", ");
        }
        n = n->next;
    }
    printf("]\n");
}

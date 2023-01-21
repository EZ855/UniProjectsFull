
#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "list.h"

// Worst case time complexity of this solution: O(n + m)
List zipList(List l1, int x, List l2, int y) {
    // TODO

    List new_ls = ListNew();

    Node l1_curr = l1->first;
    Node l2_curr = l2->first;

    // If both lists are empty
    if (l1_curr == NULL && l2_curr == NULL) {
        return new_ls;
    }
    // If l1 is empty, copies over l2 into new_ls and returns it
    else if (l1_curr == NULL && y != 0) {

        new_ls->first = newNode(l2_curr->value);
        new_ls->last = new_ls->first;
        Node new_ls_curr = new_ls->first;
        l2_curr = l2_curr->next;

        while (l2_curr != NULL) {
            new_ls_curr->next = newNode(l2_curr->value);
            new_ls_curr = new_ls_curr->next;
            new_ls->last = new_ls_curr;
            l2_curr = l2_curr->next;
        }
        return new_ls;
    }
    // If l2 is empty, copies over l1 into new_ls and returns it
    else if (l2_curr == NULL && x != 0) {
        new_ls->first = newNode(l1_curr->value);
        new_ls->last = new_ls->first;
        Node new_ls_curr = new_ls->first;
        l1_curr = l1_curr->next;

        while (l1_curr != NULL) {
            new_ls_curr->next = newNode(l1_curr->value);
            new_ls_curr = new_ls_curr->next;
            new_ls->last = new_ls_curr;
            l1_curr = l1_curr->next;
        }
        return new_ls;
    }
    // If l1 and l2 aren't empty
    int first = 0;
    Node new_ls_curr = NULL;
    if (x != 0) {
        new_ls->first = newNode(l1_curr->value);
        new_ls->last = new_ls->first;
        new_ls_curr = new_ls->first;
        l1_curr = l1_curr->next;
        first = 1;
    }
    else if (y != 0) {
        new_ls->first = newNode(l2_curr->value);
        new_ls->last = new_ls->first;
        new_ls_curr = new_ls->first;
        l2_curr = l2_curr->next;
        first = 1;
    }
    else {
        return new_ls;
    }
    
    while (l1_curr != NULL && l2_curr != NULL) {
        for (int i = 0; i < x; i++) {
            if (first == 1) {
                first = 0;
            }
            else {
                new_ls_curr->next = newNode(l1_curr->value);
                new_ls_curr = new_ls_curr->next;
                new_ls->last = new_ls_curr;
                l1_curr = l1_curr->next;
                if (l1_curr == NULL) {
                    break;
                }
            }
        }
        if (l1_curr == NULL) {
            break;
        }
        for (int i = 0; i < y; i++) {
            if (first == 1) {
                first = 0;
            }
            new_ls_curr->next = newNode(l2_curr->value);
            new_ls_curr = new_ls_curr->next;
            new_ls->last = new_ls_curr;
            l2_curr = l2_curr->next;
            if (l2_curr == NULL) {
                break;
            }
        }
        if (l2_curr == NULL) {
            break;
        }
    }

    // If both lists are empty
    if (l1_curr == NULL && l2_curr == NULL) {
        return new_ls;
    }
    // If l1 is empty, copies over l2 into new_ls and returns it
    else if (l1_curr == NULL && y != 0) {
        while (l2_curr != NULL) {
            new_ls_curr->next = newNode(l2_curr->value);
            new_ls_curr = new_ls_curr->next;
            new_ls->last = new_ls_curr;
            l2_curr = l2_curr->next;
        }
        return new_ls;
    }
    // If l2 is empty, copies over l1 into new_ls and returns it
    else if (l2_curr == NULL && x != 0) {
        while (l1_curr != NULL) {
            new_ls_curr->next = newNode(l1_curr->value);
            new_ls_curr = new_ls_curr->next;
            new_ls->last = new_ls_curr;
            l1_curr = l1_curr->next;
        }
        return new_ls;
    }
    return new_ls;
}


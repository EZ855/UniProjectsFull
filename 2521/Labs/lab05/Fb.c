// Implementation of the FriendBook ADT

#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Fb.h"
#include "List.h"
#include "Map.h"
#include "Queue.h"

#define DEFAULT_CAPACITY 1 // !!! DO NOT CHANGE THIS !!!

struct fb {
    int    numPeople;
    int    capacity;

    char **names;    // the id of a person is simply the index
                     // that contains their name in this array
    
    Map    nameToId; // maps names to ids

    bool **friends;
};

static void  increaseCapacity(Fb fb);
static char *myStrdup(char *s);
static int   nameToId(Fb fb, char *name);

////////////////////////////////////////////////////////////////////////

// Creates a new instance of FriendBook
Fb   FbNew(void) {
    Fb fb = malloc(sizeof(*fb));
    if (fb == NULL) {
        fprintf(stderr, "error: out of memory\n");
        exit(EXIT_FAILURE);
    }

    fb->numPeople = 0;
    fb->capacity = DEFAULT_CAPACITY;
    
    fb->names = calloc(fb->capacity, sizeof(char *));
    if (fb->names == NULL) {
        fprintf(stderr, "error: out of memory\n");
        exit(EXIT_FAILURE);
    }
    
    fb->nameToId = MapNew();

    fb->friends = malloc(fb->capacity * sizeof(bool *));
    if (fb->friends == NULL) {
        fprintf(stderr, "error: out of memory\n");
        exit(EXIT_FAILURE);
    }
    for (int i = 0; i < fb->capacity; i++) {
        fb->friends[i] = calloc(fb->capacity, sizeof(bool));
        if (fb->friends[i] == NULL) {
            fprintf(stderr, "error: out of memory\n");
            exit(EXIT_FAILURE);
        }
    }

    return fb;
}

void FbFree(Fb fb) {
    for (int i = 0; i < fb->capacity; i++) {
        free(fb->friends[i]);
    }
    free(fb->friends);

    MapFree(fb->nameToId);

    for (int i = 0; i < fb->numPeople; i++) {
        free(fb->names[i]);
    }
    free(fb->names);
    
    free(fb);
}

bool FbAddPerson(Fb fb, char *name) {
    if (fb->numPeople == fb->capacity) {
        increaseCapacity(fb);
    }

    if (!MapContains(fb->nameToId, name)) {
        int id = fb->numPeople++;
        fb->names[id] = myStrdup(name);
        MapSet(fb->nameToId, name, id);
        return true;
    } else {
        return false;
    }
}

static void increaseCapacity(Fb fb) {
    int newCapacity = fb->capacity * 2;
    
    fb->names = realloc(fb->names, newCapacity * sizeof(char *));
    if (fb->names == NULL) {
        fprintf(stderr, "error: out of memory\n");
        exit(EXIT_FAILURE);
    }
    for (int i = fb->capacity; i < newCapacity; i++) {
        fb->names[i] = NULL;
    }
    
    fb->friends = realloc(fb->friends, newCapacity * sizeof(bool *));
    if (fb->friends == NULL) {
        fprintf(stderr, "error: out of memory\n");
        exit(EXIT_FAILURE);
    }
    for (int i = 0; i < fb->capacity; i++) {
        fb->friends[i] = realloc(fb->friends[i], newCapacity * sizeof(bool));
        if (fb->friends[i] == NULL) {
            fprintf(stderr, "error: out of memory\n");
            exit(EXIT_FAILURE);
        }
        for (int j = fb->capacity; j < newCapacity; j++) {
            fb->friends[i][j] = false;
        }
    }
    for (int i = fb->capacity; i < newCapacity; i++) {
        fb->friends[i] = calloc(newCapacity, sizeof(bool));
        if (fb->friends[i] == NULL) {
            fprintf(stderr, "error: out of memory\n");
            exit(EXIT_FAILURE);
        }
    }
    
    fb->capacity = newCapacity;
}

bool FbHasPerson(Fb fb, char *name) {
    return MapContains(fb->nameToId, name);
}

List FbGetPeople(Fb fb) {
    List l = ListNew();
    for (int id = 0; id < fb->numPeople; id++) {
        ListAppend(l, fb->names[id]);
    }
    return l;
}

bool FbFriend(Fb fb, char *name1, char *name2) {
    int id1 = nameToId(fb, name1);
    int id2 = nameToId(fb, name2);
    assert(id1 != id2);

    if (!fb->friends[id1][id2]) {
        fb->friends[id1][id2] = true;
        fb->friends[id2][id1] = true;
        return true;
    } else {
        return false;
    }
}

bool FbIsFriend(Fb fb, char *name1, char *name2) {
    int id1 = nameToId(fb, name1);
    int id2 = nameToId(fb, name2);
    return fb->friends[id1][id2];
}

int  FbNumFriends(Fb fb, char *name) {
    int id1 = nameToId(fb, name);
    
    int numFriends = 0;
    for (int id2 = 0; id2 < fb->numPeople; id2++) {
        if (fb->friends[id1][id2]) {
            numFriends++;
        }
    }
    return numFriends;
}

////////////////////////////////////////////////////////////////////////
// Your tasks

bool FbUnfriend(Fb fb, char *name1, char *name2) {
    // TODO: Complete this function
    // Checking if fb is NULL or too small for unfriending.
    if (fb == NULL || fb->numPeople == 0 || fb->numPeople == 1) {
        return false;
    }
    // Checking if the names given exist in fb.
    if (!MapContains(fb->nameToId, name1) || ! MapContains(fb->nameToId, name2)) {
        return false;
    }
    // Getting indexes of given names.
    int index_1 = MapGet(fb->nameToId, name1);
    int index_2 = MapGet(fb->nameToId, name2);
    
    // Going into friend array and checking if given names are friends.
    if (fb->friends[index_1][index_2] == 1) {
        fb->friends[index_1][index_2] = 0;
        fb->friends[index_2][index_1] = 0;
        return true;
    }
    else {
        return false;
    }
}

List FbMutualFriends(Fb fb, char *name1, char *name2) {
    // TODO: Complete this function
    List l = ListNew();
    // Checking if names exist in fb.
    if (!FbHasPerson(fb, name1) || !FbHasPerson(fb, name2)) {
        return l;
    }
    // Getting index of name1.
    int index_1 = MapGet(fb->nameToId, name1);
    // Iterating through entire list of people.
    for (int i = 0; i < fb->numPeople; i++) {
    // If person is friends with name1, goes on to check if they are friends with name2.
        if (fb->friends[index_1][i]) {
            char *name3 = fb->names[i];
            if (FbIsFriend(fb, name2, name3)) {
            ListAppend(l, name3);
            }
        }
    }
    return l;
}

void FbFriendRecs1(Fb fb, char *name) {
    // TODO: Complete this function
    // Checking if name exist in fb.
    if (!FbHasPerson(fb, name)) {
        return;
    }
    // Getting index of name.
    int index_1 = MapGet(fb->nameToId, name);    
    // Iterating through numPeople -1 to 1 inclusive.
    for (int i = (fb->numPeople - 1); i > 0; i--) {
        // Iterating through entire list of people.
        for (int j = 0; j < fb->numPeople; j++) {
        // If current person is not friends with name and also is not name, gets how many mutuals they have.
            if (!fb->friends[index_1][j] && !(j == index_1)) {
                char *name2 = fb->names[j];
                int num_mutuals = ListSize(FbMutualFriends(fb, name, name2));
                // Forces printing from highest to lowest.
                if (num_mutuals == i) {
                    printf("\t%-20s%4d mutual friends\n", name2, num_mutuals);
                }
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////
// Optional task

void FbFriendRecs2(Fb fb, char *name) {
    // TODO: Complete this function
}

////////////////////////////////////////////////////////////////////////
// Helper Functions

static char *myStrdup(char *s) {
    char *copy = malloc((strlen(s) + 1) * sizeof(char));
    if (copy == NULL) {
        fprintf(stderr, "error: out of memory\n");
        exit(EXIT_FAILURE);
    }
    return strcpy(copy, s);
}

// Converts a name to an ID. Raises an error if the name doesn't exist.
static int nameToId(Fb fb, char *name) {
    if (!MapContains(fb->nameToId, name)) {
        fprintf(stderr, "error: person '%s' does not exist!\n", name);
        exit(EXIT_FAILURE);
    }
    return MapGet(fb->nameToId, name);
}


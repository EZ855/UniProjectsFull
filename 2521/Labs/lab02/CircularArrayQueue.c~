// Implementation of the Queue ADT using a circular array

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include "Queue.h"

#define DEFAULT_SIZE 16 // DO NOT change this line

// DO NOT modify this struct
struct queue {
	Item *items;
	int size;
	int capacity;
	int frontIndex;
};

static void increaseCapacity(Queue q);

/**
 * Creates a new empty queue
 */
Queue QueueNew(void) {
	Queue q = malloc(sizeof(*q));
	if (q == NULL) {
		fprintf(stderr, "couldn't allocate Queue");
		exit(EXIT_FAILURE);
	}

	q->items = malloc(DEFAULT_SIZE * sizeof(Item));
	if (q->items == NULL) {
		fprintf(stderr, "couldn't allocate Queue");
		exit(EXIT_FAILURE);
	}

	q->size = 0;
	q->capacity = DEFAULT_SIZE;
	q->frontIndex = 0;
	return q;
}

/**
 * Frees all resources associated with the given queue
 */
void QueueFree(Queue q) {
	free(q->items);
	free(q);
}

/**
 * Adds an item to the end of the queue
 */
void QueueEnqueue(Queue q, Item it) {
	// TODO
	// Makes sure there is enough room in the array.
	if (q->size == q->capacity) {
		increaseCapacity(q);
	}
    // Makes sure the index for the next item is within the array and adds item to end.
	if (q->frontIndex + q->size >= q->capacity) {
    	int new_index = (q->frontIndex + q->size) % q->capacity;
    	q->items[new_index] = it;
	}
	else {
    	q->items[q->frontIndex + q->size] = it;
	}
	q->size += 1;
	
}

/**
 * Removes an item from the front of the queue and returns it
 * Assumes that the queue is not empty
 */
Item QueueDequeue(Queue q) {
	// TODO
	// Makes sure queue is not empty.
	assert(q->size > 0);

    // Gets the first item in the queue.
	Item it = q->items[q->frontIndex];
	// Reset the first item to zero.
	q->items[q->frontIndex] = 0;
	q->size--;
	
	// Makes sure the new frontIndex is in the array/moves it up by one.
	if (q->frontIndex == q->capacity - 1) {
    	q->frontIndex = 0;
	}
	else {
    	q->frontIndex += 1;
	}
	
	return it;
}

/**
 * Doubles the capacity of the queue
 */
static void increaseCapacity(Queue q) {

	q->capacity *= 2;
	q->items = realloc(q->items, q->capacity * sizeof(Item));
	if (q->items == NULL) {
		fprintf(stderr, "couldn't resize Queue\n");
		exit(EXIT_FAILURE);
	}
	int j = 0;
	// From the first item in the queue, moves each one up to the end of the array.
	for (int i = q->frontIndex; i < q->capacity/2; i++) {
    	q->items[q->capacity/2 + i] = q->items[i];
    	q->items[i] = 0;
    	j++;
	}
}

/**
 * Gets the item at the front of the queue without removing it
 * Assumes that the queue is not empty
 */
Item QueueFront(Queue q) {
	assert(q->size > 0);

	return q->items[q->frontIndex];
}

/**
 * Gets the size of the given queue
 */
int QueueSize(Queue q) {
	return q->size;
}

/**
 * Returns true if the queue is empty, and false otherwise
 */
bool QueueIsEmpty(Queue q) {
	return q->size == 0;
}

/**
 * Prints the queue to the given file with items space-separated
 */
void QueueDump(Queue q, FILE *fp) {
	for (int i = q->frontIndex, j = 0; j < q->size; i = (i + 1) % q->capacity, j++) {
		fprintf(fp, "%d ", q->items[i]);
	}
	fprintf(fp, "\n");
}

/**
 * Prints out information for debugging
 */
void QueueDebugPrint(Queue q) {
}


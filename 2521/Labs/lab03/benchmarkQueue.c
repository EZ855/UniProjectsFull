
#include <stdio.h>
#include <stdlib.h>

#include "Queue.h"

int main(void) {
	Queue q = QueueNew();

	// TODO
	// Write a benchmark test to demonstrate the difference between
	// ArrayQueue and CircularArrayQueue
	
    // enqueue 1 to 40000
    // enqueueing a bunch so we can dequeue a bunch.
	for (int i = 1; i <= 40000; i++) {
		QueueEnqueue(q, i);
	}

	// dequeue 1 to 40000
	// dequeueing a bunch would just build up time saved for CircularArrayQueue.
	for (int j = 1; j <= 40000; j++) {
		QueueDequeue(q);
	}


	QueueFree(q);
}


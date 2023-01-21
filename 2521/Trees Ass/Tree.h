#include "invertedIndex.h"
// Helper function for InsertTfIdfNode - finds whether two idfs are equal or not.
int compare_float(double x, double y, double epsilon);

// Finds matching word node given a word and returns it. Returns NULL otherwise.
InvertedIndexBST FindWordNode(char *w, InvertedIndexBST tree);

// If word doesn't exist in BST, inserts it in order at leaf node.
void InsertWordNode(char *w, InvertedIndexBST tree, InvertedIndexBST wn);

// Returns matching file node or NULL if matching file node not found
FileList FindFileNode(FileList F, char *fn);

// Inserts file node in alphabetical order into a file list, returns file list with added node
FileList InsertFileNode(FileList F, char *fn, FileList FN);

// Navigates to every word node in order, goes through it's file list
// finds the matching file node and divides it's tf by the word count.
void NavigateAndDivide(InvertedIndexBST t, char *fn, int count);

// Inserts file node in descending order of tfIdfSum and if equal to a range of 0.000001, will insert
// in alphabetical order instead. Returns list with inserted item.
// Assumes non-empty list
TfIdfList InsertTfIdfNode(TfIdfList L, char *fn, TfIdfList TIN);

// Returns matching tfidf node or NULL if matching node not found
TfIdfList FindTfIdfNode(TfIdfList L, char *fn);

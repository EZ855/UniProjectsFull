#include "bit_rotate.h"

// return the value bits rotated left n_rotations
uint16_t bit_rotate(int n_rotations, uint16_t bits) {
    
    int true_rotations = n_rotations % 16;
    if (true_rotations < 0) {
        true_rotations = 16 + true_rotations;
    }
    uint16_t number = bits;
    
    uint16_t one_at_end = 0x8000;
    uint16_t extractor = 0x0000;
    for (int i = 0; i < true_rotations; i++) {
        extractor = extractor >> 1;
        extractor = extractor | one_at_end;
    }
    
    extractor = number & extractor;
    extractor = extractor >> (16 - true_rotations);
    number = number << true_rotations;
    number = number | extractor;
    
    return number;
}

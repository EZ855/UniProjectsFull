// swap pairs of bits of a 64-bit value, using bitwise operators

#include <assert.h>
#include <stdint.h>
#include <stdlib.h>

// return value with pairs of bits swapped
uint64_t bit_swap(uint64_t value) {

    uint64_t half1 = 0x5555555555555555;
    uint64_t half2 = 0xAAAAAAAAAAAAAAAA;
    
    half1 = half1 & value;
    half2 = half2 & value;
    
    half1 = half1 << 1;
    half2 = half2 >> 1;
    
    uint64_t new_number = half1 | half2;
    
        
    return new_number;
}

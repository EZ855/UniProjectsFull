// Swap bytes of a short

#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

// given uint16_t value return the value with its bytes swapped
uint16_t short_swap(uint16_t value) {


    
    uint16_t half1 = 0x00FF;
    uint16_t half2 = 0xFF00;
    
    half1 = half1 & value;
    half2 = half2 & value;
    
    half1 = half1 << 8;
    half2 = half2 >> 8;
    
    uint16_t new_number = half1 | half2;
    
        
    return new_number;
}

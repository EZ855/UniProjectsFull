#include "float_exp.h"

// given the 32 bits of a float return the exponent
uint32_t float_exp(uint32_t f) {

    uint32_t extractor = 0x7F800000;
    extractor = (extractor & f) >> 23;
    uint32_t number = 0xFF & extractor;
    
    return number;

}

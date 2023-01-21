// generate the opcode for an addi instruction

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

#include "addi.h"

// return the MIPS opcode for addi $t,$s, i
uint32_t addi(int t, int s, int i) {
    uint32_t blank = 0;
    uint32_t reg_t = t;
    uint32_t reg_s = s;
    uint16_t integer = i;
    uint32_t addi = 1;
    reg_s = reg_s << 21;
    reg_t = reg_t << 16;
    addi = addi << 29;
    blank |= integer;
    blank |= reg_t;
    blank |= reg_s;
    blank |= addi;
    
    return blank;
    
    

}

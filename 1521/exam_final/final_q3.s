# read an integer
# print 1 iff  the least significant bit is equal to the most significant bit
# print 0 otherwise

main:
    li   $v0, 5
    syscall
    
    move $t0, $v0 #x in $t0
    
    li $t1, 1
    
    and $t2, $t1, $t0   # $t2 now equals 1 if bottom is 1 or 0 if bottom is 0

    li $t1, -1
    
    and $t3, $t1, $t0 # $t3 now is either a positive number (0) if positive and a negative number (1) if negative
    li $t4, 0
if1:
    beqz $t2, if1_false
    bgt $t3, $t4, if1_false
    
    li $a0, 1
    li $v0, 1
    syscall
    li   $a0, '\n'
    li   $v0, 11
    syscall
    
    b end

if1_false:
    bnez $t2, if2_false
    blt $t3, $t4, if2_false
    
    li $a0, 1
    li $v0, 1
    syscall
    li   $a0, '\n'
    li   $v0, 11
    syscall
    
    b end
    
if2_false:

    li $a0, 0
    li $v0, 1
    syscall
    li   $a0, '\n'
    li   $v0, 11
    syscall

end:
    li $v0, 0
    jr $31
    


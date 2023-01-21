# read two integers and print all the integers which have their bottom 2 bits set.

main:
    li $v0, 5
    syscall
    move $t0, $v0   # $t0 = y;

    li $v0, 5
    syscall
    move $t1, $v0   # $t1 = x;

    addi $t0, $t0, 1
    li   $t2, 3
    
loop:
    bge $t0, $t1, end

    and $t3, $t0, $t2
if:
    bne $t3, $t2, if_false

    move $a0, $t0
    li $v0, 1
    syscall
    li   $a0, '\n'
    li   $v0, 11
    syscall


if_false:
    addi $t0, $t0, 1
    b loop

end:
    li $v0, 0
    jr $31

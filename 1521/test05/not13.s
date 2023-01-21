main:
    li   $v0, 5         #   scanf("%d", &x); $t0
    syscall             #
    move $t0, $v0

    li   $v0, 5         #   scanf("%d", &y); $t1
    syscall             #
    move $t1, $v0

    add  $t2, $t0, 1    #   int i = x + 1;   $t2
    li   $t3, 13        #   int k = 13
    
loop:
    bge  $t2, $t1, loop_post # while (i < y) {

if:
    beq  $t2, 13, if_post    # if (i !=13) {
    move $a0, $t2        #   printf("%d\n", i);
    li   $v0, 1
    syscall
    li   $a0, '\n'      #   printf("%c", '\n');
    li   $v0, 11
    syscall

if_post:                # }

    addi $t2, $t2, 1    # i++;
    b    loop

loop_post:              # }


    
end:

    li   $v0, 0         # return 0
    jr   $ra

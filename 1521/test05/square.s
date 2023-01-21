main:
    li   $v0, 5         #   scanf("%d", &x); $t0
    syscall             #
    move $t0, $v0

    li   $t1, 0         #   int i = 0;   $t1

loop0:
    bge  $t1, $t0, loop0_post # while (i < x) {
    
    li   $t2, 0         #   int j = 0;   $t2
loop1:
    bge  $t2, $t0, loop1_post # while (j < x) {
    
    li   $a0, '*'        #   printf("%c", '*');
    li   $v0, 11
    syscall

    addi $t2, $t2, 1    # j++;
    b loop1
loop1_post:

    li   $a0, '\n'      #   printf("%c", '\n');
    li   $v0, 11
    syscall
    
    addi $t1, $t1, 1    # i++;
    b loop0
loop0_post:


end:

    li   $v0, 0         # return 0
    jr   $ra

# read a mark and print the corresponding UNSW grade

main:
    la   $a0, prompt    # printf("Enter a mark: ");
    li   $v0, 4
    syscall

    li   $v0, 5         # scanf("%d", mark);
    syscall
    
    move $t0, $v0
    li   $t1, 50
    blt  $t0, $t1, print_FL
    
    li   $t1, 65
    blt  $t0, $t1, print_PS
    
    li   $t1, 75
    blt  $t0, $t1, print_CR
    
    li   $t1, 85
    blt  $t0, $t1, print_DN
    
    b    print_HD
    
    
print_FL:
    la   $a0, fl        # printf("FL\n");
    li   $v0, 4
    syscall
    
    b return
    
print_PS:
    la   $a0, ps        # printf("PS\n");
    li   $v0, 4
    syscall
    
    b return
    
print_CR:
    la   $a0, cr        # printf("CR\n");
    li   $v0, 4
    syscall
    
    b return
    
print_DN:
    la   $a0, dn        # printf("DN\n");
    li   $v0, 4
    syscall
    
    b return
    
print_HD:
    la   $a0, hd        # printf("HD\n");
    li   $v0, 4
    syscall
    
    b return

return:
    jr   $ra            # return

    .data
prompt:
    .asciiz "Enter a mark: "
fl:
    .asciiz "FL\n"
ps:
    .asciiz "PS\n"
cr:
    .asciiz "CR\n"
dn:
    .asciiz "DN\n"
hd:
    .asciiz "HD\n"

# read a line and print whether it is a palindrom

main:
    la   $a0, str0       # printf("Enter a line of input: ");
    li   $v0, 4
    syscall

    la   $a0, line
    la   $a1, 256
    li   $v0, 8          # fgets(buffer, 256, stdin)
    syscall              #

    li   $t0, 0
loop:
    la      $t1, line                                       #   
    add     $t1, $t1, $t0                                   # finds &line[n] puts into $t1
    lb      $t1, ($t1)
    beqz    $t1, end
    
    addi $t0, $t0, 1  
    
    b    loop

end:

    li   $t1, 0                                         # j = 0 in $t1
    addi $t2, $t0, -2                                   # k = i -2 in $t2

loop1:
    bge  $t1, $t2, end1
    
    la      $t3, line                                       #   
    add     $t3, $t3, $t1                                   # finds line[j] puts into $t3
    lb      $t3, ($t3)
    
    la      $t4, line                                       #   
    add     $t4, $t4, $t2                                   # finds line[k] puts into $t1
    lb      $t4, ($t4)
    
    beq     $t4, $t3, false
true:

    la   $a0, not_palindrome
    li   $v0, 4
    syscall
    
    li   $v0, 0          # return 0
    jr   $ra

false:
    
    addi $t1, $t1, 1  
    addi $t2, $t2, -1  
    b    loop1

end1:


    la   $a0, palindrome
    li   $v0, 4
    syscall

    li   $v0, 0          # return 0
    jr   $ra


.data
str0:
    .asciiz "Enter a line of input: "
palindrome:
    .asciiz "palindrome\n"
not_palindrome:
    .asciiz "not palindrome\n"


# line of input stored here
line:
    .space 256


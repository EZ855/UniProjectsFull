# this code reads a line of input and prints 42
# change it to check the string for brackets

# read a line of input and checks whether it consists only of balanced brackets
# if the line contains characters other than ()[]{} -1 is printed
# if brackets are not balance,  -1 is printed
# if the line contains only balanced brackets, length of the line is printed

main:
    la   $a0, line
    la   $a1, 1024
    li   $v0, 8          # fgets(line, 1024, stdin);
    syscall
    
    addiu   $sp, $sp, -4
    sw      $31, 0($sp)
    
    li   $a0, 0
    li   $a1, '\n'
    jal    check
    
    lw      $31, 0($sp)
    addiu   $sp, $sp, 4
    
    move $t0, $v0
    

    move $a0, $t0
    li $v0, 1
    syscall
    li   $a0, '\n'
    li   $v0, 11
    syscall


    li   $v0, 0          # return 0
    jr   $31


# PUT YOU FUNCTION DEFINITIONS HERE
check:
check_prologue:
    addiu   $sp, $sp, -4
    sw      $ra, 0($sp)
    
    addiu   $sp, $sp, -4
    sw      $s0, 0($sp)
    addiu   $sp, $sp, -4
    sw      $s1, 0($sp)
    
check_start:
    move $t0, $a0

    mul  $t1, $a0, 1     #   calculate &line[index]
    la   $t2, line       #
    add  $t3, $t1, $t2   #
    lw   $t4, ($t3)      #   $t4 is now c = line[index]    
    
check_if1:
    bne  $t4, $a1, check_if1_false
    
    addi $t0, $a0, 1    # index + 1 in $t0
    
    b check_if1_end
    
check_if1_false:
    move $s0, $a0   # index in $s0
    move $s1, $a1   # what in $s1
    
    jal match
    
    move $t0, $v0  # return value put into index in $t0

check_if2:
    blt  $t0, 0, check_if2_false

    move $s0, $t0  # index put into $s0
    move $a0, $t0  # index put into $a0
    move $a1, $s1  #  what put into $a1
    
    jal check
    
    move $t0, $v0  # return value put into index in $t0

check_if2_false:

check_if1_end:

    move $v0, $t0
        
check_epilogue:
    lw      $s1, 0($sp)
    addiu   $sp, $sp, 4
    lw      $s0, 0($sp)
    addiu   $sp, $sp, 4
    
    lw      $ra, 0($sp)
    addiu   $sp, $sp, 4

    jr      $ra


match:
match_prologue:
    addiu   $sp, $sp, -4
    sw      $ra, 0($sp)
    
    addiu   $sp, $sp, -4
    sw      $s0, 0($sp)
    addiu   $sp, $sp, -4
    sw      $s1, 0($sp)
    addiu   $sp, $sp, -4
    sw      $s2, 0($sp)
    
match_start:
    li      $t0, -1      #  $t0 = r
    
    mul  $t1, $a0, 1     #   calculate &line[index]
    la   $t2, line       #
    add  $t3, $t1, $t2   #
    lw   $t4, ($t3)      #   $t4 is now c = line[index]
    
    li   $t1, 0          #   $t1 = w
    
    
    li   $t2, '['
match_if:
    bne  $t2, $t4, match_if_false
    li   $t1, ']'
    b     match_if_end
    
    
    li   $t2, '('
match_if_false:
    bne  $t2, $t4, match_if_false1
    li   $t1, ')'
    b     match_if_end


    li   $t2, '{'
match_if_false1:
    bne  $t2, $t4, match_if_end
    li   $t1, '}'
    

match_if_end:
    
match_if1:
    beq  $t1, 0, match_if1_false
    
    move $s0, $t0   # r in $s0
    move $s1, $t4   # c in $s1
    move $s2, $t1   # w in $s2
    
    addi $a0, 1
    move $a1, $s2
    
    jal  check
    
    move $t0, $v0  # return value stored in r in $t0

match_if1_false:

    move $v0, $t0  # return r

match_epilogue:
    lw      $s2, 0($sp)
    addiu   $sp, $sp, 4
    lw      $s1, 0($sp)
    addiu   $sp, $sp, 4
    lw      $s0, 0($sp)
    addiu   $sp, $sp, 4
    
    lw      $ra, 0($sp)
    addiu   $sp, $sp, 4

    jr      $ra

.data
line:
    .byte 0:1024

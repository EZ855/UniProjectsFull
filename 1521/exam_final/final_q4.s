# read 10 numbers into an array

main:

    li   $t0, 0          # i = 0
loop0:
    bge  $t0, 10, end0   # while (i < 10) {

    li   $v0, 5          #   scanf("%d", &numbers[i]);
    syscall              #

    mul  $t1, $t0, 4     #   calculate &numbers[i]
    la   $t2, numbers    #
    add  $t3, $t1, $t2   #
    sw   $v0, ($t3)      #   store entered number in array

    add  $t0, $t0, 1     #   i++;
    b    loop0           # }
end0:



    li   $t0, 0          # i = 0
loop1:
    bge  $t0, 10, end1   # while (i < 10) {

    mul  $t1, $t0, 4     #   calculate &numbers[i]
    la   $t2, numbers    #
    add  $t3, $t1, $t2   #
    lw   $t4, ($t3)      #   $t4 is now numbers[i]
    
if0:
    ble  $t4, 0, if0_false
    
    move $a0, $t4
    li $v0, 1
    syscall
    li   $a0, ' '
    li   $v0, 11
    syscall

if0_false:
    add  $t0, $t0, 1     #   i++;
    b    loop1           # }
    
end1:

    li   $a0, '\n'
    li   $v0, 11
    syscall

    li   $t0, 0          # i = 0
loop2:
    bge  $t0, 10, end2   # while (i < 10) {

    mul  $t1, $t0, 4     #   calculate &numbers[i]
    la   $t2, numbers    #
    add  $t3, $t1, $t2   #
    lw   $t4, ($t3)      #   $t4 is now numbers[i]
    
if1:
    bge  $t4, 0, if1_false
    
    move $a0, $t4
    li $v0, 1
    syscall
    li   $a0, ' '
    li   $v0, 11
    syscall

if1_false:
    add  $t0, $t0, 1     #   i++;
    b    loop2           # }
    
end2:

    li   $a0, '\n'
    li   $v0, 11
    syscall

    li   $v0, 0
    jr   $31             # return

.data

numbers:
    .word 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  # int numbers[10] = {0};

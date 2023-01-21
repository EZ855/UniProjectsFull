# Sieve of Eratosthenes
# https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
main:

    # PUT YOUR CODE
        li      $t1, 0                                          # int col = 0; $t1
reveal_grid_body_loop1:
        bge     $t1, 1000, reveal_grid_body_end1              # while (col < N_COLS) {
        
        mul     $t2, $t1, 4
        la      $t3, prime
        add     $t4, $t3, $t2
        li      $t5, 1
        sb      $t5, ($t4)
        addi    $t1, $t1, 1                                     #   col++;
        j       reveal_grid_body_loop1                          # }
reveal_grid_body_end1:

        li      $t1, 2                                          # int col = 2; $t1
reveal_grid_body_loop2:
        bge     $t1, 1000, reveal_grid_body_end2              # while (col < N_COLS) {
        
        mul     $t2, $t1, 4
        la      $t3, prime
        add     $t4, $t3, $t2                                   # &prime[i] in $t4
        lb      $t5, ($t4)                                      # prime[i] in $t5
        
        beqz    $t5, if_false
if_true:
        move    $a0, $t1                                     # printf("%d", i);
        li      $v0, 1
        syscall
        li   $a0, '\n'                    # printf("%c", '\n');
        li   $v0, 11
        syscall
        
        mul     $t6, $t1, 2                                  # int j = 2 * i; in $t6
loop:
        bge     $t6, 1000, end                               # while (j < 1000) {
        
        mul     $t7, $t6, 4
        la      $t8, prime
        add     $t8, $t8, $t7                                   # &prime[j] in $t8
        li      $t7, 0
        sb      $t7, ($t8)                                      # prime[j] = 0
        
        
        add     $t6, $t6, $t1                                # j = j + i;
        b       loop
end:




if_false:
        
        addi    $t1, $t1, 1                                     #   col++;
        j       reveal_grid_body_loop2                          # }
reveal_grid_body_end2:

    li $v0, 0           # return 0
    jr $31

.data
prime:
    .space 1000

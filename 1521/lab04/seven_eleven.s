# Read a number and print positive multiples of 7 or 11 < n

# number stored in $t0
# i stored in $t1
# 7 stored in $t2
# 11 stored in $t3
# mod result stored in $t4

main:                  # int main(void) {

    la   $a0, prompt   # printf("Enter a number: ");
    li   $v0, 4
    syscall

    li   $v0, 5         # scanf("%d", number);
    syscall
    
    move $t0, $v0
    li   $t1, 1         # int i = 1;
    li   $t2, 7         # int j = 7;
    li   $t3, 11         # int k = 11; 
    
loop:
    bge  $t1, $t0, end  # while (i < number) {



    div  $t1, $t2
    mfhi $t4
    beq  $t4, 0, print_i   # if (i % j == 0 ||
    div  $t1, $t3
    mfhi $t4
    beq  $t4, 0, print_i
    b increment            # i % k == 0) {
    
print_i:
    move $a0, $t1         #   printf("%d", i);
    li   $v0, 1
    syscall

    li   $a0, '\n'      # printf("%c", '\n');
    li   $v0, 11
    syscall             # }
    
increment:
    addi $t1, 1
    b loop             # }
    
end:
    jr   $ra           # return

    .data
prompt:
    .asciiz "Enter a number: "

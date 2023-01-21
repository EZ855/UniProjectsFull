main: #int main(void) {
    
    li  $v0, 4
    la  $a0, string
    syscall
    
    li $v0, 0
    jr $ra
    
    .data
string:
.asciiz "I MIPS you!\n"

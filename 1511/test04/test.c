#include <stdio.h>

int main (void) {
int test = 3/2;
printf("%d", test);
return 0;
}
if (loop_counter0 == 0 || loop_counter0 == hourglass_height_and_width) {
                printf(" -");
            }
            else if (loop_counter1 > loop_counter0 - 1 && loop_counter1 < hourglass_height_and_width - loop_counter0 - 1) {
                if (loop_counter1 < (hourglass_height_and_width + 1)/2) {
                printf("%2d", loop_counter0);
                }
                
                else if (loop_counter1 == (hourglass_height_and_width + 1)/2) {
                int middle_of_hourglass = (hourglass_height_and_width + 1)/2;
                printf("%2d", middle_of_hourglass);
                }
                
                else if (loop_counter1 > (hourglass_height_and_width + 1)/2) {
                int last_half_hourglass = hourglass_height_and_width - loop_counter0 - 1;
                printf("%2d", last_half_hourglass);
                }
            }
            else {
                printf(" -");
            }

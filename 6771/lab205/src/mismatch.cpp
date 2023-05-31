#include "mismatch.h"
#include <iterator>

auto mismatch(std::vector<int> &v1, std::vector<int> &v2) -> std::pair<iter, iter> {
   
    iter ptr1 = v1.begin();
    iter ptr2 = v2.begin();

    while (ptr1 != v1.end() && ptr2 != v2.end()) {
        if (*ptr1 != *ptr2) {
            return std::pair(ptr1, ptr2);
        }
        std::advance(ptr1,1);
        std::advance(ptr2,1);
    }
    return std::pair(ptr1, ptr2);
}
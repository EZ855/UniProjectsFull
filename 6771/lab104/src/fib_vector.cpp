#include "fib_vector.h"

auto fibonacci(int n) -> std::vector<int> {
    auto nums = std::vector<int>{};

    if (n < 1) {
        return nums;
    }
    else {
        while (n > 0) {
            if (nums.empty()) {
                nums.push_back(1);
            }
            else if (nums.size() == 1) {
                nums.push_back(2);
            }
            else {
                nums.push_back(nums.end()[-2] + nums.end()[-1]);
            }
            n--;
        }
    }

    return nums;
}


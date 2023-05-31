#include "setdiff.h"

#include <algorithm>
#include <vector>

auto set_difference(std::vector<char> &vec_set, const std::vector<char> &blacklist) -> void {
    for (const char c : blacklist) {
        vec_set.erase(std::remove(vec_set.begin(), vec_set.end(), c), vec_set.end());
    }
}

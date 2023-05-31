#include "sort_descending.h"
#include <functional>

auto sort_descending(std::vector<int>& numbers) -> void {
    sort(numbers.begin(), numbers.end(), std::greater<int>());
}

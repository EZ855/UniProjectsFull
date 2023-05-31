#include "scale.h"

auto scale(std::vector<int> &ivec, double factor) -> std::vector<double> {

    std::vector<double> res;
    for (int i : ivec) {
        double casted = static_cast<double>(i);
        res.push_back(casted * factor);
    }
    return res;
}



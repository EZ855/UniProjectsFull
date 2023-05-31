#include "permutation.h"

auto is_permutation(const std::string &x, const std::string &y) -> bool {

    if (x.empty() && y.empty()) {
        return true;
    }
    if (x.empty() || y.empty()) {
        return false;
    }
    if (x.size() != y.size()) {
        return false;
    }
    for (unsigned long i = 0; i < x.size(); i++) {
        if (y.find(x[i]) == std::string::npos) {
            return false;
        }
    }

    return true;
    
}

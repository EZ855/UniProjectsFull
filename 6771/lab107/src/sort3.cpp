#include "sort3.h"
#include <vector>

auto sort3(int &a, int &b, int &c) -> void {

    std::vector<int> v{a,b,c};
    std::sort(v.begin(), v.end());
    a = v.at(0);
    b = v.at(1);
    c = v.at(2);
}

auto sort3(std::string &a, std::string &b, std::string &c) -> void {

    std::vector<std::string> v{a,b,c};
    std::sort(v.begin(), v.end());
    a = v.at(0);
    b = v.at(1);
    c = v.at(2);
}
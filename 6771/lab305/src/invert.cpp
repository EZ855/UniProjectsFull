#include "invert.h"
#include <iterator>
#include <iostream>

auto invert(const std::map<std::string, int> &mp) -> std::map<int, std::string> {
    std::map<int, std::string> res;
    for (auto it = mp.begin(); it != mp.end(); ++it) {
        auto key = res.find(it->second);
        if (key != res.end()) {
            // If equal lengths, the first one will not be replaced
            if (key->second.size() < it->first.size()) {
                key->second = it->first;
            }
        }
        else {
            res.emplace(it->second, it->first);
        }
    }
    
    auto iter = std::ostream_iterator<int>(std::cout, " ");



    return res;
}
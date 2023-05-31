#ifndef COMP6771_BASE_STRINGS_H
#define COMP6771_BASE_STRINGS_H

#include <compare>
#include <string>

struct base2str;
struct base16str;

struct base2str {
    std::string bits;
};

struct base16str {
    std::string hexits;
};

#endif // COMP6771_BASE_STRINGS_H

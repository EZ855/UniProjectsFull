#include "setdiff.h"

#include <catch2/catch.hpp>

TEST_CASE("set_difference() average case") {
    std::vector<char> chars = {'a', 'b', 'c'};
    std::vector<char> blacklist = {'a', 'b'};
    std::vector<char> res = {'c'};

    std:: vector<char> *pointChars = &chars;
    std:: vector<char> *pointblacklist = &blacklist;

    set_difference(*pointChars, *pointblacklist);
    
    REQUIRE(chars == res);
}

TEST_CASE("set_difference() edge case") {
    std::vector<char> chars = {};
    std::vector<char> blacklist ={};
    std::vector<char> res = {};

    std:: vector<char> *pointChars = &chars;
    std:: vector<char> *pointblacklist = &blacklist;

    set_difference(*pointChars, *pointblacklist);
    
    REQUIRE(chars == res);
}

/* TEST_CASE("set_difference() invalid case") {
    std::vector<int> chars = {};
    std::vector<char> blacklist ={};
    std::vector<char> res = {};

    std:: vector<int> *pointChars = &chars;
    std:: vector<char> *pointblacklist = &blacklist;

    set_difference(*pointChars, *pointblacklist);
} */
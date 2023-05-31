#include "invert.h"

#include <catch2/catch.hpp>

TEST_CASE("sanity check/average case") {
    auto m = std::map<std::string, int> {
    {"a", 6771},
    {"ab", 6771},
    {"abc", 6771},
    {"xyz", 6772},
    };
    auto n = std::map<int, std::string> {
    {6771, "abc"},
    {6772, "xyz"},
    };  

    auto res = invert(m);
    REQUIRE(res == n);
}
TEST_CASE("empty map case") {
    auto m = std::map<std::string, int> {};
    auto n = std::map<int, std::string> {};
    auto res = invert(m);
    REQUIRE(res == n);
}
TEST_CASE("equal string length not replaced") {
    auto m = std::map<std::string, int> {
    {"a", 6771},
    {"ab", 6771},
    {"abc", 6771},
    {"abd", 6771},
    {"xyz", 6772},
    };
    auto n = std::map<int, std::string> {
    {6771, "abc"},
    {6772, "xyz"},
    };  

    auto res = invert(m);
    REQUIRE(res == n);
}
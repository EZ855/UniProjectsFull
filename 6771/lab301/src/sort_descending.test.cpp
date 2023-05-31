#include "./sort_descending.h"
#include <catch2/catch.hpp>


TEST_CASE("average case/sanity check") {
    std::vector<int> v = {1, 2, 3};
    std::vector<int> exp = {3, 2, 1};
    sort_descending(v);
    CHECK(v == exp);
}
TEST_CASE("edge case empty") {
    std::vector<int> v = {};
    std::vector<int> exp = {};
    sort_descending(v);
    CHECK(v == exp);
}
TEST_CASE("edge case") {
    std::vector<int> v = {-1, 20, 918249812};
    std::vector<int> exp = {918249812, 20, -1};
    sort_descending(v);
    CHECK(v == exp);
}
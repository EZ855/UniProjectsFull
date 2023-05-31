#include "stats.h"

#include <catch2/catch.hpp>

TEST_CASE("test read marks") {
    std::vector exp = {2, 14, 90, 56, 88, 44, 76, 33, 100, 4, 47, 67, 71, 8};
    std::vector<int> res = read_marks("marks.txt");
    CHECK(exp == res);
}

TEST_CASE("test calculate stats") {
    std::vector marks = {2, 14, 90, 56, 88, 44, 76, 33, 100, 4, 47, 67, 71, 8};

    stats res = calculate_stats(marks);
    CHECK(res.avg == 50);
    CHECK(res.median == 51);
    CHECK(res.highest == 100);
    CHECK(res.lowest == 2);
}
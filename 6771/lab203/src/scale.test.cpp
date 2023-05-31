#include "scale.h"
#include <catch2/catch.hpp>

TEST_CASE("scale average case") {
    std::vector<int> ivec {1,2,3,4};
    std::vector<double> expec {0.5,1.0,1.5,2.0};
    double factor = 0.5;
    auto res = scale(ivec, factor);
    CHECK(res == expec);
}

TEST_CASE("scale default factor test") {
    std::vector<int> ivec {1,2,3,4};
    std::vector<double> expec {0.5,1.0,1.5,2.0};
    auto res = scale(ivec);
    CHECK(res == expec);
}

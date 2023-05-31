#include "permutation.h"
#include <catch2/catch.hpp>

TEST_CASE("empty strings") {
    const std::string x = "", y = "";

    REQUIRE(is_permutation(x, y) == true);

}

TEST_CASE("matching strings") {
    const std::string x = "hi, my name is", y = "hi, my name is";

    REQUIRE(is_permutation(x, y) == true);

}

TEST_CASE("one empty string") {
    const std::string x = "", y = "hi, my name is";

    REQUIRE(is_permutation(x, y) == false);

}

TEST_CASE("matching strings jumbled") {
    const std::string x = "hi, my name is", y = "ame ishi, my n";

    REQUIRE(is_permutation(x, y) == true);

}
TEST_CASE("non matching strings") {
    const std::string x = "hi, my name is", y = "ame ishi, m n";

    REQUIRE(is_permutation(x, y) == false);

}
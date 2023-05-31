#include "fib_vector.h"

#include <catch2/catch.hpp>

TEST_CASE("Works when n == 0") {
    const auto expected = std::vector<int>{};
    auto nums = fibonacci(0);

    CHECK(nums == expected);
}

TEST_CASE("Works when n == 1") {
    auto nums = fibonacci(1);

    CHECK(!nums.empty());
    CHECK(nums.size() == 1);

    CHECK(nums[0] == 1);
    CHECK(nums.at(0) == 1);
}

TEST_CASE("Works when n == 2") {
    auto nums = fibonacci(2);

    CHECK(!nums.empty());
    CHECK(nums.size() == 2);

    CHECK(nums[0] == 1);
    CHECK(nums.at(0) == 1);

    CHECK(nums[1] == 2);
    CHECK(nums.at(1) == 2);
}

TEST_CASE("Works when n == 30") {
    auto nums = fibonacci(30);

    CHECK(!nums.empty());
    CHECK(nums.size() == 30);
    // This is the 31st number if we start from 0 - we don't, instead starting at the second 1 (0, 1, 1, 2...)
    CHECK(nums[29] == 1346269);
}
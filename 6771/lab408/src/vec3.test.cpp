#include "./vec3.h"

#include <catch2/catch.hpp>

TEST_CASE("test default constructor") {
    vec3 v = vec3{};
    CHECK(v.x == 0);
    CHECK(v.y == 0);
    CHECK(v.z == 0);
    CHECK(v.r == 0);
    CHECK(v.g == 0);
    CHECK(v.b == 0);
    CHECK(v.s == 0);
    CHECK(v.t == 0);
    CHECK(v.p == 0);
    CHECK(sizeof(v) == 3 * sizeof(double));
}
TEST_CASE("test single arg constructor") {
    vec3 v = vec3{1};
    CHECK(v.x == 1);
    CHECK(v.y == 1);
    CHECK(v.z == 1);
    CHECK(v.r == 1);
    CHECK(v.g == 1);
    CHECK(v.b == 1);
    CHECK(v.s == 1);
    CHECK(v.t == 1);
    CHECK(v.p == 1);
    CHECK(sizeof(v) == 3 * sizeof(double));
}
TEST_CASE("test three arg constructor") {
    vec3 v = vec3{1, 2, 3};
    CHECK(v.x == 1);
    CHECK(v.y == 2);
    CHECK(v.z == 3);
    CHECK(v.r == 1);
    CHECK(v.g == 2);
    CHECK(v.b == 3);
    CHECK(v.s == 1);
    CHECK(v.t == 2);
    CHECK(v.p == 3);
    CHECK(sizeof(v) == 3 * sizeof(double));
}
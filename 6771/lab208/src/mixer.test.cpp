#include "mixer.h"

#include <catch2/catch.hpp>
#include <vector>


TEST_CASE("wacky colour gives colour") {
    paint p1 = RED;
    paint p2 = GREEN;
    REQUIRE(wacky_colour(p1, p2).value() == YELLOW);
}
TEST_CASE("wacky colour gives null option") {
    paint p1 = RED;
    paint p2 = MAGENTA;
    REQUIRE(wacky_colour(p1, p2) == std::optional<paint> {});
}
TEST_CASE("wacky colour gives null option through initial default") {
    paint p1 = MAGENTA;
    paint p2 = MAGENTA;
    REQUIRE(wacky_colour(p1, p2) == std::optional<paint> {});
}
TEST_CASE("mix with two colours") {
    std::vector<paint> paints = {RED, GREEN};
    REQUIRE(mix(paints) == std::optional<paint> {YELLOW});
}
TEST_CASE("mix with three colours") {
    std::vector<paint> paints = {RED, GREEN, MAGENTA};
    REQUIRE(mix(paints) == std::optional<paint> {BROWN});
}
TEST_CASE("mix with three colours, null colour after first two mixed") {
    std::vector<paint> paints = {BROWN, GREEN, MAGENTA};
    REQUIRE(mix(paints) == std::optional<paint> {});
}
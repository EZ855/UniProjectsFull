#include "./ferrari.h"

#include <catch2/catch.hpp>

TEST_CASE("ferrari no argument ctor test") {
    ferrari f = ferrari{};
    CHECK(f.speed_ == 0);
    CHECK(f.modelno_ == 6771);
    CHECK(f.owner_ == "unknown");
}
TEST_CASE("ferrari two argument ctor test") {
    ferrari f = ferrari{"Top-g", 1234};
    CHECK(f.speed_ == 0);
    CHECK(f.modelno_ == 1234);
    CHECK(f.owner_ == "Top-g");
}
TEST_CASE("ferrari pair method test") {
    ferrari f = ferrari{"Top-g", 1234};
    std::pair<std::string, int> exp = {"Top-g", 1234};
    CHECK(f.get_details() == exp);
}
TEST_CASE("ferrari drive test") {
    ferrari f = ferrari{"Top-g", 1234};
    f.drive(200);
    CHECK(f.speed_ == 200);
}
TEST_CASE("ferrari VROOM test") {
    ferrari f = ferrari{"Top-g", 1234};
    f.drive(200);
    CHECK(f.vroom() == "VROOOOOOOOM!!!");
}
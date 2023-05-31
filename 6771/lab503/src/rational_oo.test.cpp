#include "rational_oo.h"
#include "rational_oo.cpp"

#include <catch2/catch.hpp>
#include <cstddef>

TEST_CASE("test constructor") {
    std::optional<rational_number> test = rational_number::make_rational(1, 2);
    REQUIRE(test.has_value());
}
TEST_CASE("test constructor invalid") {
    std::optional<rational_number> test = rational_number::make_rational(1, 0);
    REQUIRE(!test.has_value());
}
TEST_CASE("test eq") {
    std::optional<rational_number> test1 = rational_number::make_rational(1, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    CHECK(eq(test1.value(), test2.value()));
}
TEST_CASE("test eq simplify") {
    std::optional<rational_number> test1 = rational_number::make_rational(1, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(2, 4);
    CHECK(eq(test1.value(), test2.value()));
}
TEST_CASE("test eq simplify both") {
    std::optional<rational_number> test1 = rational_number::make_rational(6, 12);
    std::optional<rational_number> test2 = rational_number::make_rational(2, 4);
    CHECK(eq(test1.value(), test2.value()));
}
TEST_CASE("test eq not equal") {
    std::optional<rational_number> test1 = rational_number::make_rational(7, 12);
    std::optional<rational_number> test2 = rational_number::make_rational(2, 4);
    CHECK(!eq(test1.value(), test2.value()));
}
TEST_CASE("test ne") {
    std::optional<rational_number> test1 = rational_number::make_rational(1, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    CHECK(!ne(test1.value(), test2.value()));
}
TEST_CASE("test mult") {
    std::optional<rational_number> test1 = rational_number::make_rational(1, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    std::optional<rational_number> exp = rational_number::make_rational(1, 4);
    std::optional<rational_number> res = mul(test1.value(), test2.value());
    REQUIRE(res.has_value());
    CHECK(eq(exp.value(), res.value()));
}
TEST_CASE("test div") {
    std::optional<rational_number> test1 = rational_number::make_rational(1, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    std::optional<rational_number> exp = rational_number::make_rational(1, 1);
    std::optional<rational_number> res = div(test1.value(), test2.value());
    REQUIRE(res.has_value());
    CHECK(eq(exp.value(), res.value()));
}
TEST_CASE("test add") {
    std::optional<rational_number> test1 = rational_number::make_rational(1, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    std::optional<rational_number> exp = rational_number::make_rational(1, 1);
    std::optional<rational_number> res = add(test1.value(), test2.value());
    REQUIRE(res.has_value());
    CHECK(eq(exp.value(), res.value()));
}
TEST_CASE("test sub") {
    std::optional<rational_number> test1 = rational_number::make_rational(3, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    std::optional<rational_number> exp = rational_number::make_rational(1, 1);
    std::optional<rational_number> res = sub(test1.value(), test2.value());
    REQUIRE(res.has_value());
    CHECK(eq(exp.value(), res.value()));
}
TEST_CASE("test subscript get") {
    std::optional<rational_number> test1 = rational_number::make_rational(3, 2);
    REQUIRE(test1.has_value());
    CHECK(test1.value()['^'] == 3);
}
TEST_CASE("test subscript set") {
    std::optional<rational_number> test1 = rational_number::make_rational(3, 2);
    REQUIRE(test1.has_value());
    test1.value()['^'] = 20;
    CHECK(test1.value()['^'] == 20);
}
TEST_CASE("test subscript get with 'v'") {
    std::optional<rational_number> test1 = rational_number::make_rational(3, 2);
    REQUIRE(test1.has_value());
    CHECK(test1.value()['v'] == 2);
}
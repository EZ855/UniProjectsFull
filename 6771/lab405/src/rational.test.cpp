#include "rational.h"
#include "rational.cpp"

#include <catch2/catch.hpp>
#include <cstddef>

TEST_CASE("test constructor") {
    std::optional<rational_number> test = rational_number::make_rational(1, 2);
    REQUIRE(test.has_value());
    CHECK(test.value().num_ == 1);
    CHECK(test.value().denom_ == 2);
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
    rational_number res = mult(test1.value(), test2.value());
    CHECK(eq(exp.value(), res));
}
TEST_CASE("test div") {
    std::optional<rational_number> test1 = rational_number::make_rational(1, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    std::optional<rational_number> exp = rational_number::make_rational(1, 1);
    rational_number res = div(test1.value(), test2.value());
    CHECK(eq(exp.value(), res));
}
TEST_CASE("test add") {
    std::optional<rational_number> test1 = rational_number::make_rational(1, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    std::optional<rational_number> exp = rational_number::make_rational(1, 1);
    rational_number res = add(test1.value(), test2.value());
    CHECK(eq(exp.value(), res));
}
TEST_CASE("test sub") {
    std::optional<rational_number> test1 = rational_number::make_rational(3, 2);
    std::optional<rational_number> test2 = rational_number::make_rational(1, 2);
    std::optional<rational_number> exp = rational_number::make_rational(1, 1);
    rational_number res = sub(test1.value(), test2.value());
    CHECK(eq(exp.value(), res));
}
#include "./qa.h"

#include <catch2/catch.hpp>

#include <iostream>
#include <limits>

struct double_traits {
    static auto ctor(double a, double b) -> double {
        return a * b;
    }

    static auto null() noexcept -> double {
        // I wonder what could lie beyond... infinity
        return std::numeric_limits<double>::infinity();
        // I'm lost in the pursuit of my... finality
    }

    static auto dtor(double &) noexcept -> void {
        // do nothing -- doubles are trivial
    }
};

auto maybe_get_double() -> maybe<double, double_traits> {
  return maybe<double, double_traits>{1.0, 3.5};
}

TEST_CASE("Sanity check") {
  auto maybe_double = maybe_get_double();

  CHECK(*maybe_double == Approx(3.5));
}
TEST_CASE("test default constructor and bool conversion") {
  auto maybe_double = maybe<double, double_traits>{};

  CHECK(maybe_double == false);
}
TEST_CASE("test single arg constructor and bool conversion") {
  const auto dub = double_traits::ctor(1.0, 1.0);
  auto maybe_double = maybe<double, double_traits>{dub};

  CHECK(maybe_double == true);
}
TEST_CASE("test mult arg constructor and bool conversion") {
  auto maybe_double = maybe<double, double_traits>{1.0, 1.0, 1.0};

  CHECK(maybe_double == true);
}
TEST_CASE("test move constructor") {
  const auto dub = double_traits::ctor(1.0, 1.0);
  auto maybe_double = maybe<double, double_traits>{dub};
  auto test = std::move(maybe_double);
  CHECK(test == true);
  CHECK(maybe_double == false);
}
TEST_CASE("test size") {
  const auto dub = double_traits::ctor(1.0, 1.0);
  auto maybe_double = maybe<double, double_traits>{dub};
  CHECK(sizeof(maybe_double) == sizeof(double));
}

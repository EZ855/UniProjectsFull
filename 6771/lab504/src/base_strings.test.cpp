#include "./base_strings.h"
#include "./base_strings.cpp"

#include <catch2/catch.hpp>

TEST_CASE("test bstr <=> hstr") {
    auto bstr = base2str{{"0b1101001110011"}};
    auto hstr = base16str{{"0xdeadbeef"}};

    CHECK(bstr <=> hstr == std::weak_ordering::less);
}

TEST_CASE("test hstr <=> bstr") {
    auto bstr = base2str{{"0b1101001110011"}};
    auto hstr = base16str{{"0xdeadbeef"}};

    CHECK(hstr <=> bstr == std::weak_ordering::greater);
}

TEST_CASE("test bstr <=> bstr") {
    auto bstr = base2str{{"0b1101001110011"}};

    CHECK(bstr <=> bstr == std::strong_ordering::equal);
}

TEST_CASE("test hstr <=> hstr") {
    auto hstr = base16str{{"0xdeadbeef"}};

    CHECK(hstr <=> hstr == std::strong_ordering::equal);
}
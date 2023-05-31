#include "./book.h"

#include <catch2/catch.hpp>
#include <iostream>
#include <ostream>

TEST_CASE("test book construct") {
    book my_book = {"Tour of C++11", "Bjarne Stroustrup", "0123456789X", 9000};
}
TEST_CASE("test book to string") {
    book my_book = {"Tour of C++11", "Bjarne Stroustrup", "0123456789X", 9000};
    std::string s = static_cast<std::string>(my_book);
    CHECK(s == "Bjarne Stroustrup, Tour of C++11");
}
TEST_CASE("test book == book") {
    book my_book = {"Tour of C++11", "Bjarne Stroustrup", "0123456789X", 9000};
    book my_book2 = {"Tour of asdf", "asdf", "0123456789X", 123};
    CHECK(my_book2 == my_book);
}
TEST_CASE("test book < book") {
    book my_book = {"Tour of C++11", "Bjarne Stroustrup", "1123456789X", 9000};
    book my_book2 = {"Tour of asdf", "asdf", "0123456789X", 123};
    CHECK(my_book2 < my_book);
}
TEST_CASE("test book print") {
    book my_book = {"Tour of C++11", "Bjarne Stroustrup", "1123456789X", 9000.015};
    std::cout << my_book << std::endl;
}
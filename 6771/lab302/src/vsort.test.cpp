#include "vsort.h"

#include <catch2/catch.hpp>
#include <vector>

TEST_CASE("test vowels") {
	std::vector<std::string> test = {"aaa", "bbb"};
	std::vector<std::string> exp = {"bbb", "aaa"};
	vsort(test);
	CHECK(test == exp);
}
TEST_CASE("test lexicographical") {
	std::vector<std::string> test = {"aaaf", "aaad"};
	std::vector<std::string> exp = {"aaad", "aaaf"};
	vsort(test);
	CHECK(test == exp);
}
TEST_CASE("test empty") {
	std::vector<std::string> test = {};
	std::vector<std::string> exp = {};
	vsort(test);
	CHECK(test == exp);
}
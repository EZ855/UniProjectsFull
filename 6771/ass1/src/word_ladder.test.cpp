#include "word_ladder.h"

#include <catch2/catch.hpp>

TEST_CASE("word_ladder::read_lexicon works as expected") {
	auto const english_lexicon = ::word_ladder::read_lexicon("../src/test.txt");
	auto const lexicon = std::unordered_set<std::string>{
		"at",
		"it"
	};
	CHECK(lexicon == english_lexicon);
}

TEST_CASE("at -> it") {
	auto const lexicon = std::unordered_set<std::string>{
		"at",
		"it"
	};

	const auto expected = std::vector<std::vector<std::string>>{
		{"at", "it"}
	};

	auto const ladders = word_ladder::generate("at", "it", lexicon);

	CHECK(ladders == expected);
}
TEST_CASE("awake -> sleep") {
	auto const english_lexicon = ::word_ladder::read_lexicon("./english.txt");

	const auto expected = std::vector<std::vector<std::string>> {
		{"awake","aware","sware","share","sharn","shawn","shewn","sheen","sheep","sleep"},
		{"awake","aware","sware","share","shire","shirr","shier","sheer","sheep","sleep"}
	};

	auto const ladders = ::word_ladder::generate("awake", "sleep", english_lexicon);

	CHECK(ladders == expected);
}
TEST_CASE("airplane -> tricycle") {
	auto const english_lexicon = ::word_ladder::read_lexicon("./english.txt");

	const auto expected = std::vector<std::vector<std::string>> {};

	auto const ladders = ::word_ladder::generate("airplane", "tricycle", english_lexicon);

	CHECK(ladders == expected);
}

// TEST_CASE("atlases -> cabaret") {
// 	auto const english_lexicon = ::word_ladder::read_lexicon("./english.txt");
// 	auto const ladders = ::word_ladder::generate("atlases", "cabaret", english_lexicon);

// 	CHECK(std::size(ladders) != 0);
// }

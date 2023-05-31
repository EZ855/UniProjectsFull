#include "./q2.h"

#include <catch2/catch.hpp>

#include <sstream>

template<typename I>
auto make_sparse_matrix(std::size_t m,
                        std::size_t n,
                        std::initializer_list<std::tuple<std::size_t, std::size_t, I>> init
) {
	auto sm = sparse_matrix<I>(m, n);
    for (const auto &[i, j, v] : init) {
        sm.element(i, j, v);
    }

	return sm;
}

/* A small collection of tests to help with sanity checking */

TEST_CASE("Check sparse_matrx(std::size_t dim)") {
	CHECK(sparse_matrix<long long>(4) == make_sparse_matrix<long long>(4, 4, {}));
}

TEST_CASE("Can input a sparse_matrix according to the correct format") {
	auto is = std::istringstream{"(2, 2, 2)\n"
	                             "(0, 0, 1)\n"
	                             "(1, 1, 1"};
	const auto expected = sparse_matrix<long long>::identity(2);
	auto result = sparse_matrix<long long>();

	is >> result;

	CHECK(result == expected);
}

TEST_CASE("Able to transpose non-square matrix") {
	auto sm = make_sparse_matrix<long long>(2, 3, {{0, 0, 2}, {1, 0, 2}});
	auto expected = make_sparse_matrix<long long>(3, 2, {{0, 0, 2}, {0, 1, 2}});

	auto result = transpose(sm);

	CHECK(result == expected);
}

#include "./register.h"

#include <catch2/catch.hpp>
#include <concepts>
#include <tuple>

TEST_CASE("size2reg I = 1") {
    CHECK(std::same_as<size2reg_t<1>, std::uint8_t>);
}
TEST_CASE("size2reg I = 2") {
    CHECK(std::same_as<size2reg_t<2>, std::uint16_t>);
}
TEST_CASE("size2reg I = 3") {
    CHECK(std::same_as<size2reg_t<3>, std::uint32_t>);
}
TEST_CASE("size2reg I = 5") {
    CHECK(std::same_as<size2reg_t<5>, std::uint64_t>);
}
TEST_CASE("size2reg I = 9") {
    CHECK(std::same_as<size2reg_t<9>, void>);
}
TEST_CASE("is_passable_in_register T = int (fundamental type)") {
    CHECK(is_passable_in_register_t<int>);
}
TEST_CASE("is_passable_in_register T = trivial class") {
    struct A
    {
        int m;
    };
    CHECK(is_passable_in_register_t<A>);
}
TEST_CASE("is_passable_in_register T = non-trivial class/non-fundamental type") {
    struct B
    {
        B() {}
    };
    CHECK(!is_passable_in_register_t<B>);
}
TEST_CASE("slot_type/slot_type_t working") {
    CHECK(std::same_as<ppl::slot_type_t<int>, int>);
}
TEST_CASE("num_slots/num_slots_v working") {
    CHECK(ppl::num_slots_v<std::tuple<int, bool, int>> == 3);
}
TEST_CASE("num_slots/num_slots_v working empty tuple") {
    CHECK(ppl::num_slots_v<std::tuple<>> == 0);
}
TEST_CASE("storing tuple") {
    CHECK(std::same_as<ppl::store_tuple_t<std::tuple<int, bool, int>>, std::tuple<int, bool, int>>);
}
TEST_CASE("storing tuple type working") {
    ppl::store_tuple<std::tuple<int, bool, int>> tuple_stored;
    CHECK(std::same_as<decltype(tuple_stored)::type, std::tuple<int, bool, int>>);
}
TEST_CASE("storing tuple value working") {
    ppl::store_tuple<std::tuple<int, bool, int>> tuple_stored;
    CHECK(decltype(tuple_stored)::value == 3);
}
TEST_CASE("storing tuple value working with empty tuple") {
    ppl::store_tuple<std::tuple<>> tuple_stored;
    CHECK(decltype(tuple_stored)::value == 0);
}
TEST_CASE("print list") {
    ppl::print_list<0, std::tuple<int, bool, int>>();
}

TEST_CASE("create node") {
    ppl::testing<int> t = {1};
    std::cout << t.val;
    auto t2 = ppl::create_node<ppl::derived>();
    std::cout << t2->val;
}
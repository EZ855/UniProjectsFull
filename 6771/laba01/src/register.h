#ifndef COMP6771_REGISTER_H
#define COMP6771_REGISTER_H

#include <type_traits>
#include <concepts>
#include <cstdint>
#include <iostream>
#include <tuple>
#include <utility>

template <auto I>
struct size2reg {
    using type = std::conditional_t<(I == 1), std::uint8_t,
        std::conditional_t<(I == 2), std::uint16_t,
            std::conditional_t<(2 < I && I <= 4), std::uint32_t,
                std::conditional_t<(4 < I && I <= 8), std::uint64_t, void>
            >
        >
    >;
};
template <std::integral auto I>
using size2reg_t = typename size2reg<I>::type;


template <typename T>
struct is_passable_in_register {
    static constexpr bool value = std::is_fundamental_v<T> || std::is_trivial_v<T>;
};

template <typename T>
static constexpr bool is_passable_in_register_t = is_passable_in_register<T>::value;

namespace ppl {
    using node_id = int;
    template <typename N>
    concept concrete_node = requires {
        // typename N::input_type;
        // std::same_as<typename N::input_type, typename std::tuple<>>;
        // typename N::output_type;
        std::is_class_v<N>;
        !std::is_abstract_v<N>;
    };
    template <typename N>
    struct num_slots {
        static constexpr int value = std::tuple_size_v<N>;
    };
    template <typename N>
    static constexpr int num_slots_v = num_slots<N>::value;

    template <typename T>
    struct slot_type {
        using type = T;
    };
    template <typename T>
    struct test : slot_type<T> {
    };
    template <typename T>
    using slot_type_t = typename slot_type<T>::type;

    template <typename tup>
    struct store_tuple {
        using type = tup;
        static constexpr int value = num_slots_v<tup>;
    };
    template <typename tup>
    using store_tuple_t = typename store_tuple<tup>::type;
    template <typename T> void printHelper() { std::cout << "unknown type\n"; }
    template <> void printHelper<int>() { std::cout << "int\n"; }
    template <> void printHelper<bool>() { std::cout << "bool\n"; }
    template <> void printHelper<char>() { std::cout << "char\n"; }
    template <> void printHelper<std::string>() { std::cout << "std::string\n"; }
    // recursive case for instantiation
    template <std::size_t index, typename T>
    void print_list() {
        if constexpr (index == std::tuple_size_v<T>) {
            return;
        } else {
            printHelper<std::tuple_element_t<index, T>>();
            return print_list<index + 1, T>();
        }
    }

    // template <typename N>
    // struct slot_array {
    //     static constexpr int num_slots = num_slots_v<N>;
    //     static constexpr slot_type_t value[num_slots] = get_types<;
    // };
    template <typename T>
    struct testing {
        T val;
    };
    struct derived : testing<int> {
        int val = 20;
    };
    template <typename N, typename... Args>
    requires concrete_node<N> and std::constructible_from<N, Args...>
    auto create_node(Args&& ...args) {
        N n = {args...};
        N* ptr = &n;
        return ptr;
    }
}

#endif // COMP6771_REGISTER_H

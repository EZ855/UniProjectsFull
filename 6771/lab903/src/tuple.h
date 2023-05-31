#ifndef COMP6771_TUPLE_H
#define COMP6771_TUPLE_H

#include <cstddef>

template <typename T, typename ...Ts>
struct tuple {
    T elem;
    tuple<Ts...> cons;
};

template <typename T>
struct tuple<T> {
    T elem;
};

template <typename T, typename ...Ts>
tuple(T, Ts...) -> tuple<T, Ts...>;

template <std::size_t I, typename T, typename ...Ts>
struct get_impl {
    auto operator()(const tuple<T, Ts...> &tp) const {
        return get_impl<I - 1, Ts...>{}(tp.cons);
    }
};

template <typename T, typename ...Ts>
struct get_impl<0, T, Ts...> {
    auto operator()(const tuple<T, Ts...> &tp) const {
        return tp.elem;
    }
};

template <typename T>
struct get_impl<0, T> {
    auto operator()(const tuple<T> &tp) const {
        return tp.elem;
    }
};

template <std::size_t I, typename T>
struct get_impl<I, T> {
    static_assert(I != 0, "Invalid size given to get at compile-time");
};


template <std::size_t I, typename T, typename ...Ts>
auto get(const tuple<T, Ts...> &tp) {
    return get_impl<I, T, Ts...>{}(tp);
}

#endif // COMP6771_TUPLE_H

#ifndef COMP6771_EXAM_QA
#define COMP6771_EXAM_QA

#include <type_traits>
#include <concepts>
#include <stdexcept>
#include <utility>

template <typename T>
struct remove_array_ref {
  using type = std::remove_reference_t<std::remove_all_extents_t<T>>;
};
template <typename T>
using remove_array_ref_t = typename remove_array_ref<T>::type;

// // Helper to get first elem in parameter pack
// template<typename T, typename ...Rest>
// auto get_elem(T elem) -> T {
//     return elem;
// }

template <typename T, typename N>
class maybe {

    public:
    using value_type = remove_array_ref_t<T>;
    using reference = value_type&;
    using pointer = std::conditional_t<std::is_class_v<value_type>, value_type*, void>;

    maybe() noexcept : val_{N::null()} {}
    explicit maybe(const value_type &arg) noexcept {
        val_ = arg;
    }
    template<typename ...Args>
    explicit maybe(Args&& ...args) {
        if constexpr (sizeof...(args) == 0) {
            maybe();
        }
        else if constexpr (sizeof...(args) == 1) {
            // auto arg = get_elem<Args...>(args...);
            // if (std::is_convertible_v<decltype(arg), const value_type &>) {
            //     maybe(static_cast<value_type&>(arg));
            // }
        }
        else {
            throw std::runtime_error{"cannot complete construction"};
            // if (std::is_invocable_v<N::ctor, Args...>) {
            //     val_ = N::ctor(std::forward<Args>(args)...);
            // } else {
            //     throw std::runtime_error{"cannot complete construction"};
            // }
        
        }
    }
    maybe(const maybe &other) = delete;
    maybe(maybe &&other) noexcept {
        val_ = std::exchange(other.val_, N::null());
    }

    ~maybe() noexcept {
        if (bool(*this)) {
            N::dtor(val_);
        }
    }

    auto operator=(const maybe &other) -> maybe& = delete;
    auto operator=(maybe &&other) noexcept -> maybe& {
        if (this == &other) {
            return *this;
        }
        this->val_ = other.val_;
        N::dtor(other.val_);
        return *this;
    }

    operator bool() const noexcept {
        return val_ != N::null();
    }

    auto operator*() -> reference {
        if (val_ != N::null()) {
            return val_;
        } else {
            throw std::runtime_error{"operator*::bad access"};
        }
    }
    auto operator->() -> pointer {
        if (val_ != N::null() && !std::is_void_v<pointer>) {
            return *val_;
        } else {
            throw std::runtime_error{"operator->::bad access"};
        }
    }

    auto operator==(const maybe &other) const noexcept -> bool {
        if (!bool(other) && !bool(*this)) {
            return true;
        }
        else if (other.val_ == val_) {
            return true;
        }
        else {
            return false;
        }
    }

    private:
    value_type val_;
};

// template <typename T, typename N>
// std::is_assignable_v<decltype(N::ctor()), T>
// class maybe {};
#endif // COMP6771_EXAM_QA

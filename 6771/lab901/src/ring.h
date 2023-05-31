#ifndef COMP6771_RING_H
#define COMP6771_RING_H

#include <initializer_list>
#include <cmath>
#include <cstddef>
#include <stdexcept>
template <typename T, std::size_t N>
class ring {
    public:
    ring(std::initializer_list<T> il) {
        if (il.size() > N) {
            throw std::invalid_argument{"Not enough capacity"};
        }
        head_ = 0;
        tail_ = 0;
        size_ = il.size();
        auto it = il.begin();
        for (std::size_t i = 0; i < N; ++i) {
            elems_[i] = *it;
            ++it;
            ++tail_;
        }
        if (tail_ == N) {
            tail_ = 0;
        }
    }

    template <typename InputIt>
    ring(InputIt first, InputIt last) {
        if (std::distance(first, last) > static_cast<long int>(N)) {
            throw std::invalid_argument{"Not enough capacity"};
        }
        head_ = 0;
        tail_ = 0;
        size_ = static_cast<long unsigned int>(std::distance(first, last));
        for (std::size_t i = 0; first != last; ++i) {
            elems_[i] = *first;
            ++first;
            ++tail_;
        }
        if (tail_ == N) {
            tail_ = 0;
        }
    }

    auto push(const T &t) -> void {
        if (size_ == N) {
            return;
        }
        elems_[tail_] = t;
        ++tail_;
        ++size_;
        if (tail_ == N) {
            tail_ = 0;
        }
    }

    auto peek() const -> const T& {
        return elems_[head_];
    }

    auto pop() -> void {
        if (size_ == 0) {
            return;
        }
        ++head_;
        --size_;
        if (head_ == N) {
            head_ = 0;
        }
    }

    auto size() const -> std::size_t {
        return size_;
    }


    private:
    std::size_t head_;
    std::size_t tail_;
    std::size_t size_;
    T elems_[N];
};
template <typename T>
ring(std::initializer_list<T> il) -> ring<T, sizeof(il)>;
// template <typename InputIt>
// ring(InputIt first, InputIt last) -> ring<typename std::iterator_traits<InputIt>::value_type, 3>;


#endif // COMP6771_RING_H


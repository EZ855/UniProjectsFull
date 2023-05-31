#include "./matrix.h"
#include <cstddef>
#include <utility>

matrix::matrix() noexcept : data_{nullptr}, n_rows_{0}, n_cols_{0} {}
matrix::matrix(std::initializer_list<std::initializer_list<int>> il) {
    auto length = std::distance(il.begin()->begin(), il.begin()->end());
    for (std::initializer_list<int> list : il) {
        if (std::distance(list.begin(), list.end()) != length) {
            throw std::logic_error("Columns are not equal length");
        }
    }
    n_rows_ = static_cast<size_t>(
        std::distance(il.begin(), il.end())
    );
    n_cols_ = static_cast<size_t>(
        std::distance(il.begin()->begin(), il.begin()->end())
    );
    // data_ = std::unique_ptr<int[]>{new int[n_rows_][n_cols_]};
}
matrix::matrix(const matrix &other) : n_rows_{other.n_rows_}, n_cols_{other.n_cols_} {
    // deep copy matrix
}
matrix::matrix(matrix &&other) {
    n_rows_ = std::exchange(other.n_rows_, 0);
    n_cols_ = std::exchange(other.n_cols_, 0);
    data_ = std::exchange(other.data_, nullptr);
}
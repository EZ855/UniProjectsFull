#ifndef COMP6771_MATRIX_H
#define COMP6771_MATRIX_H

#include <initializer_list>
#include <algorithm>
#include <cstddef>
#include <memory>
#include <stdexcept>
#include <string>
#include <utility>
class matrix {

    matrix() noexcept;
    matrix(std::initializer_list<std::initializer_list<int>> il);

    matrix(const matrix &other);
    matrix(matrix &&other);

    matrix &operator=(const matrix &other);
    matrix &operator=(matrix &&other) noexcept;

    int &operator()(std::size_t r, std::size_t c);//{
    //     if (r >= n_rows_ || c >= n_cols_) {
    //         throw std::domain_error(
    //             "(" + std::to_string(r) + ", " + std::to_string(c) +
    //             ") does not fit within a matrix with dimensions (" +
    //             std::to_string(n_rows_) + ", " + std::to_string(n_cols_) + ")"
    //         );
    //     }
    //     return &data_.get()[r][c];
    // }
    const int &operator()(std::size_t r, std::size_t c) const; //{
    //     if (r >= n_rows_ || c >= n_cols_) {
    //         throw std::domain_error(
    //             "(" + std::to_string(r) + ", " + std::to_string(c) +
    //             ") does not fit within a matrix with dimensions (" +
    //             std::to_string(n_rows_) + ", " + std::to_string(n_cols_) + ")"
    //         );
    //     }
        
    //     // return data_.get()[r][c];
    // }

    bool operator==(const matrix &rhs) const noexcept {
        if (n_rows_ !=rhs.dimensions().first || n_cols_ !=rhs.dimensions().second ) {
            return false;
        }
        for (size_t i = 0; i < n_rows_; ++i) {
            for (size_t j = 0; j < n_cols_; ++j) {
                // if (data_[i][j] != rhs.data()[i][j]) {
                //     return false;
                // }
            }
        }
        return true;
    }

    std::pair<std::size_t, std::size_t> dimensions() const noexcept {
        return std::pair<std::size_t, std::size_t> {n_rows_, n_cols_};
    }
    const int *data() const noexcept {
        return data_.get();
    }


    private:
    std::unique_ptr<int[]> data_;
    std::size_t n_rows_;
    std::size_t n_cols_;
};

#endif // COMP6771_MATRIX_H

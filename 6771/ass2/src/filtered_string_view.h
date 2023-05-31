#ifndef COMP6771_ASS2_FSV_H
#define COMP6771_ASS2_FSV_H

#include <compare>
#include <exception>
#include <cstddef>
#include <functional>
#include <iterator>
#include <string>
#include <vector>

namespace fsv {
    using filter = std::function<bool(const char &)>;

    class filtered_string_view {
        template <typename ValueType>
        class iter {
        public:
            friend filtered_string_view;
            using iterator_category = std::bidirectional_iterator_tag;
            using value_type = char;
            using reference_type = const char&;
            using pointer_type = void;
            using difference_type = std::ptrdiff_t;

            iter() = default;
            iter(const iter &) = default;
            iter &operator=(const iter &) = default;

            auto operator*() const noexcept {
                return *it_ptr_;
            }

            auto operator->() const noexcept {
                return it_ptr_;
            }

            auto operator++() noexcept -> iter& {
                ++it_ptr_;
                ++it_cur_pos_;
                while (!it_pred_(*it_ptr_) && it_cur_pos_ <= static_cast<int>(it_size_)) {
                    ++it_ptr_;
                    ++it_cur_pos_;
                }
                return *this;
            }
            auto operator++(int) -> iter {
                auto self = *this;
                ++(*this);
                return self;
            }

            auto operator--() noexcept -> iter& {
                --it_ptr_;
                --it_cur_pos_;
                while (!it_pred_(*it_ptr_) && it_cur_pos_ >= 0) {
                    --it_ptr_;
                    --it_cur_pos_;
                }
                return *this;
            }
            auto operator--(int) -> iter {
                auto self = *this;
                --(*this);
                return self;
            }

            friend auto operator==(const iter &it1, const iter &it2) -> bool {
                // Testing if pred1 == pred2
                bool pred_equal = true;
                for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
                    if (it1.it_pred_(c) != it2.it_pred_(c)) {
                        pred_equal = false;
                    }
                }
                return (
                    it1.it_ptr_ == it2.it_ptr_ &&
                    it1.it_size_ == it2.it_size_ &&
                    pred_equal &&
                    it1.it_cur_pos_ == it2.it_cur_pos_ 
                );
            };
            friend auto operator!=(const iter &it1, const iter &it2) -> bool {
                // Testing if pred1 == pred2
                bool pred_equal = true;
                for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
                    if (it1.it_pred_(c) != it2.it_pred_(c)) {
                        pred_equal = false;
                    }
                }
                return !(
                    it1.it_ptr_ == it2.it_ptr_ &&
                    it1.it_size_ == it2.it_size_ &&
                    pred_equal &&
                    it1.it_cur_pos_ == it2.it_cur_pos_ 
                );
            };

        private:
            iter(
                const value_type *base,
                std::size_t it_size,
                std::function<bool(char)> it_pred,
                std::size_t it_cur_pos
                ) : it_ptr_{base}, it_size_{it_size}, it_pred_{it_pred}, it_cur_pos_{static_cast<int>(it_cur_pos)}{}

            const value_type *it_ptr_;
            std::size_t it_size_;
            std::function<bool(char)> it_pred_;
            int it_cur_pos_;
        };


        public:
        using iterator = iter<const char>;
        using const_iterator = iter<const char>;
        using reverse_iterator = std::reverse_iterator<iterator>;
        using const_reverse_iterator = std::reverse_iterator<const_iterator>;

        iterator begin() {
            const char * ptr = ptr_;
            while (!pred_(*ptr)) {
                ++ptr;
            }
            return iterator{ptr, this->size(), pred_, 0};
        }
        iterator end() {
            const char * ptr = ptr_;
            std::size_t size = this->size();
            for (std::size_t i = 0; i < size - 1; ++i) {
                while (!pred_(*ptr)) {
                    ++ptr;
                }
                ++ptr;
            }
            ++ptr;
            return iterator{ptr, this->size(), pred_, this->size()};
        }
        const_iterator begin() const {
            const char * ptr = ptr_;
            while (!pred_(*ptr)) {
                ++ptr;
            }
            return const_iterator{ptr, this->size(), pred_, 0};
        }
        const_iterator end() const {
            const char * ptr = ptr_;
            std::size_t size = this->size();
            for (std::size_t i = 0; i < size - 1; ++i) {
                while (!pred_(*ptr)) {
                    ++ptr;
                }
                ++ptr;
            }
            ++ptr;
            return const_iterator{ptr, this->size(), pred_, this->size()};
        }
        const_iterator cbegin() {
            const char * ptr = ptr_;
            while (!pred_(*ptr)) {
                ++ptr;
            }
            return const_iterator{ptr, this->size(), pred_, 0};
        }
        const_iterator cend() {
            const char * ptr = ptr_;
            std::size_t size = this->size();
            for (std::size_t i = 0; i < size - 1; ++i) {
                while (!pred_(*ptr)) {
                    ++ptr;
                }
                ++ptr;
            }
            ++ptr;
            return const_iterator{ptr, this->size(), pred_, this->size()};
        }

        reverse_iterator rbegin() { return reverse_iterator{end()}; }
        reverse_iterator rend() { return reverse_iterator{begin()}; }
        const_reverse_iterator rbegin() const {return const_reverse_iterator{end()}; }
        const_reverse_iterator rend() const {return const_reverse_iterator{begin()}; }
        const_reverse_iterator crbegin() const {return const_reverse_iterator{end()}; }
        const_reverse_iterator crend() const {return const_reverse_iterator{begin()}; }




        static std::function<bool(char)> default_predicate ;

        filtered_string_view() : ptr_{nullptr}, size_{0}, pred_{default_predicate} {};
        filtered_string_view(const std::string &str) : ptr_{str.data()}, size_{str.size()}, pred_{default_predicate} {};
        filtered_string_view(const std::string &str, std::function<bool(char)> predicate) : ptr_{str.data()}, size_{str.size()}, pred_{predicate} {};
        filtered_string_view(const char *str) : ptr_{str}, pred_{default_predicate} {;
            std::size_t i = 0;
            while (*str != '\0') {
                ++str;
                ++i;
            }
            size_ = i;
        }
        filtered_string_view(const char *str, std::function<bool(char)> predicate) : ptr_{str}, pred_{predicate} {;
            std::size_t i = 0;
            while (*str != '\0') {
                ++str;
                ++i;
            }
            size_ = i;
        }

        filtered_string_view(const filtered_string_view &other) : ptr_{other.ptr_}, size_{other.size_}, pred_{other.pred_} {};
        filtered_string_view(filtered_string_view &&other) {;
            ptr_ = std::exchange(other.ptr_, nullptr);
            size_ = std::exchange(other.size_, 0);
            pred_ = std::exchange(other.pred_, default_predicate);
        }

        ~filtered_string_view() = default;


        filtered_string_view &operator=(const filtered_string_view &other) {
            if (this == &other) {
                return *this;
            }
            ptr_ = other.ptr_;
            size_ = other.size_;
            pred_ = other.pred_;
            return *this;
        };
        filtered_string_view &operator=(filtered_string_view &&other) {
            if (this == &other) {
                return *this;
            }
            ptr_ = std::exchange(other.ptr_, nullptr);
            size_ = std::exchange(other.size_, 0);
            pred_ = std::exchange(other.pred_, default_predicate);
            return *this;
        };
        char operator[](int i) const {
            int j = 0;
            const char* cur = ptr_;
            // Gets next qualified letter
            while (!pred_(*cur)) {
                ++cur;
            }
            // Puts the ith letter that qualifies into cur
            while (j < i || !pred_(*cur)) {
                if (pred_(*cur)) {
                    ++cur;
                    ++j;
                }
                else {
                    // Skipping unqualified letters
                    while(!pred_(*cur)) {
                        ++cur;
                    }
                    ++j;
                }
            }
            return *cur;
        };
        explicit operator std::string() const {
            std::string res = "";
            const char* cur = ptr_;
            // Goes through entire underlying string
            for (std::size_t i = 0; i < size_; ++i) {
                if (pred_(*cur)) {
                    res += *cur;
                }
                ++cur;
            }
            return res;
        };
        auto at(int index) const -> std::optional<char> {
            int i = 0;
            const char* cur = ptr_;
            // Goes through entire underlying string
            for (std::size_t j = 0; j < size_; ++j) {
                if (pred_(*cur)) {
                    if (i == index) {
                        i = -1;
                        break;
                    }
                    else {
                        ++i;
                    }
                }
                ++cur;
            }
            std::optional<char> res = {};
            if (i == -1) {
                res.emplace(*cur);
            }
            return res;
        };
        auto size() const -> std::size_t {
            std::size_t i = 0;
            const char* cur = ptr_;
            // Goes through entire underlying string
            for (std::size_t j = 0; j < size_; ++j) {
                if (pred_(*cur)) {
                    ++i;
                }
                ++cur;
            }
            return i;
        };
        auto empty() const noexcept -> bool {
            return this->size() == 0;
        };
        auto data() const noexcept -> const char * {
            return ptr_;
        };
        auto predicate() const noexcept -> const std::function<bool(char)>& {
            return pred_;
        };
        
        
        private:
        const char* ptr_;
        std::size_t size_;
        std::function<bool(char)> pred_;
    
    };
}

#endif // COMP6771_ASS2_FSV_H

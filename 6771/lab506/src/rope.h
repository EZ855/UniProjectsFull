#ifndef COMP6771_ROPE_H
#define COMP6771_ROPE_H

#include <iterator>
#include <string>
#include <vector>
#include <utility>


class rope {
    // Credit: https://gitlab.cse.unsw.edu.au/COMP6771/23T1/lecture-code/-/blob/main/src/4_2_istack_example.cpp
    template <typename ValueType>
    class iter {
        friend rope;
		public:
		using iterator_category = std::bidirectional_iterator_tag;
		using value_type = char;
        using value_type2 = std::string;
		using reference_type = value_type&;
		using pointer_type = value_type*;
		using difference_type = std::ptrdiff_t;

		iter() = default;
        iter(const iter &) = default;
        iter &operator=(const iter &) = default;
        bool operator==(const iter&) const = default;

        value_type& operator*() const {
            return *ptr_;
        }
        value_type* operator->() const {
            return ptr_;
        }

        iter &operator++() {
            ++ptr_;
            if (ptr_ == ref_.end()) {
            
            }
            return *this;
        }
        
        // this is the canonical way to write post-increment in terms of pre-increment.
        // can be remembered and written verbatim.
        iter operator++(int) {
           auto self = *this;
            ++**this;
            return self;
        }

        iter &operator--() {
            --ptr_;
            return *this;
        }
        // this is the canonical way to write post-decrement in terms of pre-decrement.
        // can be remembered and written verbatim.
        iter operator--(int) {
            auto self = *this;
            --**this;
            return self;
        }
        

       private:
        iter(value_type *base, value_type2 &base2) : ptr_{base}, ref_{base2} {}
        value_type *ptr_;
        value_type2 ref_;
    };

    public:
	// these are public typedefs required for an STL-compliant container.
   // the only logical difference between an iterator and a const-iterator is the mutability of its value type
   // hence we use a very simple template to reduce code duplication.
    using iterator = iter<char>; // value_type is int, mutable
    using const_iterator = iter<const char>; // value_type is const int, immutable
    
    // these are the canonical way of creating reverse_iterators for bidirectional ranges and greater.
    // can be written and remembered verbatim.
    using reverse_iterator = std::reverse_iterator<iterator>;
    using const_reverse_iterator = std::reverse_iterator<const_iterator>;

    // all in all there are 12 of these range functions.
    // 2 begin() variants (one is const-qualified)
    // 2 end() variants (one is const-qualified)
    // 1 cbegin(), 1 cend()
    // altogether 6 for forward traversal,
    // 6 for backwards traversal.
    iterator begin() {
        auto cur_vec = rope_.front();
        return iterator{cur_vec.data() - 1, cur_vec};
    }
    iterator end() {
        auto cur_vec = rope_.back();
        return iterator{cur_vec.data() + cur_vec.size() - 1, cur_vec};
    }
    const_iterator begin() const {
        auto cur_vec = rope_.front();
        return const_iterator{cur_vec.data() + cur_vec.size() - 1, cur_vec};
    }
    const_iterator end() const {
        auto cur_vec = rope_.back();
        return const_iterator{cur_vec.data() + cur_vec.size() - 1, cur_vec};
    }
    const_iterator cbegin() {
        auto cur_vec = rope_.front();
        return const_iterator{rope_.data()->data(), cur_vec};
    }
    const_iterator cend() {
        auto cur_vec = rope_.back();
        return const_iterator{cur_vec.data() + cur_vec.size() - 1, cur_vec};
    }

    // these also can be remembered verbatim as this is the canonical way to write the reverse iterator range functions.
    reverse_iterator rbegin() { return reverse_iterator{end()}; }
    reverse_iterator rend() { return reverse_iterator{begin()}; }
    const_reverse_iterator rbegin() const {return const_reverse_iterator{end()}; }
    const_reverse_iterator rend() const {return const_reverse_iterator{begin()}; }
    const_reverse_iterator crbegin() const {return const_reverse_iterator{end()}; }
    const_reverse_iterator crend() const {return const_reverse_iterator{begin()}; }

	rope() = default;

	explicit rope(std::vector<std::string> rope) : rope_{std::move(rope)} {}

private:
	std::vector<std::string> rope_;

};

#endif // COMP6771_ROPE_H
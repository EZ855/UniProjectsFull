#ifndef COMP6771_RATIONAL_OO_H
#define COMP6771_RATIONAL_OO_H

#include <optional>


class rational_number {
public:
    static std::optional<rational_number> null;

    static auto make_rational(int numerator, int denominator) -> std::optional<rational_number> {
        if (denominator == 0) {
            return null;
        } else {
            return std::optional<rational_number>{rational_number{numerator, denominator}};
        }
    }

    auto value() const -> double {
        return static_cast<double>(numerator_) / denominator_;
    }

    friend auto add(const rational_number &lhs, const rational_number &rhs) -> rational_number {
        auto ad = lhs.numerator_ * rhs.denominator_;
        auto bc = lhs.denominator_ * rhs.numerator_;
        auto bd = lhs.denominator_ * rhs.denominator_;

        return rational_number(ad + bc, bd);
    }

    friend auto sub(const rational_number &lhs, const rational_number &rhs) -> rational_number {
        auto ad = lhs.numerator_ * rhs.denominator_;
        auto bc = lhs.denominator_ * rhs.numerator_;
        auto bd = lhs.denominator_ * rhs.denominator_;

        return rational_number(ad - bc, bd);
    }

    friend auto mul(const rational_number &lhs, const rational_number &rhs) -> rational_number {
        auto ac = lhs.numerator_ * rhs.numerator_;
        auto bd = lhs.denominator_ * rhs.denominator_;

        return rational_number(ac, bd);
    }

    friend auto div(const rational_number &lhs, const rational_number &rhs) -> std::optional<rational_number> {
        auto ad = lhs.numerator_ * rhs.denominator_;
        auto bc = lhs.denominator_ * rhs.numerator_;

        return make_rational(ad, bc);
    }

    friend auto eq(const rational_number &lhs, const rational_number &rhs) -> bool {
        rational_number r1_simp = simplify(lhs);
        rational_number r2_simp = simplify(rhs);
        
        return r1_simp.numerator_ == r2_simp.numerator_ && r1_simp.denominator_ == r2_simp.denominator_;
    }

    friend auto ne(const rational_number &lhs, const rational_number &rhs) -> bool {
        return !eq(lhs, rhs);
    }

    static rational_number simplify (rational_number r1) {
        if (r1.denominator_ % r1.numerator_ == 0) {
            int new_r1_denom = r1.denominator_ / r1.numerator_;
            int factor1 = r1.denominator_ / new_r1_denom;
            int new_r1_num = r1.numerator_ / factor1;
            std::optional<rational_number> ret = rational_number::make_rational(new_r1_num, new_r1_denom);
            return ret.value();
        }
        else {
            return r1;
        }
    }

    int operator[] (char c) const {
        return c == '^' ? numerator_ : denominator_;
    }
    int &operator[] (char c) {
        return c == '^' ? numerator_ : denominator_;
    }


private:
    rational_number(int numerator, int denominator) : numerator_{numerator}, denominator_{denominator} {}

    int numerator_;
    int denominator_;
};

#endif // COMP6771_RATIONAL_OO_H

#include "./rational.h"

rational_number simplify (rational_number r1) {
    if (r1.denom_ % r1.num_ == 0) {
        int new_r1_denom = r1.denom_ / r1.num_;
        int factor1 = r1.denom_ / new_r1_denom;
        int new_r1_num = r1.num_ / factor1;
        std::optional<rational_number> ret = rational_number::make_rational(new_r1_num, new_r1_denom);
        return ret.value();
    }
    else {
        return r1;
    }
}

const std::optional<rational_number> rational_number::no_ = {};

rational_number add(rational_number r1, rational_number r2) {
    int new_num = r1.num_*r2.denom_ + r2.num_*r1.denom_;
    int new_denom = r1.denom_*r2.denom_;
    std::optional<rational_number> ret = rational_number::make_rational(new_num, new_denom);
    return simplify(ret.value());
}
rational_number sub(rational_number r1, rational_number r2) {
    int new_num = r1.num_*r2.denom_ - r2.num_*r1.denom_;
    int new_denom = r1.denom_*r2.denom_;
    std::optional<rational_number> ret = rational_number::make_rational(new_num, new_denom);
    return simplify(ret.value());
}
rational_number mult(rational_number r1, rational_number r2) {
    int new_num = r1.num_ * r2.num_;
    int new_denom = r1.denom_ * r2.denom_;
    std::optional<rational_number> ret = rational_number::make_rational(new_num, new_denom);
    return ret.value();
}
rational_number div(rational_number r1, rational_number r2) {
    int new_num = r1.num_ * r2.denom_;
    int new_denom = r1.denom_ * r2.num_;
    std::optional<rational_number> ret = rational_number::make_rational(new_num, new_denom);
    return ret.value();
}
bool eq(rational_number r1, rational_number r2) {

    rational_number r1_simp = simplify(r1);
    rational_number r2_simp = simplify(r2);
    
    if (r1_simp.num_ == r2_simp.num_ && r1_simp.denom_ == r2_simp.denom_) {
        return true;
    }
    else {
        return false;
    }
    
}

bool ne(rational_number r1, rational_number r2) {
    return !eq(r1, r2);
}

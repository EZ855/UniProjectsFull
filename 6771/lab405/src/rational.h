#ifndef COMP6771_RATIONAL_H
#define COMP6771_RATIONAL_H

#include <optional>

class rational_number {
    private:
    static const std::optional<rational_number> no_;
    rational_number(int num, int denom) : num_{num}, denom_{denom} {}

    public:
    // data members public for testing
    int num_, denom_;

    static auto make_rational(int num, int denom) -> std::optional<rational_number> {
        if (denom == 0) {
            return no_;
        }
        else {
            rational_number rn {num, denom};
            std::optional<rational_number> rno = rn;
            return rno;
        }
        rational_number rn {num, denom};
        std::optional<rational_number> rno = rn;
        return rno;
    }

    friend rational_number add(rational_number r1, rational_number r2);
    friend rational_number sub(rational_number r1, rational_number r2);
    friend rational_number mult(rational_number r1, rational_number r2);
    friend rational_number div(rational_number r1, rational_number r2);
    friend bool eq(rational_number r1, rational_number r2);
    friend bool ne(rational_number r1, rational_number r2);
};

#endif // COMP6771_RATIONAL_H
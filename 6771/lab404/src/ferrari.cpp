#include "./ferrari.h"

ferrari::ferrari(const std::string &owner, int modelno) : owner_{owner}, modelno_{modelno}, speed_{0}{}
ferrari::ferrari() : ferrari::ferrari("unknown", 6771) {}
ferrari::~ferrari() = default;

const std::pair<std::string, int> ferrari::get_details() {
    std::pair<std::string, int> deets = {owner_, modelno_};
    return deets;
}
void ferrari::drive(int spd) {
    speed_ = spd;
}
const std::string ferrari::vroom() {
    if (speed_ < 20) {
        return "";
    }
    else if (speed_ < 80) {
        return "vroom!!";
    }
    else {
        return "VROOOOOOOOM!!!";
    }
}
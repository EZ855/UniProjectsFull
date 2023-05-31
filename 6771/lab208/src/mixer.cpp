#include "mixer.h"
#include <iterator>
#include <optional>

auto wacky_colour(paint p1, paint p2) -> std::optional<paint> {
    switch (p1) {
        case RED:
            switch (p2) {
                case GREEN:
                    return std::optional<paint> {YELLOW};
                case BLUE:
                    return std::optional<paint> {MAGENTA};
                default:
                    return std::optional<paint> {};
            }
        case GREEN:
            switch (p2) {
                case BLUE:
                    return std::optional<paint> {CYAN};
                default:
                    return std::optional<paint> {};
            }
        case YELLOW:
            switch (p2) {
                case MAGENTA:
                    return std::optional<paint> {BROWN};
                case CYAN:
                    return std::optional<paint> {BROWN};
                default:
                    return std::optional<paint> {};
            }
        case CYAN:
            switch (p2) {
                case MAGENTA:
                    return std::optional<paint> {BROWN};
                default:
                    return std::optional<paint> {};
            }
        case BROWN:
            switch (p2) {
                case BROWN:
                    return std::optional<paint> {BROWN};
                default:
                    return std::optional<paint> {};
            }
        default:
            return std::optional<paint> {};
    }
}


auto mix(const std::vector<paint> &paints, std::function<std::optional<paint>(paint, paint)> fn) -> std::optional<paint> {
    auto iter = paints.begin();
    auto temp = *iter++;
    auto temp2 = *iter++;
    std::optional<paint> cur = fn(temp, temp2);
    
    for (; iter!=paints.end(); ++iter) {
        if (cur != std::optional<paint> {}) {
            cur = fn(cur.value(), *iter);
        }
        else {
            return cur;
        }
    }
    return cur;
}

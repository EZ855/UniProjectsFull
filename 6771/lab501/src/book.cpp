#include "./book.h"
#include <cmath>
#include <iostream>
bool operator==(const book &lhs, const book &rhs) {
    return lhs.isbn() == rhs.isbn();

}
bool operator!=(const book &lhs, const book &rhs) {
    return !operator==(lhs, rhs);
}
bool operator<(const book &lhs, const book &rhs) {
    return lhs.isbn() < rhs.isbn();
}
std::ostream &operator<<(std::ostream &os, const book &b) {
    os << b.name() + ", " + b.author() + ", " + b.isbn() + ", ";
    os << round(b.price() * 100.0) / 100.0;
    return os;
}
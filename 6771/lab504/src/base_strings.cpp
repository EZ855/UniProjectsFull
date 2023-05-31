#include "./base_strings.h"
#include <compare>

std::weak_ordering operator <=> (base2str &b2s, base16str &b16s) {
    return b2s.bits <=> b16s.hexits;
}
std::weak_ordering operator <=> (base16str &b16s, base2str &b2s) {
    return b16s.hexits <=> b2s.bits;
}
std::strong_ordering operator <=> (base16str &b16s1, base16str &b16s2) {
    return b16s1.hexits <=> b16s2.hexits;
}
std::strong_ordering operator <=> (base2str &b2s1, base2str &b2s2) {
    return b2s1.bits <=> b2s2.bits;
}
#include "sort3.h"
#include <catch2/catch.hpp>

TEST_CASE("int sort average case") {
    int a=3, b=2, c=1;
    sort3(a, b, c);
    CHECK(a==1);
    CHECK(b==2);
    CHECK(c==3);
}

TEST_CASE("int sort edge case") {
    int a=-199, b=433512, c=0;
    sort3(a, b, c);
    CHECK(a==-199);
    CHECK(b==0);
    CHECK(c==433512);
}

TEST_CASE("string sort average case") {
    std::string a="Heeheeheehaw", b="Grrr", c="Hawhawhawhee";
    sort3(a, b, c);
    CHECK(a=="Grrr");
    CHECK(b=="Hawhawhawhee");
    CHECK(c=="Heeheeheehaw");
}

TEST_CASE("string sort edge case") {
    std::string a="              ", b=";als;dfl;l", c="-1204912-048";
    sort3(a, b, c);
    CHECK(a=="              ");
    CHECK(b=="-1204912-048");
    CHECK(c==";als;dfl;l");
}
#ifndef COMP6771_ORDER_H
#define COMP6771_ORDER_H

#include <iostream>

struct A {
    A() { std::cout << "A"; }
    ~A() { std::cout << "~A"; }
};

struct B : virtual A {
    A hi;
    B() { std::cout << "B"; }
    ~B() { std::cout << "~B"; }
};

struct C : virtual A {
    B Hi;
    C() { std::cout << "C"; }
    ~C() { std::cout << "~C"; }
};

struct D : virtual C, virtual B {
    C hey;
    A hi;
    D() { std::cout << "D"; }
    ~D() { std::cout << "~D"; }
};


#endif // COMP6771_ORDER_H

#ifndef COMP6771_VEC3_H
#define COMP6771_VEC3_H

struct vec3 {
    union {
        double x, r, s;
    };
    union {
        double y, g, t;
    };
    union {
        double z, b, p;
    };
    
    vec3() : x{0}, y{0}, z{0} {};
    explicit vec3(double c) : x{c}, y{c}, z{c} {};
    vec3(double a, double b, double c) : x{a}, y{b}, z{c} {};

    vec3(const vec3 &other) = default;
    ~vec3() = default;
};

#endif // COMP6771_VEC3_H
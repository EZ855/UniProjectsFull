#ifndef COMP6771_CONCEPTION_H
#define COMP6771_CONCEPTION_H


#include <concepts>
#include <string>
template <typename A>
concept animal = requires {
    std::regular<A>;
    std::same_as<typename A::name_type, const char [8+3]>;
    std::is_same_v<decltype(std::declval<const A>().cry()), std::string>;
};

struct dog {
    public:
    std::string cry() const {return "woof";}
    using name_type = const char [8+3];
};

struct neko {
    public:
    std::string cry() const {return "nyaa";}
    using name_type = const char [8+3];
};

struct duck {
    public:
    std::string cry() const {return "quack";}
    using name_type = const char [8+3];
};

struct robot {
    public:
    using name_type = const char [8+3];
};

namespace q2 {
    struct outer {
        class inner {
            inner();
        };
    };
}

#endif // COMP6771_CONCEPTION_H

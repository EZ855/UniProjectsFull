#ifndef COMP6771_NAMESPACED_H
#define COMP6771_NAMESPACED_H

#include <string>
#include <vector>

struct celestial_body {
    std::string name;
    int pos;
};

namespace comp6771 {
    using namespace std;
    
    class planet {
        public:
        std::string name;
        int pos;
        planet(std::string name, int pos) {
            this->name = name;
            this->pos = pos;
        }
        class terrestrial {
            public:
            std::string name;
            int pos;
            terrestrial(std::string name, int pos) {
                this->name = name;
                this->pos = pos;
            }
        };
    };

}

// Hint: type aliases in modern C++ also use the "using" directive...

#endif // COMP6771_NAMESPACED_H
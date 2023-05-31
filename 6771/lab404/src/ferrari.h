#ifndef COMP6771_FERRARIPP_H
#define COMP6771_FERRARIPP_H

#include <string>
#include <utility>

class ferrari {
    public:
    // members public for testing purposes
    std::string owner_;
    int modelno_;
    int speed_;

    ferrari(const std::string &owner, int modelno);
    ferrari();
    ~ferrari();

    const std::pair<std::string, int> get_details();
    void drive(int spd);
    const std::string vroom();
};

#endif  // COMP6771_FERRARIPP_H
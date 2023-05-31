#ifndef COMP6771_BOOK_H
#define COMP6771_BOOK_H
#include<string>


class book {
    public:

    book(const std::string &name, const std::string &author, const std::string &isbn, double price) : name_{name}, author_{author}, isbn_{isbn}, price_{price} {};
    
    explicit operator std::string() const {
        return author_ + ", " + name_;
    };
    const std::string &name() const {
        return name_;
    };
    const std::string &author() const {
        return author_;
    };
    const std::string &isbn() const {
        return isbn_;
    };
    const double &price() const {
        return price_;
    };

    friend bool operator==(const book &lhs, const book &rhs);
    friend bool operator!=(const book &lhs, const book &rhs);
    friend bool operator<(const book &lhs, const book &rhs);
    friend std::ostream &operator<<(std::ostream &os, const book &b);

    private:
    std::string name_;
    std::string author_;
    std::string isbn_;
    double price_;
};

#endif // COMP6771_BOOK_H

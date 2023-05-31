#ifndef COMP6771_EXPR_H
#define COMP6771_EXPR_H

#include <memory>

class expr {
public:
    virtual double eval() const = 0;
    virtual ~expr() = default;
};

class literal : public expr {
private:
    double double_;
public:
    literal(double d):double_{d}{}
    virtual double eval() const override {return double_;}
    virtual ~literal() = default;
};

class multiply : public expr {
private:
    double d1_;
    double d2_;

public:
    multiply(std::unique_ptr<expr> e1, std::unique_ptr<expr> e2):d1_{e1.get()->eval()}, d2_{e2.get()->eval()}{}
    virtual double eval() const override {
        return d1_ * d2_;
    }
    virtual ~multiply() = default;
};

class divide : public expr {
private:
    double d1_;
    double d2_;

public:
    divide(std::unique_ptr<expr> e1, std::unique_ptr<expr> e2):d1_{e1.get()->eval()}, d2_{e2.get()->eval()}{}
    virtual double eval() const override {
        return d1_ / d2_;
    }
    virtual ~divide() = default;
};

class plus : public expr {
private:
    double d1_;
    double d2_;

public:
    plus(std::unique_ptr<expr> e1, std::unique_ptr<expr> e2):d1_{e1.get()->eval()}, d2_{e2.get()->eval()}{}
    virtual double eval() const override {
        return d1_ + d2_;
    }
    virtual ~plus() = default;
};

class minus : public expr {
private:
    double d1_;
    double d2_;

public:
    minus(std::unique_ptr<expr> e1, std::unique_ptr<expr> e2):d1_{e1.get()->eval()}, d2_{e2.get()->eval()}{}
    virtual double eval() const override {
        return d1_ - d2_;
    }
    virtual ~minus() = default;
};

#endif // COMP6771_EXPR_H

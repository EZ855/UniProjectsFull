# Question 0

In `src/q0.txt`, write the alternative which most accurately answers the question below. The answer **must** be one of `a`, `b`, `c`, or `d` (note the lowercase).

----

Consider the following code:
```cpp
#include <iostream>
#include <utility>

auto e(const int& i) -> void {
	std::cout << "1 ";
}

auto e(int& i) -> void {
	std::cout << "2 ";
}

template <typename T>
auto f(T&& a) -> void {
	e(std::forward<A>(a));
}

int k{1};

auto g() -> int {
	return k;
}

auto main() -> int {
	f(1);
	int i{1};
	f(i);
	const int &j = i;
	f(j);
	f(g());
}
```
Why is the output of this program `1 2 1 1`?
- a) `1`, `j`, and `g()`, when passed to `f`, are bound to `a` as `const int &`, and `int` does not bind to `int &`, which is why `e(const int &)` is used. Similarly, `i` is bound as `int&`, which is why `e(int &)` is used.
- b) Every value of type `int` is implicitly convertible to `const int&`, which is why the first, third, and fourth calls to `f` then use `e(const int &)`. In the second call to `f`, `a` has type `int &`, which is why the second call to `f` uses `e(int &)`.
- c) `decltype(1)`, `decltype(j)`, and `decltype(g())`, when passed to `f`, results in a value category that is not bindable to `int &`, so `e(int &)` is not called for these arguments. However, `decltype(i)` can be bound to `int &`, so `e(int &)` is used.
- d) `decltype(1)` and `decltype(g())` have the exact same type, and both are only bindable to a `const int &`, which is why `e(const int &)` is used. `decltype(i)` and `decltype(j)` are also both `int &`, but, due to the forwarding reference in `f`, in the third call when `j` is passed, `a` will be deduced to be of type `const int &`, which is why in that case `e(const int &)` is used over `e(int &)`. 

# Question 1

In `src/q1.txt`, write the alternative which most accurately answers the question below. The answer **must** be one of `a`, `b`, `c`, or `d` (note the lowercase).

----

What problem or problems does uniform initialisation seek to solve?
- a) Uniform initialisation solves the problem of overuse of `()` in C++ by instead overusing `{}`. This allows new syntax, such as the lambda ( `[capture](arg){ body }` ) syntax, to be disambiguated from other syntax, such as calling a function.
- b) Uniform initialisation solely solves the problem of making user-defined types act like primitive types (a key tenet of C++) by allowing the reuse of braced initialiser lists to be used with more than just `struct`s, `union`s, `class`es, and C-style arrays.
- c) Uniform initialisation unifies how to initialise a variable in C++ and every other kind of initialisation syntax (for example, the C-style `int var = 0;` syntax) are actually special cases of uniform initialisation.
- d) Uniform initialisation solves the problem where variable initialisation looks like a function declaration and also removes the possibility of accidental narrowing conversions in calls to object constructors.

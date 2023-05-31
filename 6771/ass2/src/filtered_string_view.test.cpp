#include "./filtered_string_view.h"
#include "./filtered_string_view.cpp"

#include <catch2/catch.hpp>
#include <compare>
#include <cstddef>
#include <set>
#include <string>

TEST_CASE("static function default_predicate working") {
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    CHECK(fsv::filtered_string_view::default_predicate(c));
  }
}
TEST_CASE("default constructor working") {
  auto sv = fsv::filtered_string_view{};
  CHECK(sv.data() == nullptr);
  CHECK(sv.size() == 0);
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    CHECK(sv.predicate()(c));
  }
}
TEST_CASE("implicit constructor working") {
  auto s = std::string{"cat"};
  auto sv = fsv::filtered_string_view{s};
  CHECK(sv.data() == s);
  CHECK(sv.size() == s.size());
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    CHECK(sv.predicate()(c));
  }
}
TEST_CASE("constructor with predicate working") {
  auto s = std::string{"cat"};
  auto pred = [](char c) { return c == 'a'; };
  auto sv = fsv::filtered_string_view{s, pred};
  CHECK(sv.data() == s);
  CHECK(sv.size() == 1);
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    if (c == 'a') {
      CHECK(sv.predicate()(c));
    }
    else {
      CHECK(!sv.predicate()(c));
    }
  }
}
TEST_CASE("implicit constructor with null-terminated string working") {
  auto sv = fsv::filtered_string_view{"cat"};
  auto data = sv.data();
  CHECK(*data == 'c');
  CHECK(*++data == 'a');
  CHECK(*++data == 't');
  CHECK(sv.size() == 3);
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    CHECK(sv.predicate()(c));
  }
}
TEST_CASE("implicit constructor with null-terminated string and predicate working") {
  auto pred = [](char c) { return c == 'a'; };
  auto sv = fsv::filtered_string_view{"cat", pred};
  auto data = sv.data();
  CHECK(*data == 'c');
  CHECK(*++data == 'a');
  CHECK(*++data == 't');
  CHECK(sv.size() == 1);
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    if (c == 'a') {
      CHECK(sv.predicate()(c));
    }
    else {
      CHECK(!sv.predicate()(c));
    }
  }
}
TEST_CASE ("copy constructor working") {
  auto sv1 = fsv::filtered_string_view{"bulldog"};
  const auto copy = sv1;
  CHECK(copy.data() == sv1.data());
}
TEST_CASE ("move constructor working") {
  auto sv1 = fsv::filtered_string_view{"bulldog"};
  const auto move = std::move(sv1);

  CHECK(sv1.data() == nullptr);
  CHECK(sv1.size() == 0);
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    CHECK(sv1.predicate()(c));
  }
}
TEST_CASE("copy assignment operator working") {
  auto pred = [](char c) { return c == '4' || c == '2'; };
  auto fsv1 = fsv::filtered_string_view{"42 bro", pred};
  auto fsv2 = fsv1;

  CHECK(fsv1 == fsv2);
}
TEST_CASE("move assignment operator working") {
  auto pred = [](char c) { return c == '8' || c == '9'; };
  auto fsv1 = fsv::filtered_string_view{"'89 baby", pred};
  auto fsv2 = std::move(fsv1);
  auto data = fsv2.data();
  CHECK(*data == '\'');
  CHECK(*++data == '8');
  CHECK(*++data == '9');
  CHECK(fsv2.size() == 2);
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    if (c == '8' || c== '9') {
      CHECK(fsv2.predicate()(c));
    }
    else {
      CHECK(!fsv2.predicate()(c));
    }
  }

  CHECK(fsv1.size() == 0);
  CHECK(fsv1.data() == nullptr);
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    CHECK(fsv1.predicate()(c));
  }
}
TEST_CASE("subscript access operator working") {
  auto pred = [](char c) { return c == '9' || c == '0' || c == ' '; };
  auto fsv1 = fsv::filtered_string_view{"only 90s kids understand", pred};

  CHECK(fsv1[0] == ' ');
  CHECK(fsv1[1] == '9');
  CHECK(fsv1[2] == '0');
  CHECK(fsv1[3] == ' ');
  CHECK(fsv1[4] == ' ');
}
TEST_CASE("string type conversion working") {
  auto pred = [](char c) { return c == '9' || c == '0' || c == ' '; };
  auto fsv1 = fsv::filtered_string_view{"only 90s kids understand", pred};
  auto s = static_cast<std::string>(fsv1);
  CHECK(s == " 90  ");
  CHECK(s.data() != fsv1.data());
}
TEST_CASE("at function working") {
  auto pred = [](char c) { return c == '9' || c == '0' || c == ' '; };
  auto fsv1 = fsv::filtered_string_view{"only 90s kids understand", pred};
  CHECK(fsv1.at(0).value() == ' ');
  CHECK(fsv1.at(1).value() == '9');
  CHECK(fsv1.at(2).value() == '0');
  CHECK(fsv1.at(3).value() == ' ');
  CHECK(fsv1.at(4).value() == ' ');
  CHECK(fsv1.at(5).has_value() == false);
  CHECK(fsv1.at(-1).has_value() == false);
}
TEST_CASE("size function working with predicate") {
  auto pred = [](char c) { return c == '9' || c == '0' || c == ' '; };
  auto fsv1 = fsv::filtered_string_view{"only 90s kids understand", pred};
  CHECK(fsv1.size() == 5);
}
TEST_CASE("size function working without predicate") {
  auto fsv1 = fsv::filtered_string_view{"Maltese"};
  CHECK(fsv1.size() == 7);
}
TEST_CASE("empty function working non-empty") {
  auto fsv1 = fsv::filtered_string_view{"Maltese"};
  CHECK(!fsv1.empty());
}
TEST_CASE("empty function working empty string") {
  auto fsv1 = fsv::filtered_string_view{};
  CHECK(fsv1.empty());
}
TEST_CASE("data function working") {
  auto pred = [](char c) { return c == '9' || c == '0' || c == ' '; };
  auto fsv = fsv::filtered_string_view{"only", pred};
  auto ptr = fsv.data();
  CHECK(*ptr == 'o');
  CHECK(*++ptr == 'n');
  CHECK(*++ptr == 'l');
  CHECK(*++ptr == 'y');
  CHECK(!*++ptr);
}
TEST_CASE("predicate function working") {
  const auto return_false = [](char) {return false;};
  const auto s = fsv::filtered_string_view{"doggo", return_false};
  const auto& predicate = s.predicate();
  for (char c = std::numeric_limits<char>::min(); c != std::numeric_limits<char>::max(); c++) {
    CHECK(!predicate(c));
  }
}
TEST_CASE("equals and not equals comparison working") {
  auto const lo = fsv::filtered_string_view{"aaa"};
  auto const hi = fsv::filtered_string_view{"zzz"};
  CHECK(!(lo == hi));
  CHECK(lo != hi);
}
TEST_CASE("spaceship comparison function working") {
  auto const lo = fsv::filtered_string_view{"aaa"};
  auto const hi = fsv::filtered_string_view{"zzz"};
  CHECK(lo <=> hi == std::strong_ordering::less);
  CHECK(lo < hi);
  CHECK(lo <= hi);
  CHECK(!(lo > hi));
  CHECK(!(lo >= hi));
}
TEST_CASE("fsv to output stream working") {
  auto fsv = fsv::filtered_string_view{"c++ > rust > java", [](char c){ return c == 'c' || c == '+'; }};
  std::cout << fsv;
}
TEST_CASE("compose function working") {
  auto best_languages = fsv::filtered_string_view{"c / c++"};
  auto vf = std::vector<fsv::filter>{
    [](const char &c){ return c == 'c' || c == '+' || c == '/'; },
    [](const char &c){ return c > ' '; },
    [](const char){ return true; }
  };

  auto sv = compose(best_languages, vf);
  auto s = static_cast<std::string>(sv);
  CHECK(s == "c/c++");
}
TEST_CASE("compose function working with empty filter list") {
  auto best_languages = fsv::filtered_string_view{"c / c++"};
  auto vf = std::vector<fsv::filter>{
  };

  auto sv = compose(best_languages, vf);
  auto s = static_cast<std::string>(sv);
  CHECK(s == "c / c++");
}
TEST_CASE("split function working when splitting in two no predicate") {
  auto sv = fsv::filtered_string_view{"0xDEADBEEF/0xdeadbeef"};
  auto tok = fsv::filtered_string_view{"/"};
  auto v = split(sv, tok);
  CHECK(static_cast<std::string>(v[0]) == "0xDEADBEEF");
  CHECK(static_cast<std::string>(v[1]) == "0xdeadbeef");
}
TEST_CASE("split function working when splitting in two with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto tok = fsv::filtered_string_view{" / "};
  auto v = split(sv, tok);
  CHECK(static_cast<std::string>(v[0]) == "DEADBEEF");
  CHECK(static_cast<std::string>(v[1]) == "deadbeef");
}
TEST_CASE("split function working with fsv empty") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"", [&interest](const char &c){ return interest.contains(c); }};
  auto tok = fsv::filtered_string_view{" / "};
  auto v = split(sv, tok);
  CHECK(static_cast<std::string>(v[0]) == "");
}
TEST_CASE("split function working with fsv empty due to predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"!!!!", [&interest](const char &c){ return interest.contains(c); }};
  auto tok = fsv::filtered_string_view{"hello my honey"};
  auto v = split(sv, tok);
  CHECK(static_cast<std::string>(v[0].data()) == "!!!!");
}
TEST_CASE("split function working with tok empty") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto tok = fsv::filtered_string_view{""};
  auto v = split(sv, tok);
  CHECK(static_cast<std::string>(v[0].data()) == "0xDEADBEEF / 0xdeadbeef");
}
TEST_CASE("split function working with tok empty due to predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"hello my honey"};
  auto tok = fsv::filtered_string_view{"!!!!", [&interest](const char &c){ return interest.contains(c); }};
  auto v = split(sv, tok);
  CHECK(static_cast<std::string>(v[0].data()) == "hello my honey");
}
TEST_CASE("split function working when splitting empty strings at front and back") {
  auto fsv = fsv::filtered_string_view{"xax"};
  auto tok  = fsv::filtered_string_view{"x"};
  auto v = split(fsv, tok);
  auto expected = std::vector<fsv::filtered_string_view>{"", "a", ""};
  CHECK(v[0] == expected[0]);
  CHECK(v[1] == expected[1]);
  CHECK(v[2] == expected[2]);
}
TEST_CASE("substr function working") {
  auto fsv = fsv::filtered_string_view{"Siberian Husky"};
  auto sub = substr(fsv, 9);
  CHECK(static_cast<std::string>(sub) == "Husky");
}
TEST_CASE("substr function working with predicate") {
  auto is_upper = [](const char c) { return std::isupper(static_cast<unsigned char>(c));};
  auto fsv = fsv::filtered_string_view{"Siberian Husky", is_upper};
  auto sub = substr(fsv, 0, 2);
  CHECK(static_cast<std::string>(sub) == "SH");
}
TEST_CASE("substr function working with length 0") {
  auto is_upper = [](const char c) { return std::isupper(static_cast<unsigned char>(c));};
  auto fsv = fsv::filtered_string_view{"Siberian Husky", is_upper};
  auto sub = substr(fsv, 13, 0);
  CHECK(static_cast<std::string>(sub) == "");
}
TEST_CASE("iterator begin, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.begin();
  
  CHECK(*it == 'D');
}
TEST_CASE("iterator pre increment working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', '/'};
  auto sv = fsv::filtered_string_view{"0xDEAD BEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it1 = sv.begin();
  ++it1;
  CHECK(*it1 == 'E');++it1;
  CHECK(*it1 == 'A');++it1;
  CHECK(*it1 == 'D');++it1;
  CHECK(*it1 == 'B');++it1;
  CHECK(*it1 == 'E');
}
TEST_CASE("iterator post increment working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', '/'};
  auto sv = fsv::filtered_string_view{"0xDEAD BEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it1 = sv.begin();
  it1++;
  CHECK(*it1 == 'E');it1++;
  CHECK(*it1 == 'A');it1++;
  CHECK(*it1 == 'D');it1++;
  CHECK(*it1 == 'B');it1++;
  CHECK(*it1 == 'E');
}
TEST_CASE("iterator end, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF/0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.end();
  
  CHECK(*it == '\0');
}
TEST_CASE("iterator pre decrement working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', '/'};
  auto sv = fsv::filtered_string_view{"0xDEAD BEEF / 0xdead beef", [&interest](const char &c){ return interest.contains(c); }};
  auto it1 = sv.end();
  --it1;
  CHECK(*it1 == 'f');--it1;
  CHECK(*it1 == 'e');--it1;
  CHECK(*it1 == 'e');--it1;
  CHECK(*it1 == 'b');--it1;
  CHECK(*it1 == 'd');
}
TEST_CASE("iterator post decrement working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', '/'};
  auto sv = fsv::filtered_string_view{"0xDEAD BEEF / 0xdead beef", [&interest](const char &c){ return interest.contains(c); }};
  auto it1 = sv.end();
  it1--;
  CHECK(*it1 == 'f');it1--;
  CHECK(*it1 == 'e');it1--;
  CHECK(*it1 == 'e');it1--;
  CHECK(*it1 == 'b');it1--;
  CHECK(*it1 == 'd');
}
TEST_CASE("iterator begin, == working") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it1 = sv.begin();
  auto it2 = sv.begin();
  
  CHECK(it1 == it2);
}
TEST_CASE("iterator != working") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it1 = sv.begin();
  auto it2 = sv.end();
  
  CHECK(it1 != it2);
}
TEST_CASE("const iterator begin, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  const auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.begin();
  
  CHECK(*it == 'D');
}
TEST_CASE("const iterator end, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  const auto sv = fsv::filtered_string_view{"0xDEADBEEF/0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.end();
  
  CHECK(*it == '\0');
}
TEST_CASE("iterator cbegin, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.cbegin();
  
  CHECK(*it == 'D');
}
TEST_CASE("iterator cend, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF/0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.cend();
  
  CHECK(*it == '\0');
}
TEST_CASE("iterator rbegin, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.rbegin();
  CHECK(*it =='f');
}
TEST_CASE("iterator rend, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF/0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.rend();
  
  CHECK(*it == 'x');
}
TEST_CASE("iterator crbegin, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF / 0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.crbegin();
  
  CHECK(*it == 'f');
}
TEST_CASE("iterator crend, * working with predicate") {
  auto interest = std::set<char>{'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', ' ', '/'};
  auto sv = fsv::filtered_string_view{"0xDEADBEEF/0xdeadbeef", [&interest](const char &c){ return interest.contains(c); }};
  auto it = sv.crend();
  
  CHECK(*it == 'x');
}

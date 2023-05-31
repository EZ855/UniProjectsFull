#include "./rethrow.h"

const std::set<std::string> db_conn::blacklist_ = {"hsmith"};

auto make_connection(db_conn &db, const std::string &uname, const std::string &pword) -> void {
    try {
    db.try_connect(uname, pword);
    } catch (const std::domain_error &de) {
        throw std::string(de.what());
    } catch (const std::runtime_error &re) {
        throw std::string(re.what());
    }
}
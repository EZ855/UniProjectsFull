#include "stats.h"
#include <algorithm>
#include <numeric>
#include <vector>

auto read_marks(const std::string &path) -> std::vector<int> {
    std::ifstream ifs;
    ifs.open(path);
	std::vector<int> res;
	while (ifs.good()) {
		std::string mark;
		std::getline(ifs, mark);
		res.push_back(stoi(mark));
	}
	ifs.close();
    return res;
}

auto calculate_stats(const std::vector<int> &marks) -> stats {
    // Invalid marks check
    if (marks.size() == 0) {
        stats res = {0,0,0,0};
        return res;
    }

    std::vector<int> marks_mutable = marks;
    std::sort(marks_mutable.begin(), marks_mutable.end());

    // Find median
    auto median = 0;
    if ((marks.size() % 2) == 0) {
        auto median_index = marks.size()/2;
        median = (marks_mutable[median_index-1] + marks_mutable[median_index])/2;
    }
    else {
        auto median_index = marks.size()/2 + 1;
        median = marks_mutable[median_index - 1];
    }

    // Find average
    auto average = static_cast<unsigned long>(std::accumulate(marks.begin(), marks.end(), 0)) /marks.size();
    // Construct stats struct
    // Note: average or median will have lost data due to conversion to int
    stats res = {static_cast<int>(average), median, marks_mutable.back(), marks_mutable.front()};
    return res;
}
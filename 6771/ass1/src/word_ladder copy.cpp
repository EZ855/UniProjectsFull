#include "word_ladder.h"
#include <unordered_set>
#include <algorithm>
#include <fstream>
#include <string>
#include <queue>

auto word_ladder::read_lexicon(const std::string &path) -> std::unordered_set<std::string> {
	std::ifstream ifs(path.c_str(), std::ifstream::in);
	std::unordered_set<std::string> res_lexicon;
	while (ifs.good()) {
		std::string line;
		std::getline(ifs, line);
		res_lexicon.insert(line);
	}
	ifs.close();
	return res_lexicon;
}


auto word_ladder::generate(
	const std::string &from,
	const std::string &to,
	const std::unordered_set<std::string> &lexicon
) -> std::vector<std::vector<std::string>> {
	// Initialize queue
	std::queue<std::vector<std::string>> q;
	std::vector<std::string> start {from};
	q.push(start);
	// // Initialize seen words
	// std::unordered_set<std::string> seen_words_all;
	// seen_words_all.insert(from);
	std::unordered_set<std::string> seen_words_current;
	// Filters lexicon down to words that match the length of from/to
	std::unordered_set<std::string> lexicon_filtered;
	auto first = lexicon.begin();
	auto last = lexicon.end();
	while (first!=last) {
		if (first->size() == from.size()) {
			lexicon_filtered.emplace(*first);
		}
		++first;
  	}
	unsigned long l = lexicon_filtered.size();

	if (l != false) {
	
	}

	// std::vector<std::string> alphabet = {
	// 	"a", "b", "c", "d", "e", "f", "g", 
	// 	"h", "i", "j", "k", "l", "m", "n", 
	// 	"o", "p", "q", "r", "s", "t", "u", 
	// 	"v", "w", "x", "y", "z"
	// };

	std::vector<std::vector<std::string>> ladders_return_vector;
	unsigned long smallest_ladder_size = 127145; // english.txt only contains 127142 words
	unsigned long current_ladder_size = 1;
	
	// Loops until queue is empty or all shortest ladders are found
	while (!q.empty()) {

		std::vector<std::string> ladder_cur = q.front();
		q.pop();

		std::string rung_cur = ladder_cur.back();

		// Optimisation -> if current ladder has more rungs than the smallest ladder, automatically quits
		// as there will only be longer ladders thereafter
		if (smallest_ladder_size < ladder_cur.size()) {
			break;
		}
		// Updates seen words every new 'generation' of ladders
		if (ladder_cur.size() != current_ladder_size) {
			// seen_words_all.insert(seen_words_current.begin(), seen_words_current.end());
			// seen_words_current.clear();
			for (std::string word : seen_words_current) {
				lexicon_filtered.erase(word);
			}
			current_ladder_size = ladder_cur.size();
		}

		for (std::string word : lexicon_filtered) {
			auto mismatch_res_1 = std::mismatch(rung_cur.begin(), rung_cur.end(), word.begin());
			if (mismatch_res_1.first != rung_cur.end()) {
				mismatch_res_1.first++;
				mismatch_res_1.second++;
				auto mismatch_res_2 = std::mismatch(mismatch_res_1.first, rung_cur.end(), mismatch_res_1.second);
				if (mismatch_res_2.first == rung_cur.end()) {

					std::vector<std::string> ladder_new = ladder_cur;
					ladder_new.emplace_back(word);

					if (word != to) {
						q.emplace(ladder_new);
						seen_words_current.emplace(word);
					}
					else {
						ladders_return_vector.emplace_back(ladder_new);
						smallest_ladder_size = ladder_cur.size();
					}
				}
			}
		}
		// // Starting from the first letter of the current rung, replaces that letter with a letter from the alphabet. Does this
		// // for each letter in the word and for every letter in the alphabet.
		// // If the new word exists in lexicon, creates a new ladder with the appended new rung/word and then 
		// // adds it to the end of the queue.
		// for (unsigned long i = 0; i < rung_cur.length(); i++) {
		// 	for (std::string s : alphabet) {

		// 		std::string rung_new = rung_cur;
		// 		rung_new.replace(i, 1, s);

		// 		if (seen_words_all.count(rung_new) == 0 && lexicon_filtered.count(rung_new) == 1) {
		// 			std::vector<std::string> ladder_new = ladder_cur;
		// 			ladder_new.emplace_back(rung_new);

		// 			// If the new 
		// 			if (rung_new.compare(to) != 0) {
		// 				q.emplace(ladder_new);
		// 				seen_words_current.emplace(rung_new);
		// 			}
		// 			else {
		// 				ladders_return_vector.emplace_back(ladder_new);
		// 				smallest_ladder_size = ladder_cur.size();
		// 			}
		// 		}
		// 	}
		// }
	}
	
	std::sort(ladders_return_vector.begin(), ladders_return_vector.end());
	return ladders_return_vector;
}
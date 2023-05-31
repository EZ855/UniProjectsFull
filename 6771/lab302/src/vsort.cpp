#include "vsort.h"
#include <algorithm>

auto vsort(std::vector<std::string> &vs) -> void {
	(void) vs;
	std::sort(vs.begin(), vs.end(), 
	[](const auto& s1, const auto& s2)-> bool {
		int vowels1 = 0;
		int vowels2 = 0;
		for (const char& c : s1) {
			if(c=='a'|| c=='e'|| c=='i'|| c=='o' || c=='u'
        	 ||c=='A'|| c=='E'|| c=='I'|| c=='O' ||c=='U')
			{
				vowels1++;
			}
		}
		for (const char& c : s2) {
			if(c=='a'|| c=='e'|| c=='i'|| c=='o' || c=='u'
        	 ||c=='A'|| c=='E'|| c=='I'|| c=='O' ||c=='U')
			{
				vowels2++;
			}
		}
		if (vowels1 == vowels2) {
			return s1 < s2;
		}
		else {
			return vowels1 < vowels2;
		}
	});
}
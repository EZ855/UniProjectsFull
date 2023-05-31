#ifndef COMP6771_EXAM_SAMPLE_Q2
#define COMP6771_EXAM_SAMPLE_Q2


#include <cstddef>
#include <memory>

template <typename T>
class sparse_matrix {

private:
    std::unique_ptr<T[]> vals_;
    std::unique_ptr<std::size_t[]> cidx_;
    
};

#endif // COMP6771_EXAM_SAMPLE_Q2
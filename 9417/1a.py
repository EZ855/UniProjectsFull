import numpy
"""
x = 1.0 - 0.1 * 3.0 * numpy.power(1,2) * numpy.power((numpy.power(1,3) + 1.0), -1/2)
print(numpy.around(x, 4))

for i in range(1,10000):
    x = x - 0.1 * 3.0 * numpy.power(x,2) * numpy.power((numpy.power(x,3) + 1.0), -1/2)
    print(numpy.around(x, 4))
"""
#A = numpy.array([[1,-1,0], [2,1,-1], [1,0,-2], [-1,2,1]])
A = numpy.array([[1,2,1,-1], [-1,1,0,2], [0,-1,-2,1]])

print(A)

b = numpy.array([[3], [2], [-2]])
print(b)

x = numpy.array([[1],[1],[1],[1]])
print(x)

while ():
    numpy.matmul(A,x)

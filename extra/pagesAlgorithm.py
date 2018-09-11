
# This code was written to come up with a good algorithm to find the number 
# of pictures to load depending on the length of the result
#
#
# blockSize: number of pictures to display in one set (with scrolling)
# maxCounter: the number of times "Show More" button allows user to brows more pictures
# last: the number of pictures in the last set
import random

x = [500,400,300,200,100,10]
y = []

for i in range(100):
  y.append(random.randint(1,500))

maxCounter = 0
blockSize = 0
last = 0

results = []
for value in x:
	if(value>200):
		maxCounter = value/66
		blockSize = 66
	elif(value>100):
		maxCounter = value/45
		blockSize = 45
	else:
		maxCounter = 1
		blockSize = value
	last = value - maxCounter*blockSize
	temp = []
	temp.append(value)
	temp.append(maxCounter)
	temp.append(blockSize)
	temp.append(maxCounter*blockSize)
	temp.append(last)
	results.append(temp)

for res in results:
	print(res)

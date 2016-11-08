
from math import *
from random import random
from collections import defaultdict

def generateBProblem(order, outfile):
	print "Generating %d-grams"%order
	tiles=[]
	counter=0
	while len(tiles)<order :
		remaining=order-len(tiles)
		if tiles==[]:
			randomlength= int( floor(1 + random()*(order-2)))
		elif remaining<4:
			randomlength=remaining

		thisDict=limitedDictionaries[randomlength+1]

		randomind = int(floor(random()*len(thisDict)))

		randomword = thisDict[randomind]
		if tiles==[]:
			lastword=randomword
			oldinter=-2;
			for letter in randomword:
				tiles.append(letter)
		else:
			inter1,inter2,letter=findintersection(lastword,randomword)
			if inter1!=-1 and abs(inter1-oldinter)>1:
				randomword=randomword[:inter2]+randomword[inter2+1:]
				for letter in randomword:
					tiles.append(letter)
				lastword=randomword
				oldinter=inter2
		if counter>10000:
			counter=0
			tiles=[]
		counter+=1
	outfile.write(",".join(tiles)+"\n")


def findintersection(word1, word2):
	for i in range(len(word1)):
		for j in range(len(word2)):
			if word1[i]==word2[j]:
				return i,j,word1[i]
	return -1,-1,""

infile= open("dictionary.txt",'r')
dictionary=infile.readlines()
infile.close()
limitedDictionaries=defaultdict(lambda:[])
for word in dictionary:
	limitedDictionaries[len(word.strip())].append(word.strip())
for dictio in limitedDictionaries:
	print "Dictionary of order %d has %d entries"%(dictio,len(limitedDictionaries[dictio]))

for order in range(15,20):
	filename = "problems/%s-bananagramProblems.txt"%(order)
	outfile=file(filename,'w')
	if order<6:
		probnum=50
	elif order<10:
		probnum=20
	else:
		probnum=5
	for i in range(probnum):
		generateBProblem(order,outfile)
	outfile.close()

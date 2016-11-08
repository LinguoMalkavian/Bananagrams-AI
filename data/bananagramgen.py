
from math import *
from random import random
from collections import DefaultDict

def generateBProblem(order, outfile):
	tiles=[]
	while len(tiles)<order :
             
		randomind = int(floor(random()*len(dictionary)))
		randomword = dictionary[randomind]
		if len(randomword)-1<order-len(tiles):
			if tiles==[]:
				lastword=randomword
				for letter in randomword: 
					tiles.append(letter)
			else:
				inter1,inter2,letter=findintersection(lastword,randomword)
				if inter1!=-1 and abs(inter1-oldinter)>1:
					randomword.pop(inter2)
					for letter in randomword:
						tiles.append(letter)
					lastword=randomword
					oldinter=inter2
		
			tiles=[]
	outfile.write(",".join(tiles))


def findintersection(word1, word2):
	for i in range(len(word1)):
		for j in range(len(word2)):
			if word1[i]==word2[j]:
				return i,j,word[i]
	return -1,-1,""
def main():
    infile= open("dictionary.txt",'r')
    dictionary=infile.readlines()
    infile.close()
    limitedDictionaries=DefaultDict(lambda:[])
    for word in dictionary:
        limitedDictionaries[len(word)].append(word)
    for order in range(4,15):	
    	filename = "problems/%s-bananagramProblems.txt"%(order) 
    	outfile=file(filename,'w')
    	for i in range(20):
    		generateBProblem(order,outfile)
    	outfile.close()

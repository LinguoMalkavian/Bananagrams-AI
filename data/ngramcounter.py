#Pablo Gonzalez Martinez
#N-gram frequency checker 
#This program goes through a dictionary file and constructs ngram counts at varrying lengths and stores them in separate files


from nltk.probability import FreqDist
from collections import defaultdict

def main ():
	dictFileName="dictionary.txt"
	dictFile=open(dictFileName,"r")
	dictionary=dictFile.readlines()
	dictFile.close()
	for n in range(15,25):
		print "%s-grams started"%(n)
		countNgrams(n,dictionary)
		print "%s-grams done"%(n)
	

def countNgrams (n,dictionary):
	fdist=FreqDist()
	for word in dictionary:
		for startind in range(len(word)-n):
			fdist[word[startind:startind+n]]+=1
	filename="%s-gramfreq.txt"%(n)
	printToFile(fdist,filename)

def printToFile (fdist,filename):
	outfile=open(filename,"w")
	for key in fdist:
		line= "%s,%.10f\n"%(key,fdist.freq(key))
		outfile.write(line) 
	outfile.close()

main()
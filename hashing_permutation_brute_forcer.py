import itertools

def f1(Nmax, K): return K % Nmax
def f2(Nmax, K): return K + (K % Nmax)
def doublehashing(i,Nmax,K): return (f1(Nmax,K)+i*f2(Nmax,K)) % Nmax

def insert(wert, arr, f):
	Nmax = len(arr)
	i = 0
	print("inserting", wert)
	while i < 10:
		j = f(i,Nmax,wert)
		print("trying", j)
		if arr[j] == -1:
			arr[j] = wert
			print("wrote", wert, "on pos", j)
			return arr
		i += 1
	raise Exception("i got big")
		

def comparesetvalues(arr, ref):
	k = 0
	for i in arr:
		if i != -1:
			if i != ref[k]:
				print("deviance on pos" , k, "with value", i, "!=", ref[k])
				return False
		k += 1
	return True


reference = [52,21,-1,29,17,-1,87,7,73,68,49,11,-1]

anfang = [17,29,73,52,11,68,7]
ende = [87,21,49]

Nmax = 13

workingarray = [-1] * Nmax
for k in anfang:
	workingarray = insert(k, workingarray, doublehashing)
	
print("after first 7 insertions:", workingarray)

for j in itertools.permutations(ende):
	print("PERMUTATION", j)
	def tryperm(j,workingarray):
		for k in j:
			workingarray = insert(k, workingarray, doublehashing)
			if not comparesetvalues(workingarray, reference):
				return False
		return True

	print("extending", workingarray)
	assert comparesetvalues(workingarray, reference)
	if not tryperm(j,list(workingarray)): continue

	print("found solution! : ")
	print(j)
	break

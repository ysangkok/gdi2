import subprocess
import tempfile
import html
from collections import defaultdict
from collections import namedtuple

def pprinttable(rows):
    esc = lambda x: html.escape(str(x))
    sour = "<table border=1>"
    if len(rows) == 1:
        for i in range(len(rows[0]._fields)):
            sour += "<tr><th>%s<td>%s" % (esc(rows[0]._fields[i]), esc(rows[0][i]))
    else:
        sour += "<tr>" + "".join(["<th>%s" % esc(x) for x in rows[0]._fields])
        sour += "".join(["<tr>%s" % "".join(["<td>%s" % esc(y) for y in x]) for x in rows])
    with tempfile.NamedTemporaryFile(suffix=".html") as f:
        f.write(sour.encode("utf-8"))
        f.flush()
        print(
            subprocess
            .Popen(["w3m","-dump",f.name], stdout=subprocess.PIPE)
            .communicate()[0]
            .decode("utf-8")
            .strip()
        )

def dictstoobj(a):
  Row = namedtuple('Row',[x for x in sorted(list(a[0].keys()))])
  obj = [[y[1] for y in sorted(x[1].items())] for x in sorted(delta.items())]
  obj = [Row(*x) for x in obj]
  return obj

T="abcdabc"
alphabet = sorted(list(set(T)))
m = len(T)

f = lambda: defaultdict(f)
delta = defaultdict(f)

# http://wiki.algo.informatik.tu-darmstadt.de/index.php/String_matching_based_on_finite_automaton
# induction basis
for j in range(m):
	for c in alphabet:
		k = min(m-1, j+1)
		while k>0 and T[0:k] != T[j-k+1:j] + c: k = k - 1
		delta[j][c] = k

pprinttable(dictstoobj(delta))

S = "abcdabcdabcb"
n = len(S)
q = 0
R = []

# induction step
for i in range(1,n+1):
	q = delta[q][S[i-1]]
	if q == m-1: R.append(i-m+1)

print(R)

# -*- coding: utf-8 -*-
from __future__ import print_function
from colorama import Fore, Back, Style, init
import heapq
import sys
from copy import deepcopy
from collections import defaultdict
from math import log

init()

import contextlib
import sys
import cStringIO

@contextlib.contextmanager
def nostdout():
    save_stdout = sys.stdout
    sys.stdout = cStringIO.StringIO()
    yield
    sys.stdout = save_stdout


def dijkstra(G, start, end):
   def flatten(L):       # Flatten linked list of form [0,[1,[2,[]]]]
      while len(L) > 0:
         yield L[0]
         L = L[1]

   q = [(0, start, ())]  # Heap of (cost, path_head, path_rest).
   visited = set()       # Visited vertices.
   while True:
      (cost, v1, path) = heapq.heappop(q)
      if v1 not in visited:
         visited.add(v1)
         if v1 == end:
            return list(flatten(path))[::-1] + [v1]
         path = (v1, path)
         for (v2, cost2) in G[v1].items():
            if v2 not in visited:
               heapq.heappush(q, (cost + cost2, v2, path))

class highlight:
    def __init__(self,color): self.color=color
    def __enter__(self):
        print(self.color, end="")
        #print("<strong>", end="")
    def __exit__(self,type,value,traceback):
        print(Fore.RESET + Style.RESET_ALL, end="")
        #print("</strong>", end="")

def fmtnum(h, oldh=None, notab=False):
    if h > sys.maxsize/2:
        if h != sys.maxsize:
            st = "inf-{0}".format(sys.maxsize-h)
        else:
            st = "inf"
    else:
        st = h
    if notab: return st
    if oldh != None and h != oldh:
        with highlight(Style.BRIGHT + Fore.RED):
            print(st, end="\t")
    else:
        print(st, end="\t")

def printm(g,s,n,toint,fromint,oldg=None):
    l = lambda x: x

    print("\t",end="")
    for k in range(s,n):
        print(fromint(k),end="\t")
    print()
    for i in range(s,n):
        print(fromint(i),end="\t")
        for j in range(s,n):
            if oldg != None:
                fmtnum(g[l(i),l(j)], oldg[l(i),l(j)])
            else:
                fmtnum(g[l(i),l(j)])

        print()

def printb(d, oldd = None, toint = lambda x: x, fromint = lambda x: x):
    print("\t",end="")
    for k in sorted(d.keys()):
        print(k,end="\t")
    print()
    for (k,v) in sorted(d.items()):
        s = min(map(toint, d.keys()))
        n = max(map(toint, d.keys()))+1
        print(k,end="\t")
        for i in range(s,n):
            if oldd:
                fmtnum(v[fromint(i)], oldd[k][fromint(i)])
            else:
                fmtnum(v[fromint(i)])
        print()


def floyd_warshall(G,start,end,toint,fromint,s,n):
  def floyd_warshall_real(w, s, n):
    d = {s-1: w}
    nex = {}
    print()
    for k in range(s,n):
      print("after k=", k-1,":")
      try:
        a=d[k-2]
      except KeyError:
        a=None

      printm(d[k-1],s,n,toint,fromint,a)
      d[k] = {}
      for i in range(s,n):
        for j in range(s,n):
#                d[k][i,j] = min(d[k-1][i,j],
#                                d[k-1][i,k] + d[k-1][k,j])
          k0 = d[k-1][i,j]
          k1 = d[k-1][i,k] + d[k-1][k,j]
          if k0 <= k1:
#            d[k-1][i,j][1].append(j)
            d[k][i,j] = k0
          else:
            try:
              nex[i,j] = k
            except KeyError:
              pass
            d[k][i,j] = k1

    print("after k=", n-1,"(final):")
    printm(d[n-1],s,n,toint,fromint,d[n-2])
    return d[n-1], nex
  def get_path (i,j,path,nex):
    if path[i,j] == sys.maxsize:
      raise Exception("no path")
    try:
      intermediate = nex[i,j]
    except KeyError:
      return []
    return get_path(i,intermediate,path,nex) + [intermediate] + get_path(intermediate,j,path,nex)
  def human(path):
    return [fromint(x) for x in path]

  w = {}
  for i in range(s,n+1):
    w[i,i] = 0
    for j in range(s,n+1):
      w.setdefault((i,j), sys.maxsize)
  for (fro,dests) in G.items():
    for (dest,weight) in dests.items():
      w[toint(fro),toint(dest)] = weight
  res = floyd_warshall_real(w,s,n+1)
  res2 = res[0][toint(start),toint(end)]
  return res2, [start] + human(get_path(toint(start),toint(end),res[0],res[1])) + [end]

def initialize(graph):
    d = {}
    p = {}
    for node in graph:
        d[node] = {}
        p[node] = {}
        for n2 in graph:
          d[node][n2] = sys.maxsize
          p[node][n2] = None
          """ schnell initialize """
          """
          try:
            d[node][n2] = graph[node][n2]
            p[node][n2] = node
          except KeyError:
            pass
          """
        d[node][node] = 0
    return d, p

def relax(u, v, graph, d, p, fromk):
    print("       checking {0} > {1} + {2}".format(fmtnum(d[v],None,True),fmtnum(d[u],None,True),fmtnum(graph[u][v],None,True)))
    if d[v] > d[u] + graph[u][v]:
        d[v]  = d[u] + graph[u][v]
        with highlight(Style.BRIGHT + Fore.RED):
          print("       true: relaxed ({3},{1}): {2} (over {0})".format(u,v,fmtnum(d[v],None,True),fromk))
        p[v] = u
    else:
        print("       false: did not relax ({0},{1})".format(fromk,v))

import pprint
pp = pprint.PrettyPrinter(indent=4)

def bellman_ford(graph, toint, fromint):
    print("Graph:")
    pp.pprint(graph)
    
    oldd = None
    d, p = initialize(graph)
    for i in range(0,len(graph)-1):
      print("after k=", i-1)
      printb(d,oldd,toint,fromint)
      oldd = deepcopy(d)
      for start in graph:
        print("choosing {0}".format(start))
        old = d[start]
        for u in graph:
            print("  choosing {0}".format(u))
            for v in graph[u]:
                print("    choosing {0}".format(v))
                relax(u, v, graph, d[start], p[start], start)
#                if (start == 1):
#                print("start={2} v={0} w={1}".format(u,v))
    #for u in graph:
    #    for v in graph[u]:
    #        assert d[v] <= d[u] + graph[u][v]
    print("after k=", len(graph) - 2)
    printb(d, oldd, toint, fromint)
    return d, p

def repeated_squaring(graph, toint, fromint):
  n = len(graph) - 1
  fromc = min(graph.keys())

  def extend_sp(D, W):
    d = defaultdict(lambda: {})
    for i in [fromint(x) for x in range(toint(fromc), toint(fromc)+n+1)]:
      print("choosing i", i)
      for j in [fromint(x) for x in range(toint(fromc), toint(fromc)+n+1)]:
        print("   chossing j", j)
        d[i][j] = sys.maxsize
        for k in [fromint(x) for x in range(toint(fromc), toint(fromc)+n+1)]:
          print("      choosing k", k)
          print("         checking", fmtnum(d[i][j],None,True), "<" , fmtnum(D[i][k],None,True), "+", fmtnum(W[k][j],None,True))
          old = d[i][j]
          d[i][j] = min(d[i][j], D[i][k] + W[k][j])
          if old != d[i][j]:
            with highlight(Fore.RED + Style.BRIGHT):
              print("         true: relaxed ({0},{1}): {3} (over {2})".format(i,j,k,fmtnum(d[i][j],None,True)))
        #printb(d, D, ord, chr)
    return d

  newgraph = deepcopy(graph)
  for (k,v) in newgraph.items():
    v[k] = 0
  W = dict([(k,defaultdict(lambda: sys.maxsize, v.items())) for (k,v) in newgraph.items()])
  D = {1: W}
  m = 1
  while n > m:
    print("neue m:")
    D[2*m] = extend_sp(D[m], D[m])
    printb(D[2*m], D[m], toint, fromint)
    m = 2*m
  return D[m]
  """
  for m in range(2,n+1):
    D[m] = extend_sp(D[m-1], W)
  return D[n]
  """

G = {'w':{'u':10, 'x':5}, 'u':{'v':1, 'x':2}, 'v':{'y':4}, 'x':{'u':3, 'v':9, 'y':2}, 'y':{'w':7, 'v':6}}

t11={
    1:{2:25,3:6},
    2:{5:7,1:11},
    3:{4:-8,2:17},
    4:{2:24,6:-3},
    5:{3:2},
    6:{2:20,5:10}
}

feb = {
        1: {4: 1},
        2: {1: -4},
        3: {2: 1, 5: 2},
        4: {2: 100, 3: 1},
        5: {}
}

feb2 = {
        1: {},
        2: {1: 1},
        3: {2: 2},
        4: {3: 3},
        5: {4: 4}
}

#print("<div style='white-space:pre;font-family:monospace;font-size:1.5em'>")
#res1 = repeated_squaring(G, ord, chr)
res1 = repeated_squaring(t11, lambda x:x, lambda x:x)
#printb(res1, None, ord, chr)
"""
res = sorted(res1.items())
for (k,v) in res:
    print(k,sorted([(x,y) for (x,y) in v.items()]))
"""

#print(dijkstra(G,'w','v'))


#print("Floyd Warshall sxuv graph:")
#print(floyd_warshall(G,'w','v', lambda x: ord(x)+1, lambda x: chr(x-1), ord('u')+1, ord('y')+1))

#print("Floyd Warshall 1-6 graph:")
#print(floyd_warshall(t11,1,2,lambda x: x, lambda x: x, 1, 6))

#print("Bellman Ford sxuv graph:")
#print(bellman_ford(G,1337, lambda x: ord(x)+1, lambda x: chr(x-1) )[0]['w']['v'])
#print("Bellman Ford Feb graph:")
#print(bellman_ford(feb,1,lambda x: x, lambda x: x)[0][1][2])
with nostdout():
  erg = bellman_ford(t11, lambda x: x, lambda x: x)
print("Bellman-Ford:")
printb(erg[0], res1)
for i in [(k,erg[1][k]) for k in erg[1].keys()]:
  print(i[0])
  for j in [(k,i[1][k]) for k in i[1].keys()]:
    print(j)

#print("</div>")

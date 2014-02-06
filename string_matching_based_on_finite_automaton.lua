function table.print(t, ...)
	for i=0,#t do
		local r = ""
		for _,j in ipairs(alphabet) do
			r = r .. ", " .. j .. "=" .. t[i][j]
		end
		print(i .. ": " .. r)
	end
end

T = "aabbccaa"
alphabet = {"a","b","c","d"}
m = #T

delta = {}

-- http://wiki.algo.informatik.tu-darmstadt.de/wiki.algo.informatik.tu-darmstadt.de/index.php/String_matching_based_on_finite_automaton.html
-- induction basis
delta[0] = {}
for _,c in ipairs(alphabet) do delta[0][c] = T:sub(1,1) == c and 1 or 0 end
for j=1,m do
        for _,c in ipairs(alphabet) do
                local k = math.min(m, j+1)
                while k > 0 and T:sub(1,k) ~= T:sub(j-k+2,j) .. c do
                   k = k - 1
                end
                if delta[j] == nil then delta[j] = {} end
                delta[j][c] = k
        end
end

table.print(delta)

S = "ccaabbccaa"
n = #S
q = 0
R = {}

-- induction step
for i=1,n do
        q = delta[q][S:sub(i,i)]
        if q == m then table.insert(R,i-m+1) end
end

print("size: " .. #R)
for _,p in ipairs(R) do
        print(p)
	local res = S:sub(p,p+#T-1)
	if T ~= res then print("error!" .. res) end
end

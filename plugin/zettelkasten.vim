function! zettelkasten#CompleteFA(findstart, base)
	if a:findstart
python3 << EOF
vars = vim.vvars

print(vars)
EOF
		return 0
	else
python3 << EOF
result = ['Hallo Welt']
EOF
		return py3eval('result')
	endif
endfunction


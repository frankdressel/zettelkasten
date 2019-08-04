function! zettelkasten#CompleteFA(findstart, base)
	if a:findstart == 1
python3 << EOF
from zettelkasten import zettelkasten
result = zettelkasten.find_match(vim.current.buffer)
EOF
		return py3eval('result')
	else
python3 << EOF
import vim
from zettelkasten import zettelkasten
result = zettelkasten.propose_match(vim.eval('a:base'))
EOF
		return py3eval('result')
	endif
endfunction


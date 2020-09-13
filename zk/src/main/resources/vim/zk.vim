command! Zkt r !zk template | jq .
command! Zka let tn=tempname() | call writefile(getline(1, '$'), tn) | execute "!zk add \"\"\"$(cat " . tn . ")\"\"\""| call delete(tn)
command! -nargs=1 Zks new | r !zk search <args>
command! -nargs=1 Zkg new | r !zk get <args> | jq .


command! Zkt new | .!docker run --rm -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk template | jq .
command! Zka let tn=tempname() | call writefile(getline(1, '$'), tn) | execute "!docker run --rm -it -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk add \"\"\"$(cat " . tn . ")\"\"\"" | call delete(tn)
command! Zkl new | .!docker run --rm -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk list
command! Zkgl let cur=getline(".") | new | execute ".!docker run --rm -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk get \"" . cur . "\" | jq ."
command! -nargs=1 Zks new | .!docker run --rm -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk search <args>
command! -nargs=1 Zkg new | .!docker run --rm -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk get <args> | jq .
command! -nargs=1 Zkd !docker run --rm -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk delete <args>

map ;kt :Zkt<CR>
map ;ka :Zka<CR>
map ;kl :Zkl<CR>
map ;kg :Zkgl<CR>

command! BD bd!

command! Zkt .!docker run --rm -it -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk template | jq .
command! Zka let tn=tempname() | call writefile(getline(1, '$'), tn) | execute "!docker run --rm -it -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk add \"\"\"$(cat " . tn . ")\"\"\"" | call delete(tn)
command! -nargs=1 Zks new | .!docker run --rm -it -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk search <args>
command! -nargs=1 Zkg new | .!docker run --rm -it -v /home/vagrant/zk:/zk zettelkasten /zettelkasten/zk get <args> | jq .


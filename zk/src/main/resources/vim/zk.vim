if exists("g:loaded_zk")
  finish
endif
let g:loaded_zk= 0.0.1 " your version number

fun! zk#Add()
  let buff=join(getline(1, '$'), "\n")
  let cmd = "zk add '''" . buff . "'''"
  echom cmd
  let res =  system(cmd)
  if v:shell_error != 0
    echo res
  else
    echom "Entry added"
  endif
endfun

fun! zk#SearchVisual()
  " Taken from: https://stackoverflow.com/a/1534347
  try
    let a_save = @a
    normal! gv"ay
    let v = getreg('a', 1, 1)
    call zk#Search(v[0])
  finally
    let @a = a_save
  endtry
endfun

fun! zk#Search(term)
  echom a:term
  let cmd = "zk search \"" . a:term . "\""
  execute "new | read !" . cmd
endfun

fun! zk#Link(text1, text2)
  echom "Not implemented"
endfun


fun! zk#GetVisual()
  " Taken from: https://stackoverflow.com/a/1534347
  try
    let a_save = @a
    normal! gv"ay
    let v = getreg('a', 1, 1)
    let cmd = "zk get \"" . v[0] . "\""
    execute "new | read !" . cmd
    execute "%!jq ."
  finally
    let @a = a_save
  endtry
endfun

fun! zk#Template()
  let cmd = "zk template"
  execute "new | read !" . cmd
  execute "%!jq ."
endfun

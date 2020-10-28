set nocompatible              " be iMproved, required
filetype off                  " required

set t_Co=256

set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()
Plugin 'VundleVim/Vundle.vim'
Plugin 'jnurmine/Zenburn'
Plugin 'SirVer/ultisnips'
call vundle#end()            " required

silent! colors zenburn

set showmatch               " show matching brackets.
set ignorecase              " case insensitive matching
set hlsearch                " highlight search results
set tabstop=2               " number of columns occupied by a tab character
set softtabstop=2           " see multiple spaces as tabstops so <BS> does the right thing
set expandtab               " converts tabs to white space
set shiftwidth=2            " width for autoindents
set autoindent              " indent a new line the same amount as the line just typed
set number                  " add line numbers
filetype plugin indent on   " allows auto-indenting depending on file type

" Setting for ultisnips
let g:UltiSnipsExpandTrigger="<tab>"
let g:UltiSnipsJumpForwardTrigger="<c-b>"
let g:UltiSnipsJumpBackwardTrigger="<c-z>"

command! Zkt new | execute ".!/zettelkasten/zk template | jq ."
command! Zka let con=join(getline(1, '$')) | execute "%!/zettelkasten/zk add ''" . shellescape(con, 1) . "''; echo " . shellescape(con, 1) . " | jq '.'"
command! -nargs=1 Zks new | .!/zettelkasten/zk search <args>
command! -nargs=1 Zkg new | .!/zettelkasten/zk get <args> | jq .
command! Zkgl let cur=getline(".") | new | execute ".!/zettelkasten/zk get \"" . cur . "\" | jq ."
command! Zkl new | .!/zettelkasten/zk list
command! -nargs=1 Zkd !/zettelkasten/zk delete <args>

map ;kl :Zkl<CR>
map ;kg :Zkgl<CR>
map ;kt :Zkt<CR>
map ;ka :Zka<CR>

command! BD bd!
command! Q qa!

set laststatus=2
set statusline=Zettelkasten
set title
set titlestring=Zettelkasten

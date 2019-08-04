import glob
import vim
import os
import re

from fuzzywuzzy import process

matched = ''

class Registry:

    def __init__(self):
        self._hash_lookup = {}
        md_files = glob.glob('**/*.md', recursive=True)
        for file_name in md_files:
            with open(file_name, 'r') as f:
                title = re.sub('#+ +', '', re.sub('\r?\n','', f.readline()))
                key = title
                self._hash_lookup[key] = file_name


    def get(self, key):
        return self._hash_lookup.get(key, '')

    def get_keys(self):
        return self._hash_lookup.keys()

def find_bracket():
    (row, col) = vim.current.window.cursor
    print(row, col)
    joined_last_lines =  ' '.join(vim.current.buffer[-3:]) + vim.current.line[0:col]
    expression = re.findall(r'\[.*\].?$', joined_last_lines)

    if len(expression) > 0:
        return expression[-1]
    return None


def find_match(text):
    result = -1
    (row, col) = vim.current.window.cursor
    print('find:', row, col)
    expression = find_bracket()
    print('found:', expression)
    if expression:
        if vim.current.line[col - 1] == ']':
            result = col
        if vim.current.line[col - 2] == ']':
            result = col - 1
    matched = expression

    print('matched index:', result)
    return result


def propose_match(base):
    expression = find_bracket()
    print('matched text:', expression)

    result = []

    if expression:

        similar = process.extract(expression, registry.get_keys(), limit=3)
        result = ['({})'.format(registry.get(s)) for s in similar[0]]
        #result = vim.current.buffer[-5:]
        #result.append('Hallo Welt2')
        #result.append(vim.current.buffer.name)
        #result.append(os.getcwd())

    return result

registry = Registry()

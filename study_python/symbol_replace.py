#!/usr/bin/python

# this function is just used to replace '---/--', do you have better func to replace whatever between words?
def checkio(string):
    """use '-' to replace the extra symbols like '---' or '-' between words"""

    # get words to list
    list = string.replace("-", " ")
    # split words by '-'
    return "-".join(list.split())

#help(checkio)

print checkio('I---like--python') == "I-like-python"


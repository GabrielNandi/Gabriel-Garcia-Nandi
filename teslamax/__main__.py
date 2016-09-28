import teslamax

def main():
    # to be accurate when just printing the package docstring, we specify
    # an empty 'end' argument to avoid inserting a newline
    print(teslamax.__doc__,end='')

if __name__ == '__main__':
    main()

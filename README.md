Tweet Maze
==========

A very simple maze generator that renders with Unicode box characters to the
console, and are thus tweetable (if they're small enough: 12x9 max).  First, you
must compile the generator:

    javac src/Solution.java
    
Then you can run it:

    $ java -cp src Solution 20 10
    ╺━━┳━┳━━━━┳━━━━━━━┳━┓
    ╻╻╺┛╻┃╻╺━┓┗━━━┓╺┳┓┗╸┃
    ┃┗┳━┫╹┣━┓┗━┳━┓┣╸┃┗━╸┃
    ┣┓┃╻┗━┫╻┗━┓┣╸┃╹┏┛┏━━┫
    ┃┃┃┗┓╻┗┛╻┏┛┃╺┻━┛╺┻━╸┃
    ┃┃┣━┫┣━━┛┃╺┻━━━┳━━━━┫
    ┃┃╹╻┃┃╺━┳┻━━━┳╸┃╺━━┓┃
    ┃┗━┫┃┣━┓┗┓┏━━┛╻┗┓┏━┛┃
    ┃╺┳┛┃┃╻┗┓╹┃┏━━╋╸┃┃╺┓┃
    ┃╻╹╺┛┃┗┓┗╸┃╹╺┓┗━┛┗┓┗┛
    ┗┻━━━┻━┻━━┻━━┻━━━━┻━╸

The first number is the width, the second the height.  Note that both dimensions
are measured in terms of "maze squares", that is, places you could stand in the
maze.  So there are one additional wall in each direction, if you're counting.

If you'd like to generate multiple mazes of different sizes, you can list
multiple pairs of dimensions on the command line:

    $ java -cp src Solution 20 10 8 5
    ┏━┳━┳━━━┳━━━━━━━━━━┳┓
    ┃╻╹╻┃┏━┓┃╻╺┓┏━┳╸┏━┓╹┃
    ┣┻━┫┃┗╸┃┃┣╸┣┛╻┗━┛╻┣╸┃
    ┃┏┓┃┗━┓┃╹┃╻┃┏┻┳━━┫┃╺┫
    ┃┃╹┗╸┏┛┗┳┛┃┃┗╸┃┏┓┃┣┓┃
    ┃┣━━━┛┏┳┛┏┛┗━┓┃┃┃╹┃╹┃
    ┃┃╺┳━━┛┃┏┻━━┓┃┃┃┣━┫╺┫
    ╹┗╸┃╺━┓┃┃╺━┓┗┛┃┃╹╻┗┓┃
    ╻╺━┫┏━┫┃┗┳╸┣━┳┫┣━┫╺┛┃
    ┣━╸┗┛╻╹┗┓╹╺┛╻╹┃╹╻┗━━┛
    ┗━━━━┻━━┻━━━┻━┻━┻━━━╸
    ┏┳━━━┳━━┓
    ┃┃╻╺━┛┏╸┃
    ╹╹┣━━┳┛╻┃
    ╻┏┻╸╻┃╺┫┃
    ┃╹┏━┛┗┓┗┛
    ┗━┻━━━┻━╸

You can also omit any dimensions at all to use interactive mode.  Enter two
numbers, hit Enter, a maze will be drawn, and then repeat:

    $ java -cp src Solution
    5 5
    ╺━━━━┓
    ╻╺━━┓┃
    ┣━┳╸┃┃
    ┃╻╹┏┫┃
    ┃┗━┛┃╹
    ┗━━━┻╸
    6 6
    ┏━┳━━━┓
    ╹╺┛┏━┓┃
    ╻╺┳┛╻╹┃
    ┣┓┃┏┻━┫
    ┃╹┃╹┏╸┃
    ┃╺┻━┫╺┛
    ┗━━━┻━╸

Just hit Enter on a blank line to exit.  Or you can use CTRL-D or CTRL-C if
you're feeling especially agressive.

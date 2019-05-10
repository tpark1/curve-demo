# curve-demo
Implementation portion of final project for CS310 at Bryn Mawr College.

## To run
To run, you need to have java installed and the apache commons math jar. The jar can be found [here](https://commons.apache.org/proper/commons-math/download_math.cgi). Once the zip is downloaded, extract the folder and move the folder named `commons-math3-3.6.1/` to the source directory.

Once you have cloned the repo and moved the apache jar to the source directory, simply run:

`javac -cp ./commons-math3-3.6.1/commons-math3-3.6.1.jar:. *.java`  
` java -cp ./commons-math3-3.6.1/commons-math3-3.6.1.jar:. Demo`

The GUI should now pop up.

Check the `How-to-use` directory for example videos.


## Future features
* Save current points to text file.
* Load control points from a text file.
* Plot an arbitrary number of curves.
* Record user creations.
* Limit length of shortening animations.

## Current problems
* Convolution sometimes fails on vertical slope comparisons. For instance, the control point slope will
evaluate to +/-14 or something but the curve only gets up to +/-5 and so the program thinks they are not equal.
* For front tracking shortening to work, curve MUST be drawn in counterclockwise order. This is because computing the direction of the normal vector involves finding the difference of tangents and subtraction is not commutative.

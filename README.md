# curve-demo

## todo
* external file/saving current layout
* Make videos on how to use
* Write up
* test animations for curve shortening 


## notes
* convolution sometimes fails on vertical slope comparisons - ie the control point will
evaluate to +/-14 or something but the curve only gets up to +/-5
* For shortening to work, curve MUST be drawn in counterclockwise order


## To run
 javac -cp ./commons-math3-3.6.1/commons-math3-3.6.1.jar:. *.java
 java -cp ./commons-math3-3.6.1/commons-math3-3.6.1.jar:. Demo

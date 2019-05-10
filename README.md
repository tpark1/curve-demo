# curve-demo

## todo
* Animations still not smooth :(
* Change rogue point criteria
* I think points near the first ctrl point are always wrong (cfs) b/c slopes are weirder
* Editting UI function
* Put options to do both versions of curve shortening


## notes
* convolution sometimes fails on vertical slope comparisons - ie the control point will
evaluate to +/-14 or something but the curve only gets up to +/-5
* For shortening to work, curve MUST be drawn in counterclockwise order


## To run
 javac -cp ./commons-math3-3.6.1/commons-math3-3.6.1.jar:. *.java
 java -cp ./commons-math3-3.6.1/commons-math3-3.6.1.jar:. Demo

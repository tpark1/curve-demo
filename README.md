# curve-demo

## todo
* Animations still not smooth :(
* Change rogue point criteria
* I think points near the first ctrl point are always wrong (cfs) b/c slopes are weirder


## notes
* convolution sometimes fails on vertical slope comparisons - ie the control point will
evaluate to +/-14 or something but the curve only gets up to +/-5
* For shortening to work, curve MUST be drawn in counterclockwise order


shortening naivelly works
work on shortening the right way

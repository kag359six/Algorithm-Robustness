# algorithm-robustness

###Concept
Rounding errors and arithmetic errors can make implementing robust computational geometry algorithms difficult. The CCW test, for example, can give the wrong orientation when points are nearly collinear, ie. two points are close together and the third is extremely far away. 

###Interactivity
The user will be able to use a slider to move a point by an extremely small value.This will allow them to see the effects of floating point arithmetic errors on the CCW test as the change in the position of the point decreases and increases. The user will be able to zoom in and out of the affected area by adjusting the resolution of the grid (which will be explained below). Additionally, the user can set the position of the three points to see how it effects the result. 5 interesting preset examples are given to users to manipulate.

###Visual
The CCW test will be performed on 3 points A, B, and C. Point A is the only one in view along with the line formed by the three points. The visual aspect will look at point A up close to see what the result of the CCW test is along the line AB in a tiny zoomed in region. A colored grid will show the different return values of the CCW test depending on point A's position. A color value will be assigned to each possible return value, giving a nice visual representation of the odd results. The user can change the resolution which will determine the number of cells in the grid, giving the effect of zooming in and out. 

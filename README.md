# Color-Vision-Analyzer
Pseudo-Isochromatic Plates (PIPs) are used to test color vision/blindness. In a PIP, a character or number is rendered in one color set against a background of dots of other colors meant to be indistinguishable by someone who is color-blind. Someone who is not color-blind can pick off and identify the character or number rendered on a plate, whereas someone who is color-blind cannot. 

# Class Description:

## EV3 Classes:
a.	Robot.java: It handles all of the functions that the Lejos EV3 robot performs. The functions help the robot to move forward or make turns. 
I.	Sound Functions: soundON(), soundOFF(), soundSuccess(), soundFailure() produces different types of beeps varying the sounds when the pixel is on, off, success, failure.
II.	Color sensor Functions: startSensor(), recordON(), recordOFF(), readColor(), isON(), and readDistance() functions help start the color sensor, record color statistics for ON and OFF, read the color, check if the pixel is ON, and calibrate the size of the pixel. 
III.	Travel Functions: travelOne() function helps to move the robot to a 1 unit distance according to the size of the board and the pixel. Travel() helps to move the robot forward.

## Java Classes:
a.	FileAccess.java: It handles the file access of the program. It accesses the file and fills in the rules into the algorithm so that we can start the algorithm functions.
b.	Pixel.java: It holds the coordinates of the box in the grid. As the definition suggests, it represents one pixel. It holds the coordinates of the pixel and color of that pixel.
c.	RuleNode.java: It handles the string parsing to parse the string from the file to actual rules of logic. It separates the Left Hand Side with the Right Hand Side and makes it available for the program to use anytime it needs.
d.	MainAlgo.java: It handles the forward search algorithm that the user requests. 
e.	Kmeans.java: It handles the K means clustering algorithm for both color-based and distance based algorithms.
f.	Cluster.java: It holds the information of a single cluster. It represents the centroid and all of the pixels currently in that cluster.
g.	Table.java: It holds all of the pixels of the board. It can be easily called from any classes to get the pixels.


# View Classes:
a.	StartView.java: It is the introductory view of the program. It suggests the user to press enter to start scanning. At this point, the user should have already configured the location of the robot.
b.	Scan.java: It scans the whole board and then provides the user an option to choose K value. It ranges from 2 to 8.

# AI Algorithms:
a.	Forward search: This search traverses through the table and updates the potential characters according to the ON or OFF value of the pixel. It reaches an ultimate decision following through the logic.
b.	K means clustering: This algorithm clusters similar pixels to a cluster based on their color and their distance from each other. 

# Classes using AI algorithm:
a.	MainAlgo.java: This class has forward() function which performs the forward search. It also has performLejos() for initiating the Kmeans clustering algorithm.
b.	Kmeans.java: This class has the core functionalities of K means clustering algorithm.

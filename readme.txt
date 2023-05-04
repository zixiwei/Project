1.In the command line, run as administrator(different computer could have different path) 
	cd Project\src\main\java
This helps you go to the file of VideoPlayer.java
2. In the command line, you type in the following codes as administrator
	javac -cp D:\csci576\Project\src\main\java;D:\csci576\Project\VLCJ\vlcj-4.7.1\vlcj-4.7.1.jar VideoPlayer.java

	java -cp D:\csci576\Project\src\main\java;D:\csci576\Project\VLCJ\vlcj-4.7.1\vlcj-4.7.1.jar VideoPlayer D:\csci576\Project\src\Slice.mp4

Notice:(different computer could have different path)
  D:\csci576\Project\src\main\java;	is the absolute path of VideoPlayer.java 
  D:\csci576\VLCJ\vlcj-4.7.1\vlcj-4.7.1.jar; 		is the absolute path of your vlcj-4.7.1.jar(vlcj-4.7.1.jar is the jar package for playing video in the Java)
  D:\csci576\Project\src\Slice.mp4	is the absolute path of your mp4 video
  Put the mp3 video you want to play in the src directory.



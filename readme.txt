1.In the comand line, run as administrator(different computer could have different path) 
	cd D:\csci576\Project\src\main\java
Then you are in the file of VideoPlayer.java
2. In the comand line, you type in the following codes as administrator
	javac --module-path "D:\csci576\VLCJ\javafx-sdk-20.0.1\lib" --add-modules javafx.controls,javafx.media VideoPlayer.java


	java --module-path "D:\csci576\VLCJ\javafx-sdk-20.0.1\lib" --add-modules javafx.controls,javafx.media VideoPlayer   D:\csci576\Project\src\main\1

Notice:(different computer could have different path)
  JDK is at least 11 or higher version

  D:\csci576\VLCJ\javafx-sdk-20.0.1\lib;		is the absolute path of your javafx-sdk-20.0.1\lib(javafx-sdk-20.0.1\lib is the JavaFX library)
  you can download the javafx-sdk-20.0.1 at this website: https://gluonhq.com/products/javafx/

  D:\csci576\Project\src\main\;			is the absolute path of the file processed by python script




This directory contains version 0.1 of the Haiti server application. 

This particular directory is configured as a Mercurial repository (.hg)
and an Eclipse Java project (.project). It also contains an Ant build
(build.xml) file.

Features and Components

* 

To expand the jar file:

$ jar xvf haiti-server.jar

To Compile the Source:

$ ant build

This command will create the following package structure within classes:

 
To run from the command line:

$ 


To run from the jar file:

$ 

To Create a JAR (Java ARchive) file containing all the classes within
the proper package structure from with the ./classes directory:

    jar cvf <path/to/your.jar> classes

To generate the JAVADOC documentation from the sourcecode:

 javadoc -d docs -sourcepath src  





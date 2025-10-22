## COS 460/540 - Computer Networks

# Project 2: HTTP Server

# <Courtney Jackson>

This project is written in <JAVA> on <Mac16.8>.

## How to compile

To compile or assemble this project run

<pre>
javac *.java
</pre>

To generate java class files

## How to run

To execute or run this project for server side run

<pre>
java Server <<PortNumber>> <<DirectoryRoot>>
</pre>

To execute or run this project for the cleint side run

<pre>
java ServerCLient localhost <<PortNumber>>
</pre>

Then on the client side terminal you may run the GET command for example

<pre>
GET /index.html HTTP/1.0
</pre>

## My experience with this project

Overall this project was significantly harder than the previous project. I struggled a lot with this one. I found myself having to re-read the instructions several times just to try to understand what I had to do. I tried several different ways to do this project but found myself having to nearly rewrite everything from scratch from the last project. Another thing that I found hard was how hard it was to find resources on how to do the project nearly every source was trying to get me to use javas built in http server package. Finally the other hard part about this assignment was my personal constant WI-FI outages over the past three days. This made things take significantly longer.

Other than the hardships of the project I did enjoy it when things actually started to function and when I finally understood exactly what I had to do. I have done a large amount of my code experience in html frameworks, writing css, and working in localhost environments so it was cool to see how I can bring up a localhost network with a simple html page.

If I could do this project all over again I would first start by looking more into how ServerSockets, BufferReader, and OutputStream work. I believe that was something that was taught in the first COS class but I was able to skip that class due to passing the AP Computer Science exam. Some of the sources I did use to help me with this projects was the Oracle official docs and [Geeks For Geeks](https://www.geeksforgeeks.org/java/introducing-threads-socket-programming-java/)

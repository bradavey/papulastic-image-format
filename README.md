# How to use: 

Just run the JAR 💀
```
java -jar papulastic-image-format.jar
```
or compile the src yourself.

You can also use the arguments way by
```
java -jar papulastic-image-format.jar YOUR ARGUMENTS
```
## Arguments
### Encoding 
Mandatory arguments
```
encode -i InputPath -o OutputPath
```
It may include after mandatory
```
--initials AuthorInitials (max 2 characters)
--signature Signature (max 16 characters)
```
### Decoding
```
decode -i InputPath -o OutputPath
```
### Metadata
```
metadata -i InputPath
```
### Hexdump
```
hexdump -i InputPath
```
Note: You will need java 17

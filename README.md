# CkTail
This is the implementation of the CkTail method, used for preliminary tests.

## Method
CkTail infer a system of LTSs, where each LTS represents a components communicating in the system. It takes as input an action log of the sysytem, and regex file that match the actions in the log. It also extracts dependencies between the diffferent components of the system. 

### Prerequisiite
Each action of the log has to contains a timestamp, following the date format given in the regex file with the line:
**-d <dateFormat>** 

The actions has to contain an identifier of the component that has sent the message (the source, denoted "Host=<id>" in the log), and an identifier of the component that receive the message (the destination, denoted "Dest=<id>" in the log. 

### *fonctionnement

## Contents
**CkTail/src/** contains the implementation of the method. (eclipse project)

**CkTail/log** contains examples of action log and regex.

**CkTail/RESULTS** The folder that will contains the results for the cases in **CkTail/log**.

## Usage

After compilation, with <CkTail.jar> your runnable jar:
```
java -jar <CkTail.jar> -i <input> -o <output>
```
\<input\> : the folder that contains your input log.
  
Results are generated in the **\<output\>** folder.

Other options: 

-t   shows the duration of each step of the program.

## TODO
 - [ ]
 - [ ] 

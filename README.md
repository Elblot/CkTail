# CkTail
This is the implementation of the CkTail method, used for preliminary tests.

## Method
CkTail infers a system of LTSs, where each LTS represents a component communicating in the system. It takes as input an actions log of the system, and a regex file containing regular expression that match the actions in the log. It also extracts dependencies between the different components of the system. 

### Prerequisite
Each action of the log has to contains a timestamp, following the date format given in the regex file with the line:
**-d \<dateFormat\>** 

The actions has to contain an identifier of the component that has sent the message (the source, denoted "Host=\<id\>" in the log), and an identifier of the component that receive the message (the destination, denoted "Dest=\<id\>" in the log). 

The actions log is composed of *Requests* and *Responses*. Each *Request* has an associated *Response* next in the log. (Currently, *Responses* are identified in the log by containing the word "response" in the label or the parameters.) 

### Overview
The implementation works in two parts. 
The first part, Split, aims to analyse the log in order to extract the dependencies between the components, and separates the log into many traces that capture the sessions.

The second part, model generation, infers models of the different components, using the traces generated in the previous step.

## Contents
**CkTail/src/** contains the implementation of the method. (eclipse project)

**CkTail/log** contains examples of action log and regex.

**CkTail/RESULTS** The folder that will contains the results for the cases in **CkTail/log**.

## Usage
You can compile the program by running the script **build.sh**.

- If you want to run the complete method, you can run the script **CkTail.sh**:
  ```
  CkTail.sh -i <input> -r <regex> -o <output>
  ```
  **\<input\>** : your input log.

  **\<regex\>** : the file that contains the regex that cover the actions in the log.
  
  Models are generated in the **\<output\>** folder, and traces and Dependency graphs are generated in a folder **tmp**.

- If you want to run only the Split part, you can run the script **split.sh**:
  ```
  split.sh -i <input> -r <regex> -o <output>
  ```
  **\<input\>** : your input log.

  **\<regex\>** : the file that contains the regex that cover the actions in the log.
  
  traces and dependency graphs are generated in the **\<output\>** folder.

- If you want to run only the model generation part of the method on already existing traces,you can run the script   **model_generation.sh**:
  ```
  model_generation.sh -i <input> -o <output>
  ```
  **\<input\>** : the folder that contains the traces.

  models are generated in the **\<output\>** folder.

Other options: 

-t   shows the duration of each step of the program.

## TODO
 - [ ] allow the user to define how to identify *Requests* and *Responses* 
 - [ ] allow user to define where the traces and dependecy graph are written when you run nthe complete method
 

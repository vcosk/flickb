@echo off
IF DEFINED JAVA_HOME (java -jar flickb.jar) ELSE (ECHO JAVA_HOME is NOT defined)
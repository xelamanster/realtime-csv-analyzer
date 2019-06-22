# realtime-csv-analyzer

Service that receives and analyzes CSV data in real time.

Implemented in scala, using akka-streams.

The service is listening TCP port 9000.
Expects data in the following format:

    0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817

The five values are:

1. A UUID which identifies a user account
2. Some Base64 encoded data
3. A floating point value
4. An integer
5. Another integer

For every thousand messages the service creates a file with
the analysis of those 1000 messages:

1. the sum of data point 5 overall
2. the number of unique users
3. the average value of data point 3 (the floating point value) per user
4. the most recent value of data point 4 per user

Application creates folder **/metrics**,
writes results in CSV format, to the files
named with current timestamp in milliseconds:

    982374938509546879632492840347
    687
    0977dca4-9906-3171-bcec-87ec0df9d745,4.92308045,982393

The service is running until it is interrupted (e.g.
by issuing `Ctrl+C` or killing it).

### Example

Given these 10 messages and aggregating over five messages:

Input:

    0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817
    5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1005421520,6642446482729493998
    0977dca4-9906-3171-bcec-87ec0df9d745,9ZWcYIblJ7ebN5gATdzzi4e8K7Q=,0.9655429720343038,237475359,3923415930816731861
    4d968baa-fe56-3ba0-b142-be9f457c9ff4,RnJNTKLYpcUqhjOey+wEIGHC7aw=,0.6532229483547558,1403876285,4756900066502959030
    0977dca4-9906-3171-bcec-87ec0df9d745,N0fiZEPBjr3bEHn+AHnpy7I1RWo=,0.8857966322563835,1851028776,6448117095479201352
    0977dca4-9906-3171-bcec-87ec0df9d745,P/wNtfFfa8jIn0OyeiS1tFvpORc=,0.8851653165728414,1163597258,8294506528003481004
    0977dca4-9906-3171-bcec-87ec0df9d745,Aizem/PgVMKsulLGquCAsLj674U=,0.5869654624020274,1012454779,2450005343631151248
    023316ec-c4a6-3e88-a2f3-1ad398172ada,TRQb8nSQEZOA5Ccx8NntYuqDPOw=,0.3790267017026414,652953292,4677453911100967584
    023316ec-c4a6-3e88-a2f3-1ad398172ada,UfL8VetarqYZparwV4AJtyXGgFM=,0.26029423666931595,1579431460,5620969177909661735
    0977dca4-9906-3171-bcec-87ec0df9d745,uZNIcWQtwst+9mjQgPkV2rvm7QY=,0.039107542861771316,280709214,4450245425875000740

Output:

`1496040896585.txt`

    30212888860247708058
    3
    0977dca4-9906-3171-bcec-87ec0df9d745,0.6794981485066369,1851028776
    5fac6dc8-ea26-3762-8575-f279fe5e5f51,0.7626710614484215,1005421520
    4d968baa-fe56-3ba0-b142-be9f457c9ff4,0.6532229483547558,237475359

`1496040919904.txt`

    25493180386520262311
    2
    0977dca4-9906-3171-bcec-87ec0df9d745,0.50374610727888,280709214
    023316ec-c4a6-3e88-a2f3-1ad398172ada,0.3196604691859787,1579431460
### Build and run instructions
Requires Java 8 and SBT

**sbt test** - to run tests

**sbt run** - to run application. **/metrics** folder will be created at the project's root.

**sbt assembly** - to build fat jar

Command will print location of the built jar e.g:

*/path/to/project/realtime-csv-analyzer/target/scala-2.13/realtime-csv-analyzer-assembly-0.1.0-SNAPSHOT.jar*

**java -jar realtime-csv-analyzer-assembly-0.1.0-SNAPSHOT.jar** - to run application using java.
**/metrics** folder will be created at the .jar location.

### Testing. Running the producer

The producer needs Oracle Java 8.

    java -jar producer.jar --tcp
    
Runs the producer, sending data to port 9000 via TCP.

You can abort both processes with `Ctrl+C`.

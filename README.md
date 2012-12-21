# Load Tester

This load tester is designed to work with HDIV-protected websites, and is
multithreaded.

## USAGE

Use ```-h``` for a list of options:

```
usage: LoadTester
 -c,--count <arg>      test thread count
 -d,--debug            verbosely show fail pages
 -h,--help             help
 -o,--output <arg>     output stats report as file
 -r,--repeat <arg>     repeat load test (default is 1)
 -t,--testcase <arg>   test case definition
 -w,--wait <arg>       maximum start wait time override
```

To get limited output for testing, try something like:

```java -jar target/loadtester-VERSION.jar -t testcase/test.xml -c 2 2>&1 | grep LoadTest ```

## BUILDING

```mvn package``` will produce an executable jar file in the target/ directory.


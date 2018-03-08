```bash
mvn clean package
java -jar target\benchmark.jar
```

Result:

|Benchmark                               |Mode  |Cnt  |   Score |    Error  |Units|
|----------------------------------------|------|-----|---------|-----------|-----|
|SingletonPerformance.doubleCheckedTest  |avgt  |10   |1094.002 |± 180.040  |ms/op|
|SingletonPerformance.enumTest           |avgt  |10   |   4.313 |±   2.092  |ms/op|
|SingletonPerformance.holderTest         |avgt  |10   |   3.882 |±   1.973  |ms/op|




Environment:

* Intel Core i7-3630QM
* 8GB DDR3

* Windows 10
* JMH version: 1.19
* VM version: JDK 1.8.0_92, VM 25.92-b14
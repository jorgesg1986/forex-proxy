# OneForge Forex Service

Local proxy for getting Currency Exchange Rates

__Running and testing it__

In order to run the tests:

```shell
sbt test
```

In order to run the application:

```shell
sbt run
```

__Design choices__


- The number of currencies has been increased to those offered by the third-party service.
- In order to comply with the requirement an internal cache is maintained with all the rates. This cache is updated every 2 minutes and expired after 5 minutes. (Configurable values in reference.conf)

````
# VM invoker: /opt/jdk1.8.0/jre/bin/java
# VM options: -Dfile.encoding=UTF-8
# Warmup: 10 iterations, 1 s each
# Measurement: 10 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 2 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.arteam.benchmark.FalseSharingBenchmark.contended

# Run progress: 0.00% complete, ETA 00:00:40
# Fork: 1 of 1
# Warmup Iteration   1: 222.997 ops/us
# Warmup Iteration   2: 188.376 ops/us
# Warmup Iteration   3: 281.153 ops/us
# Warmup Iteration   4: 255.678 ops/us
# Warmup Iteration   5: 268.013 ops/us
# Warmup Iteration   6: 293.676 ops/us
# Warmup Iteration   7: 273.443 ops/us
# Warmup Iteration   8: 284.524 ops/us
# Warmup Iteration   9: 307.968 ops/us
# Warmup Iteration  10: 284.343 ops/us
Iteration   1: 269.598 ops/us
                 read:  64.101 ops/us
                 write: 205.498 ops/us

Iteration   2: 244.854 ops/us
                 read:  54.902 ops/us
                 write: 189.952 ops/us

Iteration   3: 292.744 ops/us
                 read:  59.886 ops/us
                 write: 232.858 ops/us

Iteration   4: 255.252 ops/us
                 read:  59.646 ops/us
                 write: 195.606 ops/us

Iteration   5: 255.327 ops/us
                 read:  67.397 ops/us
                 write: 187.929 ops/us

Iteration   6: 256.675 ops/us
                 read:  59.286 ops/us
                 write: 197.389 ops/us

Iteration   7: 215.649 ops/us
                 read:  57.600 ops/us
                 write: 158.049 ops/us

Iteration   8: 274.530 ops/us
                 read:  60.306 ops/us
                 write: 214.224 ops/us

Iteration   9: 285.434 ops/us
                 read:  63.921 ops/us
                 write: 221.514 ops/us

Iteration  10: 276.872 ops/us
                 read:  61.659 ops/us
                 write: 215.213 ops/us



Result: 262.693 ±(99.9%) 33.755 ops/us [Average]
  Statistics: (min, avg, max) = (215.649, 262.693, 292.744), stdev = 22.327
  Confidence interval (99.9%): [228.939, 296.448]

Result "read": 60.870 ±(99.9%) 5.394 ops/us [Average]
  Statistics: (min, avg, max) = (54.902, 60.870, 67.397), stdev = 3.568
  Confidence interval (99.9%): [55.476, 66.265]

Result "write": 201.823 ±(99.9%) 31.896 ops/us [Average]
  Statistics: (min, avg, max) = (158.049, 201.823, 232.858), stdev = 21.097
  Confidence interval (99.9%): [169.927, 233.719]


# VM invoker: /opt/jdk1.8.0/jre/bin/java
# VM options: -Dfile.encoding=UTF-8
# Warmup: 10 iterations, 1 s each
# Measurement: 10 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 2 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.arteam.benchmark.FalseSharingBenchmark.falseSharing

# Run progress: 50.00% complete, ETA 00:00:25
# Fork: 1 of 1
# Warmup Iteration   1: 223.295 ops/us
# Warmup Iteration   2: 179.701 ops/us
# Warmup Iteration   3: 226.166 ops/us
# Warmup Iteration   4: 247.264 ops/us
# Warmup Iteration   5: 246.381 ops/us
# Warmup Iteration   6: 209.561 ops/us
# Warmup Iteration   7: 252.671 ops/us
# Warmup Iteration   8: 206.992 ops/us
# Warmup Iteration   9: 222.415 ops/us
# Warmup Iteration  10: 235.735 ops/us
Iteration   1: 215.898 ops/us
                 read:  18.010 ops/us
                 write: 197.889 ops/us

Iteration   2: 243.616 ops/us
                 read:  18.523 ops/us
                 write: 225.093 ops/us

Iteration   3: 211.852 ops/us
                 read:  20.174 ops/us
                 write: 191.677 ops/us

Iteration   4: 247.040 ops/us
                 read:  16.134 ops/us
                 write: 230.906 ops/us

Iteration   5: 243.898 ops/us
                 read:  17.246 ops/us
                 write: 226.652 ops/us

Iteration   6: 242.351 ops/us
                 read:  21.394 ops/us
                 write: 220.958 ops/us

Iteration   7: 239.412 ops/us
                 read:  16.928 ops/us
                 write: 222.483 ops/us

Iteration   8: 238.998 ops/us
                 read:  16.798 ops/us
                 write: 222.200 ops/us

Iteration   9: 184.542 ops/us
                 read:  27.732 ops/us
                 write: 156.810 ops/us

Iteration  10: 245.158 ops/us
                 read:  18.700 ops/us
                 write: 226.459 ops/us



Result: 231.277 ±(99.9%) 31.062 ops/us [Average]
  Statistics: (min, avg, max) = (184.542, 231.277, 247.040), stdev = 20.546
  Confidence interval (99.9%): [200.214, 262.339]

Result "read": 19.164 ±(99.9%) 5.159 ops/us [Average]
  Statistics: (min, avg, max) = (16.134, 19.164, 27.732), stdev = 3.413
  Confidence interval (99.9%): [14.005, 24.323]

Result "write": 212.113 ±(99.9%) 35.294 ops/us [Average]
  Statistics: (min, avg, max) = (156.810, 212.113, 230.906), stdev = 23.345
  Confidence interval (99.9%): [176.819, 247.406]


# Run complete. Total time: 00:00:50

Benchmark                                            Mode  Samples    Score    Error   Units
c.g.a.b.FalseSharingBenchmark.contended             thrpt       10  262.693 ± 33.755  ops/us
c.g.a.b.FalseSharingBenchmark.contended:read        thrpt       10   60.870 ±  5.394  ops/us
c.g.a.b.FalseSharingBenchmark.contended:write       thrpt       10  201.823 ± 31.896  ops/us
c.g.a.b.FalseSharingBenchmark.falseSharing          thrpt       10  231.277 ± 31.062  ops/us
c.g.a.b.FalseSharingBenchmark.falseSharing:read     thrpt       10   19.164 ±  5.159  ops/us
c.g.a.b.FalseSharingBenchmark.falseSharing:write    thrpt       10  212.113 ± 35.294  ops/us
````

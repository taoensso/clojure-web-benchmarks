# Some quick & dirty Clojure web server benchmarks

## Configuration
 * Macbook Air 1.7GHz i5 with 4GB memory
 * OpenJDK 1.7, -server
 * Clojure 1.5.0-alpha3
 * ApacheBench: `ab -n 5000 -c 4`

## Results

![Performance comparison chart](https://github.com/ptaoussanis/clojure-server-benchmarks/raw/master/chart.png)

Responses are echos (static HTML in the case of nginx). [Detailed benchmark information](https://docs.google.com/spreadsheet/ccc?key=0AuSXb68FH4uhdE5kTTlocGZKSXppWG9sRzA5Y2pMVkE) is available on Google Docs. **Please excuse inaccuracies**: getting consistent results is difficult. Look for general patterns (`A` tends to be faster than `B` under this configuration) rather than specific quantitative differences.

## Clojure Web Servers on GitHub

 * [ring-clojure](https://github.com/ring-clojure/ring)
 * [ring-simpleweb-adapter](https://github.com/netmelody/ring-simpleweb-adapter)
 * [ring-netty-adapter](https://github.com/shenfeng/async-ring-adapter)
 * [http-kit](https://github.com/shenfeng/http-kit)
 * [aloha](https://github.com/ztellman/aloha)
 * [aleph](https://github.com/ztellman/aleph)

## TODO

 * Jetty 8, 9 (embedded)
 * Containers (Jetty, Tomcat, GlassFish, etc.)
 * java.nio
 * Webbit
 * Other ideas?

## Contact & Contribution

Reach me (Peter Taoussanis) at [taoensso.com](https://www.taoensso.com) for questions/comments/suggestions/whatever. I'm very open to ideas if you have any! I'm also on Twitter: [@ptaoussanis](https://twitter.com/#!/ptaoussanis).
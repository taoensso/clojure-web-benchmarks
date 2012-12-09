# Some quick & dirty Clojure web server benchmarks

## Configuration
 * Macbook Air 1.7GHz i5 with 4GB memory
 * OpenJDK 1.7, -server
 * Clojure 1.5.0-alpha3
 * ApacheBench: `ab -n 5000 -c 4`

## Results

![Performance comparison chart](https://github.com/ptaoussanis/clojure-server-benchmarks/raw/master/chart.png)

Responses are echos (static HTML in the case of nginx). [Detailed benchmark information](https://docs.google.com/spreadsheet/ccc?key=0AuSXb68FH4uhdE5kTTlocGZKSXppWG9sRzA5Y2pMVkE) is available on Google Docs.

## Clojure Web Servers on GitHub

 * [ring-clojure](https://github.com/ring-clojure/ring)
 * [http-kit](https://github.com/shenfeng/http-kit)
 * [aloha](https://github.com/ztellman/aloha)
 * [ring-simpleweb-adapter](https://github.com/netmelody/ring-simpleweb-adapter)
 * [aleph](https://github.com/ztellman/aleph)

## TODO

 * Jetty 8, 9 (embedded)
 * Jetty 7, 8, 9 (container)
 * java.nio
 * Webbit
 * Other ideas?

## Contact & Contribution

Reach me (Peter Taoussanis) at [taoensso.com](https://www.taoensso.com) for questions/comments/suggestions/whatever. I'm very open to ideas if you have any! I'm also on Twitter: [@ptaoussanis](https://twitter.com/#!/ptaoussanis).
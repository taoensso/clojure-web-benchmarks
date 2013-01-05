# Clojure web server shoot-out

## Results

**Detailed benchmark results** available in `/results/`. Chart available on [Google Docs](http://goo.gl/QRTCH).

![Performance comparison chart](https://github.com/ptaoussanis/clojure-web-server-benchmarks/raw/master/results/20130105-17-26.png)

## Servers
  * [Jetty Ring adapter](https://github.com/ring-clojure/ring) - Standard Ring adapter.
  * [SimpleWeb Ring adapter](https://github.com/netmelody/ring-simpleweb-adapter) - Pure-Java HTTP server without using Servlets.
  * [Netty Ring adapter](https://github.com/shenfeng/async-ring-adapter) - Netty adapter for use with Ring.
  * [http-kit](https://github.com/shenfeng/http-kit) - HTTP client/server with async & WebSockets support.
  * [Aleph](https://github.com/ztellman/aleph) - Clojure framework for asynchronous communication, built on top of Netty and Lamina.
  * [Aloha](https://github.com/ztellman/aloha) - Reference implementation of a Clojure/Netty webserver, or basically Aleph without any extraneous fluff.
  * Jetty 7, Jetty 8, Tomcat 7 servlets via [lein-servlet](https://github.com/kumarshantanu/lein-servlet).
  * [nginx 1.2.6](http://nginx.org).
  * **TODO**: Containers (Jetty, Tomcat, GlassFish), Webbit. More ideas welcome!
  * **TODO**: SimpleWeb and Netty Ring adapters DNF.

## Configuration
  * Macbook Air 1.7GHz Intel Core i5 with 4GB 1333MHz DDR3, running Mac OS X 10.7.5.
  * Clojure 1.5.0-RC1 on Oracle JDK7 build 1.7.0_04-b21.
  * Response length: 1163 bytes (`servers/index.html`).
  * ApacheBench Version 2.3 Revision: 1373084. (NB requires [upgrade](https://gist.github.com/1724673) on OS X).
  * Leiningen `trampoline`, `:jvm-opts ["-server" "-XX:+UseConcMarkSweepGC"]`.
  * See `scripts/bench.sh` for full details, including OS and TCP tuning.
  * See `servers/nginx.conf` for nginx config. **TODO**: Improve. Suggestions?
  * **Low concurrency**: `ab -n 100000 -c 16 -k`.
  * **High concurrency**: `ab -n 100000 -c 64 -k`.

## Contact & contribution

### Welcoming pull-requests for:
  * More servers.
  * Updates servers.
  * Server config tuning!

Reach me (Peter Taoussanis) at [taoensso.com](https://www.taoensso.com) for questions/comments/suggestions/whatever. I'm very open to ideas if you have any! I'm also on Twitter: [@ptaoussanis](https://twitter.com/#!/ptaoussanis).

# Clojure web server benchmarks

## Results

![Performance comparison chart](https://github.com/ptaoussanis/clojure-web-server-benchmarks/raw/master/results/20121213-04-03-official.png)

Responses are echos (static HTML in the case of reference nginx server). [Detailed benchmark information](http://goo.gl/bgyVI) is available on Google Docs.

These results are only useful in so far as they're complete and accurate. Please feel free to get in touch if you've spotted any errors, or have any other ideas for how to improve the information here. **Pull requests welcome**!

### Configuration
  * Amazon EC2 M1 Large 64-bit instance running Ubuntu Server 12.04.1 LTS.
  * OpenJDK 1.7, -server, -XX:+UseConcMarkSweepGC.
  * Clojure 1.5.0-alpha3.

## Running benchmarks yourself
  1. Ensure [httperf](http://www.hpl.hp.com/research/linux/httperf/) and [Autobench](http://www.xenoclast.org/autobench/) are installed.
  2. Clone this repo.
  2. Run `scripts/run-servers.sh` then `scripts/run-benchmarks.sh`.

NOTE: I had serious trouble getting Mac OS X to cooperate reliably with _any_ benching tools (incl. httperf, ab, and siege), even after tuning for ephemeral ports and open file limits.

## Clojure web servers on GitHub
  * [ring-clojure](https://github.com/ring-clojure/ring)
  * [ring-simpleweb-adapter](https://github.com/netmelody/ring-simpleweb-adapter)
  * [ring-netty-adapter](https://github.com/shenfeng/async-ring-adapter)
  * [http-kit](https://github.com/shenfeng/http-kit)
  * [aloha](https://github.com/ztellman/aloha)
  * [aleph](https://github.com/ztellman/aleph)

## TODO
  * Merge kumarshantanu servlets PR (Jetty 7, Jetty 8, Tomcat 7)
  * Bench containers (Jetty, Tomcat, GlassFish, etc.)
  * Webbit
  * Other ideas?

## Contact & contribution

Reach me (Peter Taoussanis) at [taoensso.com](https://www.taoensso.com) for questions/comments/suggestions/whatever. I'm very open to ideas if you have any! I'm also on Twitter: [@ptaoussanis](https://twitter.com/#!/ptaoussanis).

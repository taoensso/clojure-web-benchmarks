# Clojure web server shoot-out

This is a **collaborative repo**. Please see [here](#contact--contribution) for info on contributors & contributing.

## 1. Results


## Latest Results / 2014-01-21


  * **Clojure Google Group discussion**: http://goo.gl/xe46R.
  * **Detailed benchmark results** available in `/results/`.
  * **Margin of error**: +/- ~10%.
  
### 1.1 On Ubuntu 13.04 + i7-4700MQ

  * 2.4GHz Intel Core i7-4700MQ with 16GB 1333MHz DDR3
  * Clojure 1.5.1 on Oracle JDK7 build  1.7.0_45
  * Chart available on [Google Drive](http://goo.gl/XrHk7C)
  
  
  ![Performance comparison chart](results/20140121-14-30.png)


  


### 1.2 On Fedora 19 + i7-3520M

  * 2.9GHz Intel Core i7-3520M with 16GB 1600MHz DDR3
  * Clojure 1.5.1 on OpenJDK 1.7.0-51
  * Chart available on [Google Drive](http://goo.gl/2FtAFy).

![Performance comparison chart](results/20140120-22-49.png)

  


## 2. Pending changes

  * **Margin of error**: +/- ~10%. (This may be outdated?).


## 3. Update History

### 2014-01-20

  * add immutant testing (latest official release 1.0.2)
  * add undertow testing

### 2014-01-14

  * org.clojure/clojure 1.4.0 --> 1.5.1
  * compojure    1.1.4        --> 1.1.6
  * ring         1.1.6        --> 1.2.1
  * aleph        0.3.0-beta13 --> 0.3.0
  * http-kit     1.3.0-alpha2 --> 2.1.16
  * ring-netty-adapter 0.0.3 -->  netty-ring-adapter 0.4.6
  * remove testing about pure nginx which generally dosen't service dymanic contents without other modules. 
  * add nginx with php5-fpm 5.5 testing
  * add nginx-clojure 0.1.0 testing
  * add 128 clients testing



## 4. Configuration (as of latest results)
  * Response length: 1163 bytes (`servers/index.html`).
  * ApacheBench Version 2.3 Revision: 1430300.
  * ApacheBench `ab -n 300000 -c <16,64,92,128> -rk`.
  * Leiningen `trampoline`, `:jvm-opts ["-server" "-XX:+UseConcMarkSweepGC"]`.
  * See `scripts/tune_linux.sh` , `scripts/tune_macosx.sh`  for details about OS tuning. Please run the related tune_xxx.sh before starting servers or ab.
  * See `scripts/bench.sh` for full details.
  * See `servers/nginx-php/conf/nginx.conf` for nginx php config. **TODO**: Improve. Suggestions?

## 5.Servers
  * [Jetty Ring adapter](https://github.com/ring-clojure/ring) - Standard Ring adapter.
  * [SimpleWeb Ring adapter](https://github.com/netmelody/ring-simpleweb-adapter) - Pure-Java HTTP server without using Servlets.
  * [Netty Ring adapter](https://github.com/shenfeng/async-ring-adapter) - Netty adapter for use with Ring.
  * [http-kit](https://github.com/shenfeng/http-kit) - HTTP client/server with async & WebSockets support.
  * [Aleph](https://github.com/ztellman/aleph) - Clojure framework for asynchronous communication, built on top of Netty and Lamina.
  * [Aloha](https://github.com/ztellman/aloha) - Reference implementation of a Clojure/Netty webserver, or basically Aleph without any extraneous fluff.
  * Jetty 7, Jetty 8, Tomcat 7 servlets via [lein-servlet](https://github.com/kumarshantanu/lein-servlet).
  * [nginx 1.4.4](http://nginx.org) + [php5-fpm 5.5.3+dfsg-1ubuntu2.1](http://php-fpm.org/)
  * [nginx-clojure 0.1.0](https://github.com/xfeep/nginx-clojure) compiled into [nginx 1.4.4](http://nginx.org)  .
  * [immutant 1.0.2](http://immutant.org/)
  * [undertow Ring adapter 0.1.2](https://github.com/piranha/ring-undertow-adapter)

## 6. Contact & contribution

This is a **collaborative repo** maintained by [a number of contributors](https://github.com/ptaoussanis/clojure-web-server-benchmarks/graphs/contributors). Thanks to everyone for making this possible!

### Welcoming pull requests for:
  * Additional web servers.
  * Updated servers (no snapshot releases please).
  * Server / bench config tuning!
  * Migrating tests from AB to [wrk](https://github.com/wg/wrk), [weighttp](https://github.com/lighttpd/weighttp), or similar.
  * Higher concurrency tests (c=256, c=500, c=1000, etc.).

In all cases, **please try to include updated results & graphs** when possible. We're looking primarily at relative numbers here so it's not a big deal if the hardware changes between PRs, so long as it's documented and mentioned in any graphs.

You can reach me (Peter Taoussanis) at [taoensso.com](https://www.taoensso.com), or the other contributors through the [issues page](https://github.com/ptaoussanis/clojure-web-server-benchmarks/issues?state=open).

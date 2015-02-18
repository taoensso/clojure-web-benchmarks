# Clojure web server benchmarks

This is a **collaborative repo** maintained with the participation of [a number of contributors](https://github.com/ptaoussanis/clojure-web-server-benchmarks/graphs/contributors). Thanks to everyone for making this possible!

## Latest benchmark configuration
  * Benchmarking with [wrk2](https://github.com/giltene/wrk2).
  * Server response length: 1163 bytes (`servers/index.html`).

The particular **server hardware**, **wrk2 config**, and **server versions** will vary between each result set.

## Latest results

> There is no such thing as a one-size-fits-all benchmark. Results will vary **dramatically** based on your server hardware and the kind of workload you're testing.

Results are organised into **named benchmarking profiles** that determine **wrk2 config**:

Profile             | Description
------------------- | -----------
[1k-keepalive]      | 32→1024 conns (keepalive)
[1k-non-keepalive]  | 32→1024 conns (non-keepalive)
[60k-keepalive]     | 10k→60k conns (keep-alive)
[60k-non-keepalive] | 10k→60k conns (non-keepalive)
[Legacy results]    | Everything before benchmarking profiles were introduced.

## Running benchmarks yourself

Clone this repo locally, then run `start-here.sh` in the repo root.

## Server links (alphabetically)

  * [Aleph]
  * [http-kit]
  * [Immutant v1]
  * [Immutant v2 "thedeuce"]
  * [Jetty Ring adapter]
  * Jetty 7/8/9, Tomcat 7/8 servlets via [lein-servlet]
  * [nginx-clojure] compiled into [nginx]
  * [Undertow Ring adapter]

## Contact & contribution

### Welcoming pull requests for:

  * Additional web servers.
  * Updated servers (no snapshot releases please).
  * Server / bench config tuning!

### If you are submitting a pull request to update results

  1. Please target your PR towards a **branch with your GitHub user name**.
  2. Please try to **include graph/s** when possible. We're looking mostly for _relative_ numbers here so it's not a big deal if the hardware changes between PRs, so long as it's documented and mentioned in any graphs.
  3. Please try ensure that **all** servers being benchmarked have a **reasonable configuration for your hardware environment**. This is especially important if your hardware environment is unusual (e.g. fewer/more cores than usual). If you're unclear on how to adjust some server's configuration for your environment, feel free to open an issue to check that the config you're proposing seems solid to interested maintainers.

If you have any questions, you can reach me at [taoensso.com](https://www.taoensso.com), or the other contributors through the [issues page](https://github.com/ptaoussanis/clojure-web-server-benchmarks/issues?state=open).

/ Peter Taoussanis ([@ptaoussanis])

[@ptaoussanis]: https://github.com/ptaoussanis

[1k-keepalive]: https://github.com/ptaoussanis/clojure-web-server-benchmarks/tree/master/results/1k-keepalive
[1k-non-keepalive]: https://github.com/ptaoussanis/clojure-web-server-benchmarks/tree/master/results/1k-non-keepalive
[60k-keepalive]: https://github.com/ptaoussanis/clojure-web-server-benchmarks/tree/master/results/60k-keepalive
[60k-non-keepalive]: https://github.com/ptaoussanis/clojure-web-server-benchmarks/tree/master/results/60k-non-keepalive
[Legacy results]: https://github.com/ptaoussanis/clojure-web-server-benchmarks/tree/master/results/legacy

[Aleph]: https://github.com/ztellman/aleph
[http-kit]: https://github.com/shenfeng/http-kit
[Immutant v1]: http://immutant.org/
[Immutant v2 "thedeuce"]: https://github.com/immutant/immutant
[Jetty Ring adapter]: https://github.com/ring-clojure/ring
[lein-servlet]: https://github.com/kumarshantanu/lein-servlet
[nginx-clojure]: https://github.com/xfeep/nginx-clojure
[nginx]: http://nginx.org
[Undertow Ring adapter]: https://github.com/piranha/ring-undertow-adapter
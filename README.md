<a href="https://www.taoensso.com/clojure" title="More stuff by @ptaoussanis at www.taoensso.com"><img src="https://www.taoensso.com/open-source.png" alt="Taoensso open source" width="420"/></a>  

# Clojure web (server) benchmarks

This is a **collaborative repo** maintained by a [a number of contributors](https://github.com/ptaoussanis/clojure-web-server-benchmarks/graphs/contributors). Thanks to everyone for making this possible! \- [Peter Taoussanis](https://www.taoensso.com)

## Results

Last updated: **TODO**

There is no such thing as a one-size-fits-all benchmark. Results will vary **dramatically** based on your server hardware and the kind of workload you're testing.

So results are organised into **named benchmarking profiles** that determine **wrk config**:

Profile | Connections | Keep alive?
:-- | :-: | :-:
[1k-keepalive](../../tree/master/results/1k-keepalive#1k-keepalive)      | 32→1024 | ✓
[1k-non-keepalive](../../tree/master/results/1k-non-keepalive#1k-non-keepalive)  | 32→1024 | -
[60k-keepalive](../../tree/master/results/60k-keepalive#60k-keepalive)     | 10k→60k | ✓
[60k-non-keepalive](../../tree/master/results/60k-non-keepalive#60k-non-keepalive) | 10k→60k | -

Or see [here](../../tree/master/results/legacy#legacy-results) for older results from before benchmarking profiles were introduced.

## Running benchmarks yourself

Getting started is really easy!

1. Clone this repo locally
2. Run `start-here.sh` in the repo root

## Benchmark info

- Benchmarking with [wrk](https://github.com/wg/wrk)
- Server [response](../../blob/master/blob/master/servers/index.html) length: 1163 bytes

The particular **server hardware**, **wrk2 config**, and **server versions** will vary between each result set.

## Servers tested

Alphabetically:

- [Aleph](https://github.com/ztellman/aleph)
- [http-kit](https://github.com/shenfeng/http-kit)
- [Immutant v2](https://github.com/immutant/immutant)
- [Jetty Ring adapter](https://github.com/ring-clojure/ring)
- Jetty 7/8/9, Tomcat 7/8 servlets via [lein-servlet](https://github.com/kumarshantanu/lein-servlet)
- [nginx-clojure](https://github.com/xfeep/nginx-clojure) compiled into [nginx](https://nginx.org/)
- [Undertow Ring adapter](https://github.com/piranha/ring-undertow-adapter)

## Contributing

GitHub pull requests **very welcome** for:

- Additional web servers
- Updated servers (no snapshot releases please!)
- Server / bench config tuning!

### If you are submitting a pull request to update results

1. Please target your PR towards a **branch with your GitHub user name**.
2. Please try to **include graph/s** when possible. We're looking mostly for _relative_ numbers here so it's not a big deal if the hardware changes between PRs, so long as it's documented and mentioned in any graphs.
3. Please try ensure that **all** servers being benchmarked have a **reasonable configuration for your hardware environment**. This is especially important if your hardware environment is unusual (e.g. fewer/more cores than usual). If you're unclear on how to adjust some server's configuration for your environment, feel free to open an issue to check that the config you're proposing seems solid to interested maintainers.

If you have any questions, please open an issue on GitHub.

Cheers!

\- [Peter Taoussanis](https:/www.taoensso.com)

(ns nginx-clojure
  (:require
   [clojure.string   :as str]
   [clojure.java.io :as io])
  (:use [clojure.java.shell :only [sh]]))



;;;; Handler

(def nginx-clojure-ver "0.3.0")

(def max-balanced-tcp-connections "65000")

(def properties (into {} (System/getProperties)))

(def arc (let [c (properties "os.arch")]
                      (if (.contains c "64")
                        "x64"
                        "i586")))

(def os (let [os (str/lower-case (properties "os.name"))]
                      (if (.contains os "linux")
                        "linux"
                        "macosx")))

(def nginx-exe (if (= os "macosx") "nginx-macosx"
                                       (str "nginx-linux-" arc)))

(def response
  {:status  200
   :headers {"content-type" "text/html"}
   :body    (slurp "../index.html")})

(defn handler [request] response)

(def tpl-file (io/file
                (io/resource 
                  "nginx.conf.tpl" )))

(defn osh [& args]
  (let [rs (apply sh args)]
      (println (:out rs))
      (println (:err rs))
      rs))

(defn  check-download 
  []
  (let [gz (str "nginx-clojure-"  nginx-clojure-ver  ".tar.gz")]
     (when (not  (.exists (io/as-file nginx-exe)))
          (when (not  (.exists (io/as-file (str "download/"  gz))))
            (osh "sh"  "-c" "mkdir download" )
             (println "downloading nginx-clojure ver:"   nginx-clojure-ver)
             (osh  "wget" (str "http://sourceforge.net/projects/nginx-clojure/files/"  gz  "/download")
                   "-O" (str "download/" gz ".tmp"))
             (osh "sh" "-c" (str "mv "  " download/" gz ".tmp"  " download/" gz )))
            (osh "sh"  "-c"  (str "tar -xzvf  download/" gz))
            (let [nginx-dir (-> (osh "sh"  "-c"  (str "ls " "nginx-clojure-"  nginx-clojure-ver)) (:out) (.trim))]
              (println "nginx-dir" nginx-dir)
              (osh "sh"  "-c"  (str "mv "  "nginx-clojure-"  nginx-clojure-ver "/" nginx-dir "/* ./")))
            (osh "sh" "-c"  (str "chmod +x  " nginx-exe)))
      (osh "sh" "-c" (str "rm -rf nginx && ln -s " nginx-exe " nginx"))))

(defn gen-nginx-conf-by-tpl
  []
  (let [pid (-> (java.lang.management.ManagementFactory/getRuntimeMXBean) (.getName) (.split "@") (first))
             libjvm (-> (:out (osh "sh" "-c" (str "lsof  -p " pid "  | grep libjvm."))) (.split "\\s+") (last) (.trim) )
             clojure-rt (-> (:out (sh "sh" "-c" (str "lsof  -p  " pid "  | grep clojure-.*\\.jar"))) (.split "\\s+") (last) (.trim) )
             tpl (slurp tpl-file)]
    (println  "libjvm" libjvm)
    (println "nginx-exe" nginx-exe)
    (println "clojure rt" clojure-rt)
    (spit "conf/nginx.conf" 
          (-> tpl (str/replace "#{max_balanced_tcp_connections}" max-balanced-tcp-connections )
                        (str/replace "#{jvm_shared_library_path}" libjvm )
                        (str/replace "#{class-path}" (str clojure-rt 
                                                          java.io.File/pathSeparator   "jars/nginx-clojure-" nginx-clojure-ver ".jar" 
                                                          java.io.File/pathSeparator  "src"))))))

(defn -main [& args]
  ;;;check 
  ;(println (slurp tpl-file))
  (check-download)
  (gen-nginx-conf-by-tpl)
  (clojure.lang.Agent/shutdown)
  )

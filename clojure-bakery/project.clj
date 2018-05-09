(defproject clojure-bakery "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.4.474"]]
  :jvm-opts ["-server" "-Dclojure.core.async.pool-size=40"]
  :main ^:skip-aot clojure-bakery.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

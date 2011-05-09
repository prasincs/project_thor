(defproject thor "0.2-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [incanter "1.2.3"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]]
  :dev-dependencies [[com.fxtlabs/autodoc "0.8.0-SNAPSHOT"]
                     [swank-clojure "1.2.1"]
                     ]
  :autodoc {:name "Project Thor" 
            :page-title "API documentation"
            :web-home "http://prasincs.github.com/project_thor/docs"
            :output-path "docs"
            }
  ; sets the 'java.li:brary.path' property to load ZeroMQ library
  :native-path "/usr/local/lib"
  :main thor.main)

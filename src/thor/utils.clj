(ns thor.utils
 (:use [clojure.contrib.logging])

  )
(use '[clojure.contrib.string :only (join split )])
(defmacro extract-double [textbox]
  `(Double/parseDouble 
                        (.getText ~textbox)))

(defmacro extract-integer [textbox]
  `(Integer/parseInt
     (.getText ~textbox)))

(defmacro onclick [button & body]
  `(doto ~button 
  (.addActionListener (proxy [java.awt.event.ActionListener] []
                       (actionPerformed [evt#]
                        ~@body
                         )))))

(defn write-lines2 [file-name lines]
  (with-open [wtr (java.io.BufferedWriter. (java.io.FileWriter. file-name))]
    (doseq [line lines] (.write	wtr line))))

(defn keys-from-fields [fields]
  (read-string (str "(" (join " " (map #(str ":" %) fields)) ")")))

; probably should make it lazier
(defmacro in-seq? "check if an item is in the sequence" [item & sq]
`(= (some #{~item} ~@sq) ~item) )

; taken from http://www.paullegato.com/blog/setting-clojure-log-level/
(defn set-log-level! [level]
  "Sets the root logger's level, and the level of all of its Handlers, to level."
  (println (.. (impl-get-log "user") getLogger ))
  (let [logger (.getLogger (impl-get-log ""))]
    (.setLevel logger level)
    (doseq [handler (.getHandlers logger)]
      (. handler setLevel level))))

(defn convert-unit [s mult-func]
  (let [v (split #"\s" s)]
    (try 
      (* (Double/parseDouble (first v )) 
         (mult-func (nth v 1)))
      (catch Exception e 
        (prn e "ewwww... I hit an error while converting"))
      )

    ))

(defn convert-to-Hz [s]
  (defn multiplier [s]
    (cond 
      (= s "GHz") (* 1000 1000 1000)
      (= s "MHz") (* 1000 1000)
      (= s "KHz") (* 1000)
      )
    )
  (convert-unit s multiplier)
  )

(defn convert-to-bps [s]
  (defn multiplier [s]
    (cond 
      (= s "Gbps") (* 1024 1024 1024)
      (= s "Mbps") (* 1024 1024)
      (= s "Kbps") (* 1024 )
      )
    )
  (convert-unit s multiplier)
  )





(ns #^{:doc "A description parser for thor AKA Hammer"}
  thor.lang
  (:use clojure.contrib.logging thor.utils)
  (:require thor.queue thor.node)
  )

(use '[clojure.contrib.math :only (expt) ])
(use '[clojure.contrib.string :only (split)])
(def *devices* (atom {}))
(def *total-samples* (atom 100)) ; define total-samples with a default value
(def *total-devices* (atom 100)) ; define total-devices with a default
(def *experiment-attrs* (atom {}))
(def *duration* (atom 0))
(def *current-time* (atom 1))
(def *keyspace* (atom (expt 2 10)))



; medium types  anything but 0
(def wired 2)   ; magic number alert! ;)
(def wireless 3)
(def *network-type* (atom 0))

(def *wireless-network*
  (atom {:bandwidth 10000 ; wonky magic numbers
         :frequency 10000 
         :channel 6
         :propagation-loss-expt 2
         :loss '[(propagation-loss)]
         :noise 0
         })
  )
(def *wired-network*  (atom {}))

(defn get-duration [] @*duration*)

(defn get-network-attrs []
  (if (= @*network-type* wireless )
    @*wireless-network*
    ))

(defn set-dict-attr [ d k v]
  (swap! d assoc k v)
  )

(defn init-wireless-network 
  "Apply the attributes from the map"
  [attrs]
  (defn set-wireless-attr [attr &[func]]
    (let [v ( if (nil? func) 
              (attrs attr) 
              (func (attrs attr))) ]
      (set-dict-attr *wireless-network* attr v)
      )
    )
  (set-wireless-attr :frequency convert-to-Hz)
  (set-wireless-attr :bandwidth convert-to-bps)
  (set-wireless-attr :channel)
  (set-wireless-attr :propagation-loss-expt)
  ;(set-wireless-attr :loss do-something-to-loss)
  (set-wireless-attr :noise)
  )

(defn init-wired-network [attrs]
  (println "maybe you want to implement this!")
  )

(defn init-medium [t attrs]
  (if (= t wireless)
    (do 
      (reset! *network-type* wireless)
      (init-wireless-network attrs)
      )
    (if (= t wired)
      (do
      (reset! *network-type* wired)
        (init-wired-network attrs))
      )
    )
  )

(defmacro defmedium   
  "Defines the properties of the medium of communication"
  [t attrs]
  `(init-medium ~t ~attrs)
  )



(defn get-current-time [] @*current-time*)


(defn defkeyspace [value]
  `(reset! *keyspace* ~value))

(defn get-keyspace [] @*keyspace*)

(defn get-total-devices [] @*total-devices*)
(defn get-devices [] @*devices*)

(defn add-device [n d]
  (reset! *devices* (conj @*devices* [n d])))

(defmacro defdevice 
  "Defines a device with name and a map of attributes"
  [n attrs]
  `(add-device '~n ~attrs ))

(defmacro defsamples [n]
  `(reset! *total-samples* ~n))

(defn get-total-samples [] @*total-samples*)

(defmacro defduration [n]
  `(reset! *duration* ~n))

(defn create-experiment 
  "Create an experiment based on attributes"
  [n attrs]
  (reset! *experiment-attrs* attrs)
  )

(defn get-experiment-attrs [] @*experiment-attrs*)

(defn create-at-event [t f]
  ; add function f at time t
  (println (str "adding event at time " t))
  (thor.queue/add-events-to-queue (thor.queue/create-event t f))
  )

(defn create-every-event  [t f]
  (loop [c (get-current-time)]
    (if (< c @*duration*)
      (do 
        (create-at-event c f)
        (recur (+ c t))
        ))
    ))


(defmacro every [t f]
  `(create-every-event ~t '~f)
  )

(defmacro at [t f]
  `(create-at-event ~t '~f)
  )

(defmacro at-start
  [f]
  `(at 0 ~f)
  )

(defmacro at-end
  "A special at function that just runs at the end"
  [f]
  `(at @*duration* ~f)
  )

(defn on-function [attrs]
  attrs
  )

(defmacro on-new-neighbor [attrs]
  `(on-function ~attrs)
  )



(defmacro defexpt 
  "Defines an experiment"
  [n attrs]
  `(create-experiment '~n ~attrs)
  )


(defmacro defdata [n size]
  (cond
    (= size 'B) `(* ~n 1)
    (= size 'K) `(* ~n 1024)
    (= size 'M) `(* ~n 1024 1024)
    (= size 'G) `(* ~n 1024 1024 1024)
    )
  )

(defn load-experiment
  "Load experiment data from file f"
  [f]
  (let [oldns (ns-name *ns*)]

    (in-ns  'thor.lang)
    (binding [*experiment-attrs* (atom {})]
      (load-file f)
      (in-ns oldns)
      @*experiment-attrs*
      ))
  )

(defn load-device 
  "Loads a device from file f."
  [f]
  (let [oldns (ns-name *ns*)]
    (in-ns  'thor.lang)
    (binding [*devices* (atom {})]
      (load-file f)
      (in-ns oldns)
      ;just return all the devices found
      ;this could be then concat into another list 
      @*devices*
      )))


(defn simulation-init [&[args]]
  ; args left there for future -- perhaps key value pairs
  (debug "Simulation INIT")
  )


(defn simulation-run[&[args]]
  ; args left there for future -- perhaps key value pairs
  (loop [current-event (thor.queue/next-event)]
    ; take things from queue and run
    (reset! *current-time* (:time current-event))
    (eval (:task current-event))
    (if (and (thor.queue/has-events?)
             (<= @*current-time* @*duration*)) 
      (recur (thor.queue/next-event))))

  )

(defn display-results [&[args]]
  ; todo -> implement
  )

(defn get-device [n]
  (if (contains? @*devices* n)
    (get @*devices* n)
    (throw (Error. "No such device in database"))
  ))

(defn create-node [attrs]

  (if (contains? attrs :device)
    
    (let [attrs (assoc attrs :device-attrs (get-device (attrs :device)))]
      (println attrs)
  (atom (thor.node/create-node attrs)))
  ))

(defn move-node [n op pos]
  (reset! n (thor.node/node-move @n op pos)))

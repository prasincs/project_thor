(ns #^{:doc "A description parser for thor AKA Hammer"}
  thor.lang
  (:require thor.queue)
  )

(def *devices* (atom {}))
(def *total-samples* (atom 100)) ; define total-samples with a default value
(def *total-devices* (atom 100)) ; define total-devices with a default
(def *experiment* (atom {}))
(def *duration* (atom 0))
(def *current-time* (atom 0))

; medium types 
(def wired 1)   ; magic number alert! ;)
(def wireless 2)



(defn get-current-time [] @*current-time*)

(defn add-device [n d]
  (reset! *devices* (conj @*devices* [n d])))

(defmacro defdevice 
  "Defines a device with name and a map of attributes"
  [n attrs]
  `(add-device '~n ~attrs ))

(defmacro defsamples [n]
  `(reset! *total-samples*~ n))


(defmacro defduration [n]
  `(reset! *duration* ~n))

(defn create-experiment 
  "Create an experiment based on attributes"
  [n attrs]
  (reset! *experiment* attrs)
  )

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
    (binding [*experiment* (atom {})]
    (load-file f)
    (in-ns oldns)
    @*experiment*
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



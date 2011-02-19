(ns thor.network
  (:require clojure.core thor.node)
  (:use thor.utils)
;  (:import [java.lang.Math])
  )

(defn create-neighbors-map [] (atom {}))

(defn squared [x] (* x x))

(defn find-distance [n1 n2 ] (Math/sqrt
			      (+
			      (squared ( - (-> n2 :location :x) (-> n1 :location :x)))
			      (squared ( - (-> n2 :location :y) (-> n1 :location :y))))))

(defn find-neighbors [node node-list]
  )


(defn add-neighbor [neighbors-map node-id new-node-id ]
  (if (contains? @neighbors-map node-id)
    (let [neigh-list (get @neighbors-map node-id)]
    (if (not (in-seq? new-node-id neigh-list))
      (swap! neighbors-map assoc
	     node-id (concat neigh-list (list new-node-id)))
      ))
    (swap! neighbors-map assoc node-id (list new-node-id))
    ))

(defn create-node-list [] (ref ()))

(defn add-node [n node-list] 
  (dosync (alter node-list conj n)))



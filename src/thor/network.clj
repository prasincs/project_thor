(ns thor.network
  (:require clojure.core thor.node)
  (:use thor.utils)
  ;  (:import [java.lang.Math])
  )


(defn squared [x] (* x x))

(defn find-distance [n1 n2 ] (Math/sqrt
                               (+
                                 (squared ( - (-> n2 :location :x) (-> n1 :location :x)))
                                 (squared ( - (-> n2 :location :y) (-> n1 :location :y))))))

(defn min-range [n1 n2] (min (:range n1) (:range n2)))

(def *neighbors* (atom {}))



(defn add-neighbor [neighbors-map node-id new-node-id ]
  (if (contains? @neighbors-map node-id)
    (let [neigh-list (get @neighbors-map node-id)]
      (if (not (in-seq? new-node-id neigh-list))
        (swap! neighbors-map assoc
               node-id (concat neigh-list (list new-node-id)))
        ))
    (swap! neighbors-map assoc node-id (list new-node-id))
    ))


(defn find-closest-node [node node-list & args]
  (let [dist (atom 0)
        ret-node (atom {})]
  (doseq [n @node-list]
    (let [cur-dist (find-distance node n) ]
    (if (< cur-dist dist)
      (reset! dist  cur-dist)
      (reset! ret-node n )
      )
    ))
    @ret-node
    ))




(defn find-neighbors [node node-list]
  (let [neighbors (atom ())]
  (doseq [n @node-list]
      (if (not (= (:id node) (:id n)))
        (if (<= (find-distance node  n) (min-range node n))
          (do
          (swap! neighbors concat (list (:id n))))
            )))
    @neighbors))

(defn create-node-list [] (ref ()))

(defn add-node [n node-list] 
  (dosync (alter node-list conj n)))




(ns thor.math
  (:use clojure.contrib.generic.math-functions)
  )

(defn log_10 [x] 
  (/ (log x) 
     (log 10)))


(defn squared [x] (* x x))

(defn find-distance [n1 n2 ] 

  (Math/sqrt
    (+
      (squared ( - (-> n2 :location :x) (-> n1 :location :x)))
      (squared ( - (-> n2 :location :y) (-> n1 :location :y))))))


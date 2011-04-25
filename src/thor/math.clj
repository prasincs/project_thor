(ns thor.math)

(defn squared [x] (* x x))

(defn find-distance [n1 n2 ] 

  (Math/sqrt
    (+
      (squared ( - (-> n2 :location :x) (-> n1 :location :x)))
      (squared ( - (-> n2 :location :y) (-> n1 :location :y))))))


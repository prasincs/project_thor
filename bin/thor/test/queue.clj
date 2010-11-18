(ns thor.test.queue
  (:use [thor.queue] :reload)
  (:use [clojure.test]))

(defn put-items[] 
  (let [q (create-queue)]
    (dotimes [_ 10]
      (add-to-queue q (create-event (format "event %d" _) (.nextInt random 10))
        ))
    (.size q)
    ))

(deftest put-items-test
  (is (= (put-items) 10)))
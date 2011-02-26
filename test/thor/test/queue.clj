(ns thor.test.queue
  (:use [thor.queue] :reload)
  (:use [clojure.test]))

(defn mkqueue [n ] 
  (binding [*queue* (create-queue)]
    (dotimes [_ n]
      (add-events-to-queue 
        (create-event 
          (int (rand n))  '(format "'(println %s)" _) ))
    )
    @*queue*
    ))




(deftest put-items-test
  (let [ q (mkqueue 10)]
  (is (and (= (count q ) 10))))

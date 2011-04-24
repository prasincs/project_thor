(use 'thor.lang)
(use '(incanter core stats charts))

(defduration 100)

(simulation-init)

(def mt (atom ()))

(at 10 
    (println "test " (get-current-time)))

(at 20 
    (println "test2 " (get-current-time)))

(every 5 
       (println "clock " (get-current-time) ))

(at 15 
    (every 10 
           (println "clock2 " (get-current-time))
           ))
(every 1 (do
           (println "adding")
           (swap! mt concat (list (* (get-current-time) 2)))))

(at 100
    (let [x (range 1 101)
          x_2 @mt]
  (view (bar-chart x x_2))))

(simulation-run)

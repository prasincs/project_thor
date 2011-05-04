
(use 'thor.lang)
(use '(incanter core stats charts))
(defduration 100)
(simulation-init)
(def mt (atom ()))
(every 1 (do
           (println "adding")
           (swap! mt conj 
                  (* (get-current-time) 
                     2))))
(at 100
    (let [x (range 1 101)
          x_2 (reverse @mt)]
      (view (bar-chart x x_2 :y-label "2*x"))))
(simulation-run)

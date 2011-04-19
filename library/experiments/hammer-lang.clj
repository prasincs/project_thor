(use 'thor.lang)

(defduration 100)

(simulation-init)

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

(simulation-run)

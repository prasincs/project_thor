(use 'thor.lang)

(defduration 100)

(simulation-init)

(at 10 
    (println "test"))

(at 20 
    (println "test2"))

(every 10 
       (println "clock"))

(simulation-run)

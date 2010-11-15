(ns thor.main
  (:gen-class)
  (:use thor.node thor.network)
  )

  (defn -main [& args]
    ( let [ node (create-node 1 "test")
            node-list (create-node-list)]
      (println (get-name node))
      (cons (add-node node node-list) node-list)
      )
    (println "Hello World"))
  


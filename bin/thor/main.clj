(ns thor.main
  (:gen-class)
  (:use thor.node thor.network)
  )

(def node-list (create-node-list))

(defn run-simulation []
    ( let [ node (create-node 1 "test")
            node-list (create-node-list)]
      (println (get-name node))
      
      )
    
  )
  

  (defn -main [& args]
    (run-simulation)
    )
  


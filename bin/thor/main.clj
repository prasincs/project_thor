(ns thor.main
  (:gen-class)
  (:use thor.node thor.network thor.ui.window)
  (:import [java.util Random])
  )

(def node-list (create-node-list))

(defn run-simulation []
  
  )

(defn main []
  (let [ random (Random. )]
    (dotimes [_ 10]
   (add-node (create-random-node 500 500) node-list)
  )
    (init-window @node-list)
    (run-simulation)
    ))

 (defn -main [& args]
   (main)
  )
  


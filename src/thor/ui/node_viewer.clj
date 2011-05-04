(ns 
  thor.ui.node-viewer
  (:import [javax.swing JFrame JList JButton BoxLayout JPanel DefaultListModel BoxLayout ListSelectionModel JScrollPane ScrollPaneConstants ]
           [java.awt Dimension GridBagLayout BorderLayout]
  
           [javax.swing.event ListSelectionListener]
           ))

(use '[incanter core processing io])
(def frame 
  (doto (JFrame. "Project Thor")
    (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
    (.setSize (Dimension. 500 500))
    ))

(def radius 100)
(def textValue (atom 0))
(def nodelist (atom ()))

; keep a record of states to replay later
(def states (atom {})) 

(def statesListModel (DefaultListModel.))
(def statesList (let [stateList (doto (JList. 
                  statesListModel)
                   (.setSelectionMode ListSelectionModel/SINGLE_SELECTION)
                   (.setFixedCellWidth 200)
                                                     
                                  )
                      ]
                 (doto stateList
                   
                   (.addListSelectionListener 
                     (proxy [ListSelectionListener] [] 
        
                       (valueChanged [evt]
                                     (let [selected (.getSelectedValue stateList)]
                                       (println "selected " selected )
                                       (apply-state selected)
                                       )
                                     )
                       )
                     )

                   )

                 (JScrollPane. stateList ScrollPaneConstants/VERTICAL_SCROLLBAR_ALWAYS ScrollPaneConstants/HORIZONTAL_SCROLLBAR_AS_NEEDED 
                  )))

(defn add-states-to-list [model elements]
  (doseq [elem elements]
    (.addElement model elem)
    )
  )

(defn get-sketch []


    (sketch 
          
          (setup []
                 (doto this
                   
                   (framerate 15)
                   (size 500 500)
                   (stroke-weight 1)
                   )
                 )
          (draw []
                (doto this
                  (background 255)
                  (smooth)
                  (fill 0)
                  
                  (text (format "Test:%d" @textValue) 0 16 ))
                  
                  (doseq [n @nodelist]
                    (let [loc (-> n :location)]      
                      (doto this
                        (fill 0)
                        (ellipse (:x loc) (:y loc) 20 20)
                      (fill 64 187 128 100)
                      ;(println loc)
                    (ellipse (:x loc) (:y loc) radius radius)
                        (fill 0)
                        (text (format "Node %d" (:id n)) (:x loc) (:y loc))
                    ))
                  
                  )))

          )
    
  
(def sktch (get-sketch))

(defn init-window []
    
      (doto frame 

        (.setLayout (BoxLayout. (.getContentPane frame)
                           BoxLayout/X_AXIS
                           ))
        (.add sktch)
        (.add statesList)
        (.setVisible true)
        )
    (.init sktch)
  )

(defn apply-state [time]
  (let [state (get @states time)]
    (reset! textValue (:textValue state))
    (reset! nodelist (:nodelist state))
  ))

(defn set-state [time {:keys [t nodes]}]
  (println "set-state")
  (swap! states assoc time 
         {:textValue t
          :nodelist nodes
          }
         )
  (add-states-to-list 
    statesListModel 
    (list time))
  (apply-state time)
  )

(defn add-nodes [time {:keys [text nodes]}]
  (println nodes)
  (set-state time
    {:t text
     :nodes nodes})
  
  ;(reset! t tval)
  )

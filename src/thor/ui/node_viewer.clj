(ns 
  thor.ui.node-viewer
  (:import [javax.swing JFrame JList JButton BoxLayout JPanel DefaultListModel BoxLayout ListSelectionModel JScrollPane JViewport JLabel ScrollPaneConstants ]
           [java.awt Dimension GridBagLayout BorderLayout]
  
           [javax.swing.event ListSelectionListener]
           ))

(use '[incanter core processing io])
(def frame 
  (doto (JFrame. "Project Thor")
    (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
    (.setSize (Dimension. 700 500))
    ))

(def radius 100)
(def textValue (atom ""))
(def nodelist (atom ()))

; keep a record of states to replay later
(def states (atom {})) 

(def statesListModel (DefaultListModel.))
(def stateList (doto (JList. 
                  statesListModel)
                   (.setSelectionMode ListSelectionModel/SINGLE_SELECTION)
                   (.setFixedCellWidth 200)
                                                     
                                  )
)
(def statesListScrollPane 
                (let [header 
                      (doto 
                      (JViewport. )
                        (.setView 
                          (JLabel. "Time"))
                        )]
                (doto 
                 (JScrollPane. 
                   stateList 
                   ScrollPaneConstants/VERTICAL_SCROLLBAR_ALWAYS 
                   ScrollPaneConstants/HORIZONTAL_SCROLLBAR_AS_NEEDED 
                  )
                  (.setColumnHeader header)
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
                  
                  (text @textValue 0 16 ))
                  
                  (doseq [n @nodelist]
                    (let [loc (-> n :location)]      
                      (doto this
                        (fill 0)
                        (ellipse (:x loc) (:y loc) 20 20)
                      (fill 64 187 128 100)
                      ;(println loc)
                    (ellipse (:x loc) (:y loc) radius radius)
                        (fill 250 79 45)
                        (text (if (nil? (:name n)) 
                                        (format "Node %d" (:id n))
                                (:name n)
                                ) (:x loc) (:y loc))
                        ;(text (format "(%d, %d)" (:x loc) (:y loc)) (:x loc) (:y loc))
                        ;(text (format "Node %d" (:id n)) (:x loc) (:y loc))
                    ))
                  
                  )))

          )
    
  
(def sktch (get-sketch))

(defn apply-state [time]
  (let [state (get @states time)]
    (reset! textValue (:textValue state))
    (reset! nodelist (:nodelist state))
  ))


(defn init-window []
    
      (doto frame 

        (.setLayout (BoxLayout. (.getContentPane frame)
                           BoxLayout/X_AXIS
                           ))
        (.add sktch)
        (.add statesListScrollPane)
        (.setVisible true)

        )
        (doto stateList
                   
                   (.addListSelectionListener 
                     (proxy [ListSelectionListener] [] 
        
                       (valueChanged [evt]
                                     (let [selected (.getSelectedValue stateList)]
                                       ;(println "selected " selected )
                                       (apply-state selected)
                                       )
                                     )
                       )
                     )

                   )
    (.init sktch)
  )

(defn set-state [time {:keys [t nodes]}]
  ;(println "set-state")
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
  (. Thread (sleep 200))
  ;(println nodes)

  (set-state time
    {:t text
     :nodes nodes})
  
  ;(reset! t tval)
  )

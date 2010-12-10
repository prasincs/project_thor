(ns thor.ui.animation
  (:gen-class)
   (:import [java.util Random] [java.awt.event MouseEvent ActionListener ]
     [javax.swing JFrame JButton BoxLayout JPanel JSlider] 
     [java.awt Dimension GridBagLayout FlowLayout BorderLayout]
     [javax.swing.event ChangeListener]
     )
   (:use thor.utils)
    )
(use '(incanter core processing ))
 
;(def e (ref ())) 
(defn animation [title nodenum minRange maxRange duration]
(let [frame (JFrame. title)
      buttons-panel (JPanel.)
      start-button (JButton. "Start")
      stop-button (JButton. "Stop")
      fps (JSlider. JSlider/HORIZONTAL 1 100 15)
      nodes nodenum
      maxSize maxRange
      e (ref ())
      dragging (ref false)
      mouse-pos (ref {:x :y})
      width 600
      height 600
      minSize minRange
      current-framerate (ref 15)
      ; float array to store circle properties?
      ds 2
      sel 0
      ;selected node switch
      random (Random.)
      fnt (ref nil)
      running (ref false)
      current-time (ref 0)
  ; define a sketch object 
  sktch (sketch
          ;;define the setup function
          (setup []
            (doto this
              
              ;no-loop
              (framerate @current-framerate)
              (size width height)
              (stroke-weight 1)
            )
           (dotimes [_ nodes] 
             (dosync (alter e conj
                       ; add a reference of the map
                       (ref 
                         {:x (.nextInt random width)
                        :y (.nextInt random height)
                        :radius (+ minSize (.nextInt random (- maxSize minSize)))
                        :x-speed (+ 0.5 (.nextFloat random))
                        :y-speed (+ 0.5 (.nextFloat random))
                        :selected false
                        }
                         )))
             )
           (dosync (ref-set fnt (load-font this "FreeSans-16.vlw")))
          

          )
          
          ; define the draw function
          
          (draw []
            
            (doto this
              (background 255)
              (smooth)
              (text-font @fnt)
                (fill 0)
                (text (format "time: %d" @current-time) 0 16 )
                )
            
          
                      
             (dosync
                (doseq [n @e] 
                   (let [radi (:radius @n)
                         diam (* 2 radi)]
                      (if (:selected @n)
                        (do 
                          (when @dragging
                            
                              (alter n #(assoc % :x (:x @mouse-pos)))
                              (alter n #(assoc % :y  ( :y @mouse-pos)))
                              )
                        (doto this
                          (fill 64 187 128 100))) 
                       (doto this 
                         (fill 64 128 187 100))
                       ) ;end if
                    
                      
                           
                      
                     (doto this
	                     (no-stroke)
                     
                        ; draw circle
	                     (ellipse (:x @n) (:y @n) radi radi))
                     (when @running
                      ; move the circle
                        (alter n #(assoc % :x (+ (:x @n ) (:x-speed @n))))
                        (alter n #(assoc % :y (+ (:y @n ) (:y-speed @n))))
                        (if (< (:x @n) (- 0 diam))
                          (alter n #(assoc % :x (+ width diam))))
                        (if (> (:x @n) (+ width diam))
                          (alter n #(assoc % :x (- 0 diam))))
                        (if (< (:y @n) (- 0 diam))
                          (alter n #(assoc % :y (+ height diam))))
                        (if (> (:y @n) (+ height diam))
                          (alter n #(assoc % :y (- 0 diam))))
                        
                       )
                     (doseq [n2 @e]
                       (if (< (dist (:x @n) (:y @n) (:x @n2) (:y @n2)) radi) 
                         (doto this
                           (stroke 10)
                           (line (:x @n) (:y @n) (:x @n2) (:y @n2)))
                         ))
                  ))
                ;increment time counter
                (when @running
                  (ref-set current-time (inc @current-time)))
              )
            
          )
          (mouseMoved [mouse-event]
            (dosync
              (alter mouse-pos #(assoc % :x (mouse-x mouse-event)))
              (alter mouse-pos #(assoc % :y (mouse-y mouse-event)))

              ; loop through all the nodes
              (doseq [n @e]
                
                 (let [radi (:radius @n)
                         diam (* 0.5 radi) ; not diameter, tweaking values
                         ]
                   ;(println mouse-event)
                   (if (< (dist (:x @n) (:y @n) 
                            (mouse-x mouse-event)
                            (mouse-y mouse-event))
                         radi)
                     
                    (alter n #(assoc % :selected true))
                     (alter n #(assoc % :selected false))
                     
                     )
                   )
                 ;(println n)
                 )
              ))
          (mouseDragged [mouse-event]
            
            (dosync
              ; save the mouse position while the node is being dragged
              (ref-set mouse-pos {:x (mouse-x mouse-event) :y (mouse-y mouse-event)})
              (ref-set dragging true)
            ))
          
          (mouseReleased [mouse-event]
            (dosync
            (ref-set dragging false)))
          
          
  )
  
  ]
 ; (view sktch :size [width height])
 (doto buttons-panel
   
   (.add start-button)
   (.add stop-button)
   (.add fps)
   (.setPreferredSize (Dimension. 0 30))
   (.setMaximumSize (Dimension. 600 50))
   )
 (doto frame
    (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
    (.setLayout (BoxLayout. (.getContentPane frame) BoxLayout/Y_AXIS))
    ;(.setLayout (BorderLayout. ))
    (.add buttons-panel) 
    (.add sktch)
    (.setSize (Dimension. 600 650))
    ;(.pack)
    (.setVisible true)
    )
 (.init sktch)
 
(onclick start-button
                         (dosync
                         (ref-set running true)))
(onclick stop-button
                         (dosync
                         (ref-set running false)))
(doto fps
  (.addChangeListener (proxy [ChangeListener][]
                          (stateChanged [evt]
                            
  (dosync 
    (ref-set current-framerate (.getValue (.getSource evt)))
    (framerate sktch @current-framerate)))))) 
))

(defn start-animation []
   (animation "test" 100 20 100 1)  )

  

  
   

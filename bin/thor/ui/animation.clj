(ns thor.ui.animation
   (:import [java.util Random]))
  (use '(incanter core processing))
(def e (ref ())) 
(let [nodes 20
      maxSize 100
      width 600
      height 600
      minSize 20
    
      ; float array to store circle properties?
      ds 2
      sel 0
      ;selected node switch
      dragging false
      random (Random.)
      
  ; define a sketch object 
  sktch (sketch
          ;;define the setup function
          
          (setup []
            (doto this
              ;no-loop
              (framerate 15)
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
           

          )
          
          ; define the draw function
          
          (draw []
            (defn process-node [n]
              (def radi (:radius n))
              (def diam (* 2 radi))
              (println n)
              (no-stroke)
              
              (fill 64 128 187 100)
              (ellipse (:width n) (:height n) radi radi)
              
              )
            
            (doto this
              (background 1)
                      )
             (dosync
                (doseq [n @e] 
                   (let [radi (:radius @n)
                         diam (* 2 radi)]
                      (if (:selected @n)  ( println "selected" (doto this(fill 64 187 128 100))) 
                       (doto this (fill 64 128 187 100)))
                    
                     (doto this
	                     (no-stroke)
                     
                        
	                     (ellipse (:x @n) (:y @n) radi radi)
                     )
                     (doseq [n2 @e]
                       (if (< (dist (:x @n) (:y @n) (:x @n2) (:y @n2)) radi) 
                         (doto this
                           (stroke 10)
                           (line (:x @n) (:y @n) (:x @n2) (:y @n2)))
                         ))
                  ))
              )
            
          )
          (mouseMoved [mouse-event]
            (dosync
              
              (doseq [n @e]
                
                 (let [radi (:radius @n)
                         diam (* 2 radi)]
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
          
          
  )
  
  ]
  (view sktch :size [width height]))
  


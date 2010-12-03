(ns thor.ui.window
  (:require clojure.core thor.network))
(import '(javax.swing JFrame JButton JPanel JOptionPane)
  '(java.awt.event ActionListener)
  '(java.awt Color Dimension GridLayout))          


  (defn draw-nodes [g nodes]
  (doseq [ node nodes]
    (def location (node :location))
    (.setColor g (Color. 0 0 0))
    (.fillOval g (get location :x) (get location :y) 15 15)
    )) 
 
(defn create-canvas [node-list]
  (proxy [JPanel] [] 
    (paintComponent [g]
      (proxy-super paintComponent g)
      (.setColor g (Color. 255 255 255))
      (.fillRect g 0 0 500 500)
      (draw-nodes g node-list))
    (getPreferredSize [] (Dimension. 500 500))))


(def window (JFrame. "Project Thor"))
 

(defn init-window [ node-list ]
  (let [canvas (create-canvas node-list)]
 (doto window 
   ;(.setLayout (GridLayout. 2 2 3 3))
   (.add canvas)
   (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
   .pack
   (.setVisible true))))

 ;(.. frame getContentPane (add button))


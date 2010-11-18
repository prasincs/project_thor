(ns thor.ui.window
  (:require clojure.core thor.network))
(import '(javax.swing JFrame JButton JPanel JOptionPane)
  '(java.awt.event ActionListener)
  '(java.awt Color Dimension GridLayout))          


(def list-circles [ {:x 10 :y 10} {:x 40 :y 40} {:x 60 :y 60}])

(defn draw-circles [g circles]
  (doseq [ circle circles]
    (.setColor g (Color. 0 0 0))
    (.fillOval g (get circle :x) (get circle :y) 15 15)
    )) 
 
(defn create-canvas []
  (proxy [JPanel] [] 
    (paintComponent [g]
      (proxy-super paintComponent g)
      (.setColor g (Color. 255 255 255))
      (.fillRect g 0 0 500 500)
      (draw-circles g list-circles))
    (getPreferredSize [] (Dimension. 500 500))))

(let [frame (JFrame. "Project Thor")
     canvas (create-canvas)]
 (doto frame 
   ;(.setLayout (GridLayout. 2 2 3 3))
   (.add canvas)
   (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
   .pack
   (.setVisible true)))

 ;(.. frame getContentPane (add button))


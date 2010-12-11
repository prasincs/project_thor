(ns thor.main
  (:gen-class)
  (:use thor.ui.animation thor.utils)
  (:import [javax.swing JFrame JLabel JTextField JButton]
        [java.awt.event ActionListener]
        [java.awt GridLayout])
  )


(defn main []
 
(let [frame (JFrame. "Project Thor")
      nodenum-label (JLabel. "Number of nodes")
      nodenum-text (JTextField. "20" )
      minRange-text (JTextField. "10")
      minRange-label (JLabel. "Minimum Range")
      maxRange-label (JLabel. "Maximum Range")
      maxRange-text (JTextField. "20")
      duration-label (JLabel. "Duration")
      duration-text (JTextField. "10")
      simulate-button (JButton. "Simulate")]
    (onclick simulate-button
                    (animation "thor"                       
                        (extract-integer nodenum-text)
                        (extract-double minRange-text)  
                        (extract-double maxRange-text)
                      )
                  )
    (doto frame 
                (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
                (.setLayout (GridLayout. 5 2))
                (.add nodenum-label)
                (.add nodenum-text)
                (.add minRange-label)
                (.add minRange-text)
                (.add maxRange-label)
                (.add maxRange-text)
                ;(.add duration-label)
                ;(.add duration-text)
                (.add simulate-button)
                (.pack)
                (.setVisible true))))


 (defn -main [& args]
   (main)
  )
  

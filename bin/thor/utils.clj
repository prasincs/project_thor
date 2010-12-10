(ns thor.utils)

(defmacro extract-double [textbox]
  `(Double/parseDouble 
                        (.getText ~textbox)))

(defmacro extract-integer [textbox]
  `(Integer/parseInt
     (.getText ~textbox)))

(defmacro onclick [button & body]
  `(doto ~button 
  (.addActionListener (proxy [java.awt.event.ActionListener] []
                       (actionPerformed [evt#]
                        ~@body
                         )))))
(ns jdbk-overtone-playground.core (:gen-class))

(defn -main [& args]
  (println (str "Hello, " (if args (first args) "World") "!")))

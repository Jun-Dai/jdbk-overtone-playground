(ns jdbk-overtone-playground.core (:gen-class))

(use 'overtone.live)

(require 'jdbk-overtone-playground.inst)
(refer 'jdbk-overtone-playground.inst)

(defn play-freq
  [metronome beat instrument [freq dur]]
  (let [end (+ beat dur)]
    (if note
      (let [id (at (metronome beat) (instrument freq))]
        (at (metronome end) (ctl id :gate 0))))
    end))

(defn play-note
  [metronome beat inst [note dur]]
  (let [freq (if note (midicps note))]
    (play-freq metronome beat inst [freq dur])))


(defn play-ratio
  [metronome beat instrument orig [ratio dur]]
  (let [freq (if ratio (* orig ratio) nil)]
    (play-freq metronome beat instrument [freq dur])))

(defn one-twenty-bpm [] (metronome 120))
(def otb one-twenty-bpm)


(defn play-freq-score
  ([inst score] (play-freq-score (one-twenty-bpm) inst score))
  ([mnome inst score] (play-freq-score mnome (mnome) inst score))
  ([mnome beat instrument score]
   (let [cur-note (first score)]
       (when cur-note
         (let [next-beat (play-freq mnome beat instrument cur-note)]
           (println "next-beat: " next-beat)
           (apply-at (mnome next-beat) play-freq-score mnome next-beat instrument
                     (next score) []))))))

(defn test-plucked [& args]
  (play-freq-score plucked-string [[440 1] [660 2] [550 1] [880 2]]))

(defn test-organ [& args]
  (play-freq-score organ [[440 1] [660 2] [550 1] [880 2]]))






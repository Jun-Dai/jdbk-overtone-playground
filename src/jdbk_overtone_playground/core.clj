(ns jdbk-overtone-playground.core (:gen-class))

(use 'overtone.live)

(require 'jdbk-overtone-playground.inst)
(refer 'jdbk-overtone-playground.inst)

(defn halt [coll]
  (map #(ctl % :gate 0) coll))

(defn play-freq
  [metronome beat instrument [freq dur]]
  (let [end (+ beat dur)]
    (if freq
      (let [id (at (metronome beat) (instrument freq))]
        (at (metronome end) (ctl id :gate 0))))
    end))

(defn one-twenty-bpm [] (metronome 120))
(def otb one-twenty-bpm)

(defn play-freq-melody
  ([inst score] (play-freq-melody (one-twenty-bpm) inst score))
  ([mnome inst score] (play-freq-melody mnome (mnome) inst score))
  ([mnome beat instrument score]
   (let [cur-note (first score)]
      (when cur-note
         (let [next-beat (play-freq mnome beat instrument cur-note)]
           (apply-at (mnome next-beat) play-freq-melody mnome next-beat instrument
                     (next score) []))))))

(defn ratio->hz [orig [ratio dur]]
  (if ratio [(* orig ratio) dur] [ratio dur]))

(defn ratioscore->hz [orig score]
  (map (partial ratio->hz orig) score))

(defn midinote->hz [[note dur]]
  (if note [(midi->hz note) dur] [note dur]))

(defn midiscore->hz [score]
  (map midinote->hz score))


(def melody {:type :freq :notes [[440 2] [660 2] [528 2] [440 2]]})
(def melody2 {:type :ratio :orig 330 :notes [[nil 2] [1/1 2] [3/2 2] [6/5 2] [1/1 2]]})
(def melody3 {:type :midinotes :notes [[61 2] [68 2] [64 2] [61 2] [nil 2] [60 4]]})

(defn play-score
  ([score] (play-score (:inst score) (score)))
  ([inst score]
   (case (:type score)
     :freq (play-freq-melody inst (:notes score))
     :ratio (play-freq-melody inst (ratioscore->hz (:orig score) (:notes score)))
     :midinotes (play-freq-melody inst (midiscore->hz (:notes score))))))

(defn jdbk-demo []
;  (play-score plucked-string melody3)
  (play-score organ melody2))

(defn test-plucked [& args]
  (play-freq-melody plucked-string [[440 1] [660 2] [550 1] [880 2]]))

(defn test-organ [& args]
  (play-freq-melody organ [[440 1] [660 2] [550 1] [880 2]]))





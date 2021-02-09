(ns jdbk-overtone-playground.inst (:gen-class))

(use 'overtone.live)

;; snagged from https://github.com/overtone/overtone/blob/master/src/overtone/examples/getting_started/video.clj
;; modified to take frequency instead of note
(definst plucked-string [freq 440 amp 0.8 dur 2 decay 30 coef 0.3 gate 1]
  (let [noize  (* 0.8 (white-noise))
        dly    (/ 1.0 freq)
        plk    (pluck noize gate dly dly decay coef)
        dist   (distort plk)
        filt   (rlpf dist (* 12 freq) 0.6)
        clp    (clip2 filt 0.8)
        reverb (free-verb clp 0.4 0.8 0.2)]
    (* amp (env-gen (perc 0.0001 dur)) reverb)))


;; snagged from https://github.com/ctford/dueling-keyboards/blob/master/src/dueling_keyboards/instrument.clj
(definst organ [freq 440 dur 5.0 vol 0.3 wet 0.5 room 0.5 limit 5000 attack 0.1 gate 1]
  (->
    (map #(sin-osc (* freq %)) (range 1 5))
    mix
    (lpf limit)
    (free-verb :mix 0.5 :damp wet :room room)
    (* (env-gen (asr attack 1.0 0.5) (line:kr 1.0 0.0 dur) :gate gate :action FREE))
    (* vol)))


;; take a single piano A4 sample and adjust the sample speed to get different pitches
(def simple-piano-sample (freesound-sample 374311))
(definst simple-piano [freq 440 gate 1]
  (let [env (env-gen (adsr 0 1 1 0.1) :gate gate :action FREE)]
    (scaled-play-buf 2 simple-piano-sample :rate (/ freq 440))))

(ns bel.r05-comps-re-frame.views
  (:require [reagent.core :as r]
            [bel.r05-comps-re-frame.rf :as rf :refer [<sub >evt <sub-lazy]]))



(defn input-num-get-set [path min max]
  [:input {:on-change #(let [v (long (-> % .-target .-value))
                             v (if (> v max) max v)
                             v (if (< v 0) 0 v)]
                         (>evt [::rf/set path v]))
           :type      "number"
           :min       min
           :max       max
           ;:placeholder "123 Enter a number"
           :value     (let [v (<sub [::rf/get path])]
                        (if (< v 0) "" v))}])

(defn range-get-set [text min max path]
  [:div.special
   [:input {:type      "range"
            :id        "range"
            :min       min
            :max       max
            :value     (<sub [::rf/get path])
            :style     {:margin-right "5px"}
            :on-change #(>evt [::rf/set path (-> % .-target .-value)])}]
   [:label {:style {:margin-right "5px"}
            :for   "range"} text]])

(defn input-txt-get-set [path placeholder]
  [:input {:on-change   #(>evt [::rf/set path (-> % .-target .-value)])
           :type        "text"
           :style       {:width 280}
           :placeholder placeholder
           :value       (<sub [::rf/get path])}])

(defn button-cons [text evt-fn]
  [:button {:disabled (or (-> (<sub [::rf/get [:consultant :name]])
                              count
                              (> 2)
                              not)
                          (-> (<sub [::rf/get [:consultant :value]])
                              (< 0)))
            :on-click #(do (println "calling mail event...")
                           (evt-fn))}
   text])

(defn block [text]
  [:div {:style {:margin-top    "80px"
                 :margin-bottom "60px"}}
   [:hr]
   [:h2 text]])

(defn button-with-succ []
 [:<>
  [button-cons "Bewertung absenden" #(do
                                       (>evt [::rf/email]))]
  ;(>evt [::rf/set [:consultant :value] -1])
  ;(>evt [::rf/set [:consultant :name] nil])
  ;(>evt [::rf/set [:consultant :kunde] nil])
  ;(>evt [::rf/set [:consultant :comment] nil]))]

  (when (<sub [::rf/get [:success]])
    [:<>
     [:div "Vielen Dank!"]
     [:div "Sie können jetzt die nächste Bewertung eingeben..."]])
  (when (<sub [::rf/get [:error]])
    [:div
     "Fehler beim Senden der E-Mail. Bitte nochmal senden. Falls es nicht klappt, bitte eine E-Mail an "
     [:a {:href "mailto:tietz@v-und-s.de"} "tietz@v-und-s.de"]
     ". Danke & Sorry."])])

(defn main []
  [:<>


   [:main
    [:img {:src "logo.jpg" :alt "V&S Logo"}]
    [:h1 "V&S - Feedback - Bewertung"]

    ;[block "r/atom data "]
    ;[:pre (with-out-str (cljs.pprint/pprint (<sub [::rf/db])))]


    [:h3 "Wie bewerten Sie den Berater?"]
    [:h5 "Auf einer (sehr harten) Skala von 0 bis 10..."]
    [:p "Zu Ihrer Orientierung hier ein paar Anhaltspunkte. Eine 4 oder 7 geht auch..."]
    [:p "0 = Naja… Wir sind nicht vollständig überzeugt. Passt nicht recht zu uns. Personen-Wechsel würde dem Projekt helfen."]
    [:p "5 = Solide Arbeit. Ergebnis in Ordnung. Dockt bei fast allen Kollegen gut an. Wenn das so bleibt, ist alles gut. Richtige Person für das Projekt und die Aufgabe."]
    [:p "10 = Wow! Einer der besten Berater bisher. Ansprache, Auftritt, Sach- und Sozialkompetenz überdurchschnittlich. Überraschend gute Wirkung im Projekt! Hinterlässt einen bleibenden Eindruck."]

    [:h5 "Hier den Namen des Beraters eingeben: "]
    [:p [input-txt-get-set [:consultant :name] "Berater-Name"]]

    [:h5 "Bewertung (0-10) eintragen"]
    [input-num-get-set [:consultant :value] -1 10]
    [range-get-set nil #_"oder den Slider nutzen... (0-10)" -1 10 [:consultant :value]]
    [:br]
    [button-with-succ]
    [:br]
    [:br]
    [:br]
    [:br]
    [:h5 "Nur wenn Sie mögen: Kommentar und Ihr Name"]
    [:textarea {:rows        7
                :value       (<sub [::rf/get [:consultant :comment]])
                :on-change   #(>evt [::rf/set [:consultant :comment] (-> % .-target .-value)])
                :placeholder "OPTIONAL: Kommentare, Hinweise, ..."}]
    [input-txt-get-set [:consultant :kunde] "OPTIONAL: Ihr Name, Firma"] [:div "gerne auch annonym - einfach hier nichts eintragen."]
    [:br]
    ;[button-with-succ]


    [:footer
     [:br] [:br]
     [:hr]
     [:p (str "© " (.getFullYear (js/Date.)) " V&S")]]]])




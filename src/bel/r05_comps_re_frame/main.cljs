(ns bel.r05-comps-re-frame.main
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [bel.r05-comps-re-frame.rf :as rf :refer [<sub >evt >evt-now]]
   [bel.r05-comps-re-frame.views :as views]
   [bel.r05-comps-re-frame.config :as config]
   [goog.Uri]
   [bel.r05-comps-re-frame.ajax :as ajax]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root)
    (rdom/render [views/main] root)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn init []
 (let [uri (goog.Uri. js/window.location.href)
       consultant (.getParameterValue uri "consultant")]
   (println "consultant: " consultant) 
  (>evt-now [::rf/boot])
  (when consultant
         ;; Dispatch an event to store 'consultant' in app-db
    (>evt-now [::rf/set [:consultant :name] consultant])) 
  (dev-setup)
  (mount-root)
  (ajax/load-interceptors!) ))

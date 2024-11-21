(ns bel.r05-comps-re-frame.rf
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx reg-sub reg-fx reg-cofx path]]
   #_[ajax.core :refer [POST]]
   [bel.r05-comps-re-frame.ajax :as ajax]
   [day8.re-frame.http-fx]))
   


;; ========== SETUP ============================================================
(def <sub (comp deref re-frame/subscribe))
(def <sub-lazy re-frame/subscribe)
(def >evt re-frame/dispatch)
(def >evt-now re-frame/dispatch-sync)

(def default-db
  {:consultant {:value -1}})


(defn send-email [data]
  (let [url "https://decision-konsent.herokuapp.com/api/email" #_"https://api.resend.com/emails"
        ;api-key ""  
        payload {:email data
                 :password "password"
                 :user "password"}]
        
    (ajax/wrap-post {:uri url
                     :params payload
                     :on-success [::set [:success] true]
                     :on-failure [::set [:error] true]})))



(comment
  (send-email {:ein-test :data}))


;; ========== EFFECTS ==========================================================
(reg-event-fx
 ::email
 (fn [{:keys [db] :as cofx} _]
  (let [xmap (send-email (:consultant db))]
    ;(println "xmap: " xmap)
    {:http-xhrio xmap})))

(reg-event-fx
 ::boot
 (fn [_ _]
   {:db default-db}))

(reg-event-db
 ::ping
 (fn [db [_ val]]
   (let [db-new (if val
                  (assoc db :ping val)
                  (update db :ping (fnil inc 0)))]
     ;(println db-new)
     db-new)))

(reg-event-db
 ::set
 (fn [db [_ path val]]
   (let [db-new (assoc-in db path val)]
     ;(println db-new)
     db-new)))

;; ========== SUBSCRIPTIONS ====================================================
(reg-sub
 ::db
 (fn [db _]
   db))

(reg-sub
 ::ping
 (fn [db _]
   (:ping db)))

(reg-sub
 ::get
 (fn [db [_ path]]
   ;(println "get: " path)
   (get-in db path)))

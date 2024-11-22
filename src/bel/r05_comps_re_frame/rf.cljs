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
  (let [url     "https://decision-konsent.herokuapp.com/api/email" #_"https://api.resend.com/emails"
        ;api-key ""  
        payload {:email    data
                 :password "password"
                 :user     "password"}]

    (ajax/wrap-post {:uri        url
                     :params     payload
                     :on-success [::success]
                     :on-failure [::set [:error] true]})))



(comment
  (send-email {:ein-test :data}))


;; ========== EFFECTS ==========================================================

(reg-event-fx
  ::error
  (fn [{:keys [db] :as cofx} _]
    ;(println "error: " db)
    {:db (-> db
             (assoc :error true))
     :fx [[:dispatch-later {:ms 7000 :dispatch [::set [:error] false]}]]}))

(reg-event-fx
  ::success
  (fn [{:keys [db] :as cofx} _]
    ;(println "success: " db)
    {:db (-> db
             (assoc :success true)
             (assoc :error false)
             (assoc-in [:consultant :value] -1)
             (assoc-in [:consultant :name] nil)
             (assoc-in [:consultant :comment] nil))
     :fx [[:dispatch-later {:ms 10000 :dispatch [::set [:success] false]}]]}))

(reg-event-fx
  ::email
  (fn [{:keys [db] :as cofx} _]
     (let [xmap (send-email (:consultant db))]
         ;(println "xmap: " xmap)
         ;{:fx [[:dispatch [::success]]]} ; for testing
         {:http-xhrio xmap})))

(reg-event-fx
  ::boot
  (fn [_ _]
    {:db default-db}))

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
  ::get
  (fn [db [_ path]]
    ;(println "get: " path)
    (get-in db path)))

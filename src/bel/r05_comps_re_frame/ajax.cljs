(ns bel.r05-comps-re-frame.ajax
  (:require
    [ajax.core :as ajax]))

(defn local-uri? [{:keys [uri]}]
  ;(println uri)
  (not (re-find #"^\w+?://" uri)))

(defn default-headers [request]
  (if (local-uri? request)
    (-> request
        (update :headers #(merge {"x-csrf-token" js/csrfToken} %)))
    request))



(defn load-interceptors! []
  (swap! ajax/default-interceptors
         conj
         (ajax/to-interceptor {:name    "default headers"
                               :request default-headers})))


(defn http-xhrio [xhrio-map method]
 {:method          method
  :uri             (:uri xhrio-map)
  :params          (:params xhrio-map)
  :timeout         5000
  :format          (ajax/json-request-format)
  :response-format (ajax/json-response-format {:keywords? true})
  :on-success      (:on-success xhrio-map)
  :on-failure      (:on-failure xhrio-map)})


(defn wrap-post [xhrio-map]
  (http-xhrio xhrio-map :post))

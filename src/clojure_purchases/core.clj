(ns clojure-purchases.core
  (:require [clojure.string :as str]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

(def file-name "purchases.csv")

(defn read-purchases [& args]
  (let [purchases (str/split-lines(slurp file-name))        
        purchases (map (fn [line]                         
                         (str/split line #","))                    
                    purchases)        
        header (first purchases)        
        purchases (rest purchases)        
        purchases (map (fn [line]                         
                         (zipmap header line))                    
                    purchases)]
    purchases))        

(defn purchases-html [category]
  (let [purchases (read-purchases)
        purchases (filter (fn [purchase]
                            (or (nil? category)
                                (= category (get purchase "category"))))
                    purchases)]
    [:ol
     (map (fn [purchase]
            [:li (str
                   (get purchase "customer_id")
                   " "
                   (get purchase "date")
                   " "
                   (get purchase "credit_card")
                   " "
                   (get purchase "cvv")
                   " "
                   (get purchase "category"))])
       purchases)]))

(c/defroutes app
  (c/GET "/" []
    (h/html [:html 
             [:body
              [:a {:href "/"} "All"]
              " "
              [:a {:href "/Alcohol"} "Alcohol"]
              " "
              [:a {:href "/Furniture"} "Furniture"]
              " "
              [:a {:href "/Toiletries"} "Toiletries"]
              " "
              [:a {:href "/Shoes"} "Shoes"]
              " "
              [:a {:href "/Jewelry"} "Jewelry"]
              (purchases-html nil)]]))
  (c/GET "/:category" [category]
    (h/html [:html
             [:body
              (purchases-html category)]])))
                   
(defn -main []
  (j/run-jetty app {:port 3000}))
    
                        
  
  


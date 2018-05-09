(ns clojure-bakery.core
  (require [clojure.core.async :as async :refer :all])
  (:gen-class))

;This function creates and populates the customer channel, each customer has a number and a fib number
(defn create-customers [num-of-customers]
  (let [customer-chan (chan num-of-customers)]
      (go
        (doseq [customers (range 1 (+ num-of-customers 1))]
        (>! customer-chan {:customer-num customers :fib-num (rand-int 40)})))
        customer-chan))

;This function creates and populates the worker channel, each worker has just a number
(defn create-workers [num-of-workers]
  (let [worker-chan (chan num-of-workers)]
      (go
        (doseq [workers (range 1 (+ num-of-workers 1))]
        (>! worker-chan workers)))
      worker-chan))

;https://gist.github.com/PlugIN73/9973838 source for this fib code
(defn fib
  [x]
  (cond
    (= x 0) 0
    (= x 1) 1
    :else (+ (fib (dec x)) (fib (- x 2)))))

;Takes both channels and prints out the results of fib for each customer until there are no customers left
(defn do-work [customer-chan worker-chan num-of-customers]
  (def customer-being-served 1)
  (while (<= customer-being-served num-of-customers)
  (dosync
    (let [customer (<!! customer-chan)
          worker (<!! worker-chan)
          result (fib (get customer :fib-num))]
    (if-not (nil? customer)
      (println "Customer" (get customer :customer-num) "got the fib of" (get customer :fib-num) "which is" result "from worker" worker))
      (>!! worker-chan worker)
      (def customer-being-served (inc customer-being-served))
    )))
    (println "All customers served, great job team!"))

(defn -main
  [num-of-customers num-of-workers]
  (let [workers (create-workers (read-string num-of-workers))
        customers (create-customers (read-string num-of-customers))]
    (do-work customers workers (read-string num-of-customers))))

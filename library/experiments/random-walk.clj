(defexpt 
  :title "Random Walk"
  :devices {:types ["phone-1"] :sample (random)}
  :number 10
  :time  100
  :field {:width 100 :height 100}
  :direction (random)
  :speed (random)
  :global-payload (defdata 1) ;1 KB of data
  :global-actions (seq 
                    (every 10 search-neighbors)
                    (on-new-neighbor (send-payload))
                   )
  )

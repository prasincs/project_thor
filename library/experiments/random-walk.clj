(defexpt  random-walk 
         {
          :title "Random Walk"
          :devices {:types ["phone-1"] 
                    :number 10 
                    :sample (random)}
          :duration  100
          :area {:width 100 :height 100}
          :direction (random)
          :speed (random)
          :global-payload (defdata 1) ;1 KB of data
          :global-actions (seq 
                            (every 10 find-neighbors)
                            (on-new-neighbor (send-payload))
                            )
          })

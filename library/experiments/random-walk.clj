
(defduration 100)


(defexpt  random-walk 
         {
          :title "Random Walk"
          :devices {:types ["phone-1"] 
                    :number 10 
                    :sample 'random}
          :area {:width 100 :height 100}
          :direction 'random
          :speed 'random
          :global-payload (defdata 1 K) ;1 KB of data
          :global-actions [ (every 10 'find-neighbors)
                           (on-new-neighbor 'send-payload)
                          
                          ]

          })

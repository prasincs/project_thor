(use 'thor.lang 'thor.net.wireless)

(defduration 100)

; For the values of Path Propagation Loss - look at
; Wireless Networks - Local and Wireless Networks (Ivan Marsic ) [Pg 33]
;




(defmedium {:type wireless
            :bandwidth "1.5 Mbps"
            :frequency "2.4 GHz"
            :noise "0"
            :propagation-loss-expt 2 ; free space
            :loss [(propagation-loss)]
            })




  

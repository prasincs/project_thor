---
title: Hammer Time
layout: post
tags: thor, hammer
---

I've been busy with designing various sections of Project Thor and one of the things I realized somewhat early on was that I needed a better way to express the properties of the network to talk meaningfully about network. It's done by almost all major simulators using a Domain Specific Language (DSL). For example NS-2, the big gorilla of network simulator uses TCL. My feelings about TCL aside, I think it's a good idea. Thus, I introduce Hammer - the DSL for Thor.

Thor, being a discrete event simulator has some salient properties that Hammer leverages. 

* Global Time
* Event Queue
* Clojure/Lisp-ness

I do want to put a disclaimer that I'm still working on the language itself and given how much it has evolved so far, I wouldn't put it past me to change almost everything syntax-wise in the next two weeks.

I have been working on an [example simple dht experiment](https://github.com/prasincs/project_thor/blob/master/library/experiments/simple-dht.clj) setup. What The idea is that you implement DHT/or any other network protocol that you want to test using the library constructs provided already. You define the semantics and functions of the protocol you are experimenting with. And then, with Hammer, you setup the experiment and running time properties.

You can define the duration that you want to run the experiment, what devices you want to use, put experimental data and run various events at given times.

The clojure/lisp-ness comes when we start talking about events in the sense that being able to run data as code and vice versa lets us just append events to queues without complicated message passing and what not.

Given that I have about two weeks left to get this polished. Crunch time baby! Err.. Hammer time!

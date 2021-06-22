networkInstructions = stepFactory.createStep("NetworkInstructions")

networkInstructions.run = {
  println "networkInstructions.run"
  a.addEvent("StepStart", ["step":"networkInstructions"])

  a.setDropPlayers(true)

  g.random(connectivity)

  //JUST in case, remove connections to bad players
  g.V.filter{! it.goodplayer}.each{ v->
    if(v.ai == null){
      g.removeEdges(v)
    }
  }

  g.V.filter{it.goodplayer || it.ai == 1}.each{ v->
    v.selectionSteps = true
    v.updatedThisRound = []
  }

  g.V.filter{it.goodplayer && it.bot==false}.each{ v->
    v.nAlters = v.neighbors.count()
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Instructions_1", v.nAlters)
    a.add(v,[name: "Next", result: {
      v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Instructions_2", v.number)
      a.add(v,[name: "Next", result: {
        if(condition==1){v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Instructions_3", "Below that", "who are currently in your social network", "you are connected to")}
        if(condition==2){v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Instructions_3", "You will then be asked who you would like to share your response to this question with. Later", "who chose to share their opinion with you", "you shared your opinion with")}
        a.add(v,[name: "Next", result: {
          v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Instructions_4")
          a.add(v,[name: "Next", result: {
            v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Instructions_5")
            a.add(v,[name: "Next", result: {
              v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Instructions_Wait")
              a.addEvent("NetworkInstructionsRead", ["v.number":v.number])
            }, class: "btn btn-custom1 btn-lg btn-block"])
          }, class: "btn btn-custom1 btn-lg btn-block"])
        }, class: "btn btn-custom1 btn-lg btn-block"])
      }, class: "btn btn-custom1 btn-lg btn-block"])
    }, class: "btn btn-custom1 btn-lg btn-block"])
  }

  def playerList = []
  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    playerList << v.number
  }
  def sortedList = playerList.sort()
  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    v.History_Ref = sortedList
  }
}

networkInstructions.done = {
  println "networkInstructions.done"
  g.V.filter{it.goodplayer || it.ai == 1}.each{ v->
    v.neighbors.each{n->
      a.addEvent("EdgeList0", ["v.number":v.number, "n.number":n.number])
      println("Edgelist Time_0")
      println(v.number + " " + n.number)
    }
  }
  networkDisclosure1.start()
}

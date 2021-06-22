demoInstructions = stepFactory.createStep("DemoInstructions")

demoInstructions.run = {
  println "demoInstructions.run"
  a.addEvent("StepStart", ["step":"demoInstructions"])

  a.setDropPlayers(true)

  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    v.finalNeighbors = v.neighbors.number.toList()
    v.finalNeighbors = v.finalNeighbors.toArray()
    v.finalNeighbors = v.finalNeighbors.sort()
  }

  g.removeEdges()

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Demo_Instructions", String.format("%.2f", (DemoStepPay)))
    a.add(v, [name: "Next", result: {
      v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Demo_Instructions_Wait")
      a.addEvent("DemoInstructionsRead", ["v.number":v.number])
    }, class: "btn btn-custom1 btn-lg btn-block"])
  }
}

demoInstructions.done = {
  println "demoInstructions.done"
  demoSurvey1.start()
}

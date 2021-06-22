demoSurvey1 = stepFactory.createStep("DemoSurvey1")

demoSurvey1.run = {
  println "demoSurvey1.run"
  a.addEvent("StepStart", ["step":"demoSurvey1"])

  a.setDropPlayers(true)

  g.V.filter{it.goodplayer && !it.bot}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Demo_Heading")
    // a.add(v, createSurveyQuestion(qPartyID_1, v))
  }
}

demoSurvey1.done = {
  println "demoSurvey1.done"
  demoSurvey2.start()
}

surveyStep0A = stepFactory.createStep("SurveyStep0A")

surveyStep0A.run = {
  println "surveyStep0A.run"
  a.addEvent("StepStart", ["step":"survey_0A"])

  a.setDropPlayers(true)

  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qIdeology, v))
    a.add(v, createSurveyQuestion(qPartyID_1, v))
  }
}

surveyStep0A.done = {
  println "surveyStep0A.done"
  surveyStep0B.start()
}

surveyStep0B = stepFactory.createStep("SurveyStep0B")

surveyStep0B.run = {
  println "SurveyStep0B.run"
  a.addEvent("StepStart", ["step":"survey_0B"])

  a.setDropPlayers(true)

  g.V.filter{it.PartyID_1=="1"}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qPartyID_R, v))
  }

  g.V.filter{it.PartyID_1=="2"}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qPartyID_I, v))
  }

  g.V.filter{it.PartyID_1=="3"}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qPartyID_D, v))
  }
}

surveyStep0B.done = {
  println "surveyStep0B.done"
  surveyStep1.start()
}

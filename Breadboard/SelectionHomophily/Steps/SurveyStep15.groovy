surveyStep15 = stepFactory.createStep("SurveyStep15")

surveyStep15.run = {
  println "surveyStep15.run"
  a.addEvent("StepStart", ["step":"survey_15"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qCrimA, v))
    a.add(v, createSurveyQuestion(qCrimB, v))
    a.add(v, createSurveyQuestion(qCrimI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.CrimA = (r.nextInt(7) + 1)
    v.CrimB = (r.nextInt(7) + 1)
    v.CrimI = (r.nextInt(7) + 1)
  }
}

surveyStep15.done = {
  println "surveyStep15.done"
  g.V.filter{it.goodplayer}.each{ v->
    v.payment += (BasePay - v.payment)
  }
  networkInstructions.start()
}
surveyStep4 = stepFactory.createStep("SurveyStep4")

surveyStep4.run = {
  println "surveyStep4.run"
  a.addEvent("StepStart", ["step":"survey_4"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qForeA, v))
    a.add(v, createSurveyQuestion(qForeB, v))
    a.add(v, createSurveyQuestion(qForeI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.ForeA = (r.nextInt(7) + 1)
    v.ForeB = (r.nextInt(7) + 1)
    v.ForeI = (r.nextInt(7) + 1)
  }
}

surveyStep4.done = {
  println "surveyStep4.done"
  surveyStep5.start()
}
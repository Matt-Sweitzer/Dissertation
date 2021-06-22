surveyStep12 = stepFactory.createStep("SurveyStep12")

surveyStep12.run = {
  println "surveyStep12.run"
  a.addEvent("StepStart", ["step":"survey_12"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qEnviA, v))
    a.add(v, createSurveyQuestion(qEnviB, v))
    a.add(v, createSurveyQuestion(qEnviI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.EnviA = (r.nextInt(7) + 1)
    v.EnviB = (r.nextInt(7) + 1)
    v.EnviI = (r.nextInt(7) + 1)
  }
}

surveyStep12.done = {
  println "surveyStep12.done"
  surveyStep13.start()
}
surveyStep13 = stepFactory.createStep("SurveyStep13")

surveyStep13.run = {
  println "surveyStep13.run"
  a.addEvent("StepStart", ["step":"survey_13"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qLgbtA, v))
    a.add(v, createSurveyQuestion(qLgbtB, v))
    a.add(v, createSurveyQuestion(qLgbtI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.LgbtA = (r.nextInt(7) + 1)
    v.LgbtB = (r.nextInt(7) + 1)
    v.LgbtI = (r.nextInt(7) + 1)
  }
}

surveyStep13.done = {
  println "surveyStep13.done"
  surveyStep14.start()
}
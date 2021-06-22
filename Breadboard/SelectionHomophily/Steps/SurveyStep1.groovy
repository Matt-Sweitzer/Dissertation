surveyStep1 = stepFactory.createStep("SurveyStep1")

surveyStep1.run = {
  println "surveyStep1.run"
  a.addEvent("StepStart", ["step":"survey_1"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qEconA, v))
    a.add(v, createSurveyQuestion(qEconB, v))
    a.add(v, createSurveyQuestion(qEconI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.EconA = (r.nextInt(7) + 1)
    v.EconB = (r.nextInt(7) + 1)
    v.EconI = (r.nextInt(7) + 1)
  }
}

surveyStep1.done = {
  println "surveyStep1.done"
  surveyStep2.start()
}
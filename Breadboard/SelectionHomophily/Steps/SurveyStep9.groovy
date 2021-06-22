surveyStep9 = stepFactory.createStep("SurveyStep9")

surveyStep9.run = {
  println "surveyStep9.run"
  a.addEvent("StepStart", ["step":"survey_9"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qEducA, v))
    a.add(v, createSurveyQuestion(qEducB, v))
    a.add(v, createSurveyQuestion(qEducI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.EducA = (r.nextInt(7) + 1)
    v.EducB = (r.nextInt(7) + 1)
    v.EducI = (r.nextInt(7) + 1)
  }
}

surveyStep9.done = {
  println "surveyStep9.done"
  surveyStep10.start()
}
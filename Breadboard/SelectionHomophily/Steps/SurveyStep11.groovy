surveyStep11 = stepFactory.createStep("SurveyStep11")

surveyStep11.run = {
  println "surveyStep11.run"
  a.addEvent("StepStart", ["step":"survey_11"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qAborA, v))
    a.add(v, createSurveyQuestion(qAborB, v))
    a.add(v, createSurveyQuestion(qAborI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.AborA = (r.nextInt(7) + 1)
    v.AborB = (r.nextInt(7) + 1)
    v.AborI = (r.nextInt(7) + 1)
  }
}

surveyStep11.done = {
  println "surveyStep11.done"
  surveyStep12.start()
}
surveyStep3 = stepFactory.createStep("SurveyStep3")

surveyStep3.run = {
  println "surveyStep3.run"
  a.addEvent("StepStart", ["step":"survey_3"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qImmiA, v))
    a.add(v, createSurveyQuestion(qImmiB, v))
    a.add(v, createSurveyQuestion(qImmiI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.ImmiA = (r.nextInt(7) + 1)
    v.ImmiB = (r.nextInt(7) + 1)
    v.ImmiI = (r.nextInt(7) + 1)
  }
}

surveyStep3.done = {
  println "surveyStep3.done"
  surveyStep4.start()
}
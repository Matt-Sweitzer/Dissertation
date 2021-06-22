surveyStep6 = stepFactory.createStep("SurveyStep6")

surveyStep6.run = {
  println "surveyStep6.run"
  a.addEvent("StepStart", ["step":"survey_6"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qGunsA, v))
    a.add(v, createSurveyQuestion(qGunsB, v))
    a.add(v, createSurveyQuestion(qGunsI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.GunsA = (r.nextInt(7) + 1)
    v.GunsB = (r.nextInt(7) + 1)
    v.GunsI = (r.nextInt(7) + 1)
  }
}

surveyStep6.done = {
  println "surveyStep6.done"
  surveyStep7.start()
}
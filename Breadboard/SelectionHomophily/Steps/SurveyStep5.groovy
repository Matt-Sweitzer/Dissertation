surveyStep5 = stepFactory.createStep("SurveyStep5")

surveyStep5.run = {
  println "surveyStep5.run"
  a.addEvent("StepStart", ["step":"survey_5"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qHealA, v))
    a.add(v, createSurveyQuestion(qHealB, v))
    a.add(v, createSurveyQuestion(qHealI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.HealA = (r.nextInt(7) + 1)
    v.HealB = (r.nextInt(7) + 1)
    v.HealI = (r.nextInt(7) + 1)
  }
}

surveyStep5.done = {
  println "surveyStep5.done"
  surveyStep6.start()
}
surveyStep2 = stepFactory.createStep("SurveyStep2")

surveyStep2.run = {
  println "surveyStep2.run"
  a.addEvent("StepStart", ["step":"survey_2"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qTerrA, v))
    a.add(v, createSurveyQuestion(qTerrB, v))
    a.add(v, createSurveyQuestion(qTerrI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.TerrA = (r.nextInt(7) + 1)
    v.TerrB = (r.nextInt(7) + 1)
    v.TerrI = (r.nextInt(7) + 1)
  }
}

surveyStep2.done = {
  println "surveyStep2.done"
  surveyStep3.start()
}
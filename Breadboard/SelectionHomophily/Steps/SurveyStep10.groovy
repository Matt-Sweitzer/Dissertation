surveyStep10 = stepFactory.createStep("SurveyStep10")

surveyStep10.run = {
  println "surveyStep10.run"
  a.addEvent("StepStart", ["step":"survey_10"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qRaceA, v))
    a.add(v, createSurveyQuestion(qRaceB, v))
    a.add(v, createSurveyQuestion(qRaceI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.RaceA = (r.nextInt(7) + 1)
    v.RaceB = (r.nextInt(7) + 1)
    v.RaceI = (r.nextInt(7) + 1)
  }
}

surveyStep10.done = {
  println "surveyStep10.done"
  surveyStep11.start()
}
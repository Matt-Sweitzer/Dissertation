surveyStep8 = stepFactory.createStep("SurveyStep8")

surveyStep8.run = {
  println "surveyStep8.run"
  a.addEvent("StepStart", ["step":"survey_8"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qTradA, v))
    a.add(v, createSurveyQuestion(qTradB, v))
    a.add(v, createSurveyQuestion(qTradI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.TradA = (r.nextInt(7) + 1)
    v.TradB = (r.nextInt(7) + 1)
    v.TradI = (r.nextInt(7) + 1)
  }
}

surveyStep8.done = {
  println "surveyStep8.done"
  surveyStep9.start()
}
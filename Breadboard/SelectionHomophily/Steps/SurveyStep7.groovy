surveyStep7 = stepFactory.createStep("SurveyStep7")

surveyStep7.run = {
  println "surveyStep7.run"
  a.addEvent("StepStart", ["step":"survey_7"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qSociA, v))
    a.add(v, createSurveyQuestion(qSociB, v))
    a.add(v, createSurveyQuestion(qSociI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.SociA = (r.nextInt(7) + 1)
    v.SociB = (r.nextInt(7) + 1)
    v.SociI = (r.nextInt(7) + 1)
  }
}

surveyStep7.done = {
  println "surveyStep7.done"
  g.V.filter{it.goodplayer}.each{ v->
    v.payment += (BasePay/3).round(2)
  }
  surveyStep8.start()
}
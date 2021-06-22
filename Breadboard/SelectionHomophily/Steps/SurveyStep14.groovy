surveyStep14 = stepFactory.createStep("SurveyStep14")

surveyStep14.run = {
  println "surveyStep14.run"
  a.addEvent("StepStart", ["step":"survey_14"])
  
  a.setDropPlayers(true)
  
  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Heading")
    a.add(v, createSurveyQuestion(qTaxeA, v))
    a.add(v, createSurveyQuestion(qTaxeB, v))
    a.add(v, createSurveyQuestion(qTaxeI, v))
  }
  
  g.V.filter{it.ai == 1}.each{ v->
    v.TaxeA = (r.nextInt(7) + 1)
    v.TaxeB = (r.nextInt(7) + 1)
    v.TaxeI = (r.nextInt(7) + 1)
  }
}

surveyStep14.done = {
  println "surveyStep14.done"
  surveyStep15.start()
}
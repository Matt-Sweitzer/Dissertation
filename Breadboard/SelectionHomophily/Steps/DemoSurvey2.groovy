demoSurvey2 = stepFactory.createStep("DemoSurvey2")

demoSurvey2.run = {
  println "demoSurvey2.run"
  a.addEvent("StepStart", ["step":"demoSurvey2"])

  a.setDropPlayers(true)

  g.V.filter{it.goodplayer && !it.bot && it.PartyID_1=="1"}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Demo_Heading")
    // a.add(v, createSurveyQuestion(qPartyID_R, v))
    a.add(v, createSurveyQuestion(qAffect1_R, v))
    a.add(v, createSurveyQuestion(qAffect2_R, v))
    a.add(v, createSurveyQuestion(qAffect3_R, v))
    a.add(v, createSurveyQuestion(qAffect4_R, v))
  }

  g.V.filter{it.goodplayer && !it.bot && it.PartyID_1=="2"}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Demo_Heading")
    // a.add(v, createSurveyQuestion(qPartyID_I, v))
    a.add(v, createSurveyQuestion(qAffect1_I, v))
    a.add(v, createSurveyQuestion(qAffect2_I, v))
    a.add(v, createSurveyQuestion(qAffect3_I, v))
    a.add(v, createSurveyQuestion(qAffect4_I, v))
  }

  g.V.filter{it.goodplayer && !it.bot && it.PartyID_1=="3"}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Demo_Heading")
    // a.add(v, createSurveyQuestion(qPartyID_D, v))
    a.add(v, createSurveyQuestion(qAffect1_D, v))
    a.add(v, createSurveyQuestion(qAffect2_D, v))
    a.add(v, createSurveyQuestion(qAffect3_D, v))
    a.add(v, createSurveyQuestion(qAffect4_D, v))
  }
}

demoSurvey2.done = {
  println "demoSurvey2.done"
  demoSurvey3.start()
}

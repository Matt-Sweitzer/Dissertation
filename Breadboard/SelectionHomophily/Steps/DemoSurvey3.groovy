demoSurvey3 = stepFactory.createStep("DemoSurvey3")

demoSurvey3.run = {
  println "demoSurvey3.run"
  a.addEvent("StepStart", ["step":"demoSurvey3"])

  a.setDropPlayers(true)

  g.V.filter{it.goodplayer && !it.bot}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Demo_Heading")
    // a.add(v, createSurveyQuestion(qIdeology, v))
    a.add(v, createSurveyQuestion(qDiscussPol, v))
    a.add(v, createSurveyQuestion(qVotedLoc, v))
    a.add(v, createSurveyQuestion(qVotedNat, v))
    a.add(v, createSurveyQuestion(qSignedPat, v))
    a.add(v, createSurveyQuestion(qDonated, v))
    a.add(v, createSurveyQuestion(qAttRally, v))
    a.add(v, createSurveyQuestion(qContactedPol, v))
    a.add(v, createSurveyQuestion(qSignBump, v))
    a.add(v, createSurveyQuestion(qMember, v))
    a.add(v, createSurveyQuestion(qWorkCamp, v))
    a.add(v, createSurveyQuestion(qNewsLet, v))
    a.add(v, createSurveyQuestion(qFacebook, v))
    a.add(v, createSurveyQuestion(qTwitter, v))
    a.add(v, createSurveyQuestion(qInstagram, v))
    a.add(v, createSurveyQuestion(qLiveUS, v))
    a.add(v, createSurveyQuestion(qCitizen, v))
    a.add(v, createSurveyQuestion(qEligibleVote, v))
    a.add(v, createSurveyQuestion(qAge, v))
    a.add(v, createSurveyQuestion(qGender, v))
    a.add(v, createSurveyQuestion(qRace, v))
    a.add(v, createSurveyQuestion(qEduc, v))
    a.add(v, createSurveyQuestion(qIncome, v))
  }
}

demoSurvey3.done = {
  println "demoSurvey3.done"
  chatSelection1.start()
}

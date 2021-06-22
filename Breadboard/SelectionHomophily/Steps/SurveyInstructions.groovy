def BasePay1 = String.format("%.2f", (BasePay/3))
def BasePay2 = String.format("%.2f", ((BasePay/3)*2))
def BasePay3 = String.format("%.2f", (BasePay))

surveyInstructions = stepFactory.createStep("SurveyInstructions")

surveyInstructions.run = {
  println "surveyInstructions.run"
  a.addEvent("StepStart", ["step":"surveyInstructions"])

  a.setDropPlayers(true)

  g.V.filter{it.goodplayer}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Instructions_1", BasePay1, BasePay2)
    a.add(v, [name: "Next", result: {
      v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Instructions_2")
      a.add(v, [name: "Next", result: {
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Survey_Instructions_Wait")
        a.addEvent("SurveyInstructionsRead", ["v.number":v.number])
      }, class: "btn btn-custom1 btn-lg btn-block"])
    }, class: "btn btn-custom1 btn-lg btn-block"])
  }
}

surveyInstructions.done = {
  println "surveyInstructions.done"
  // surveyStep1.start()
  surveyStep0A.start()
}

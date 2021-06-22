def BasePay1 = String.format("%.2f", (BasePay/3))
def BasePay2 = String.format("%.2f", ((BasePay/3)*2))
def BasePay3 = String.format("%.2f", (BasePay))
def letterList = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]
def nameCheck = ["AA", "AB", "AD", "AG", "AH", "AI", "AM", "AN", "AS",
                 "AT", "AW", "AX", "BA", "BE", "BI", "BO", "BY", "DO",
                 "DR", "ED", "EF", "EH", "EL", "EW", "EX", "HA", "HE",
                 "HO", "IF", "IQ", "IS", "IT", "JD", "MR", "MY", "NA",
                 "NO", "OD", "OF", "OI", "OP", "OX", "OY", "OZ", "SO",
                 "TO", "UH", "UM", "UN", "UP", "VD", "VS", "WE", "AL",
                 "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                 "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME",
                 "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV",
                 "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR",
                 "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA",
                 "WA", "WV", "WI", "WY", "US"]
def nameCheck2 = []
for(i=0; i<nameCheck.size(); i++){
  nameCheck2 << nameCheck[i]+"0"
  nameCheck2 << nameCheck[i]+"1"
  nameCheck2 << nameCheck[i]+"2"
  nameCheck2 << nameCheck[i]+"3"
  nameCheck2 << nameCheck[i]+"4"
  nameCheck2 << nameCheck[i]+"5"
  nameCheck2 << nameCheck[i]+"6"
  nameCheck2 << nameCheck[i]+"7"
  nameCheck2 << nameCheck[i]+"8"
  nameCheck2 << nameCheck[i]+"9"
}
nameCheck = nameCheck2

initStep = stepFactory.createStep("InitStep")

initStep.run = {
  println "initStep.run"
  a.addEvent("StepStart", ["step":"initStep"])
  hasStarted += 1

  a.addEvent("Parameters",
    ["addAI":addAI,
    "amtSandbox":amtSandbox,
    "BasePay":BasePay,
    "condition":condition,
    "connectivity":connectivity,
    "DemoStepPay":DemoStepPay,
    "hasStarted":hasStarted,
    "MTurk":MTurk,
    "NetStepPay":NetStepPay,
    "tieChoiceProb":tieChoiceProb,
    "Topic1_Q":Topic1_Q,
    "Topic1_A":Topic1_A,
    "Topic2_Q":Topic2_Q,
    "Topic2_A":Topic2_A,
    "Topic3_Q":Topic3_Q,
    "Topic3_A":Topic3_A,
    "Topic4_Q":Topic4_Q,
    "Topic4_A":Topic4_A,
    "Topic5_Q":Topic5_Q,
    "Topic5_A":Topic5_A,
    "Topic6_Q":Topic6_Q,
    "Topic6_A":Topic6_A,
    "Topic7_Q":Topic7_Q,
    "Topic7_A":Topic7_A,
    "Topic8_Q":Topic8_Q,
    "Topic8_A":Topic8_A,
    "Topic9_Q":Topic9_Q,
    "Topic9_A":Topic9_A,
    "Topic10_Q":Topic10_Q,
    "Topic10_A":Topic10_A,
    "Topic_HeldOut_Q":Topic_HeldOut_Q,
    "Topic_HeldOut_A":Topic_HeldOut_A,
    "uncertainty":uncertainty,
    "waitTime":waitTime])

  g.V.each{ v->
    nameCheck << v.number
  }

  if (addAI){g.addAI(a, 20)}
  g.V.filter{it.ai == 1}.each{ v->
    v.finished = false
    v.consent = true
    v.goodplayer = false
    v.dropped = false
    v.active = true
    v.late = false
    v.selectionSteps = false
    v.selectedfortie = 0
    v.bot = false
    v.payment = 0
    let1 = letterList[r.nextInt(25)]
    let2 = letterList[r.nextInt(25)]
    num1 = r.nextInt(10)
    letters = let1+let2+num1.toString()
    while(nameCheck.contains(letters) & nameCheck.size()<6760){
      let1 = letterList[r.nextInt(25)]
      let2 = letterList[r.nextInt(25)]
      num1 = r.nextInt(10)
      letters = let1+let2+num1.toString()
    }
    v.number = letters
    nameCheck << v.number
    v.score = v.number
  }

  g.V.filter{! it.consent}.each { v->
    a.remove(v)
    v.dropped = true
    v.active = true
    a.addEvent("PlayerRemoved_NoConsent", ["v.number":v.number])
    if(MTurk){
      v.text = c.get("Dropped_NoConsent_MTurk")
    }
    if(! MTurk){
      v.text = c.get("Dropped_NoConsent_RM", v.number)
    }
  }

  // Start dropping inactive players
  a.setDropPlayers(true)

  g.V.filter{it.consent}.each { v->
    v.text = c.get("Init_Button")
    v.text += """<audio autoplay src="https://raw.githubusercontent.com/Matt-Sweitzer/matt-sweitzer.github.io/master/misc/beep-09.mp3"></audio>""".toString()
    a.add(v,
      [name: "Begin",
       result: {
         v.goodplayer = true
         if(v.ai == 1){
           v.goodplayer = false
         }
         v.payment += (BasePay/3).round(2)
         v.text = "<p style='text-align: left; font-size: 16px; font-family: Serif;'><strong>Please wait. Phase 2 will begin shortly.</strong></p>"
         a.addEvent("PlayerReadyUp", ["v.number":v.number])
       }, class: "btn btn-custom1 btn-lg btn-block"])
  }
}
initStep.done = {
  println "initStep.done"
  surveyInstructions.start()
}

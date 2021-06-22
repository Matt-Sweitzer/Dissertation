def curTime = new Date().getTime()
def startTime = curTime + (waitTime * 60000)
def playerCounter = 0
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
def BasePay1 = String.format("%.2f", (BasePay/3))
def BasePay2 = String.format("%.2f", ((BasePay/3)*2))
def BasePay3 = String.format("%.2f", (BasePay))

onJoinStep = stepFactory.createNoUserActionStep()

onJoinStep.run = { playerId->

  println "onJoinStep.run"
  def v = g.getVertex(playerId)

  if(hasStarted==0){
    v.finished = false
    v.consent = false
    v.goodplayer = false
    v.dropped = false
    v.active = true
    v.late = false
    v.selectionSteps = false
    v.selectedfortie = 0
    v.bot = false
    v.payment = 0
    playerCounter += 1
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

    a.addEvent("PlayerJoined", ["v.number":v.number])

    def curTime2 = new Date().getTime()
    def timeToStart = ((startTime - curTime2)/1000).toInteger()

    println("timeToStart: " + timeToStart)
    println("Number of players joined: " + g.V.count())
    // g.addTimer(timeToStart, v.id + "_gameStartTimer", "", "down", "time", "0", {}, v, "info")

    v.text = c.get("Welcome", (timeToStart/60).toInteger())
    a.add(v, [
      name: "Next",
      result:{
        v.text = c.get("Consent")
        a.add(v, [
          name: "Agree to participate",
          result:{
            a.addEvent("PlayerConsented", ["v.number":v.number])
            if(MTurk == true){
              v.text = c.get("WelcomeWait", (timeToStart/60).toInteger(), BasePay1)
              v.text += """<hr style="height:5px;border-width:0;color:gray;background-color:gray">"""
              a.add(v, createSurveyQuestion(qAMT, v))
            }
            if(MTurk == false){
              v.consent = true
              v.text = c.get("WelcomeWait", (timeToStart/60).toInteger(), BasePay1)
            }
          }, class: "btn btn-custom1 btn-lg btn-block"
        ], [
          name: "Decline to participate",
          result:{
            v.consent = false
            v.active = false
            v.dropped = true
            a.addEvent("PlayerDissented", ["v.number":v.number])
            if(MTurk == true){
              v.text = c.get("Dropped_NoConsent_MTurk") + g.getSubmitForm(v, 0, "Player ${v.number} dropped_noconsent", amtSandbox, false)
            }
            if(MTurk == false){
              v.text = c.get("Dropped_NoConsent_RM", v.number)
            }
          }, class: "btn btn-custom1 btn-lg btn-block"
        ])
      }, class: "btn btn-custom1 btn-lg btn-block"
    ])
  }

  if(hasStarted==1){
    if(v.ai == null){
      v.goodplayer = false
      v.consent = false
      v.active = true
      v.payment = 0
      v.number = "-99"
      v.dropped = true
      v.late = true
      if(MTurk == true){
        v.text = c.get("GameStarted_MTurk")
      }
      if(MTurk == false){
        v.text = c.get("GameStarted_RM", v.number, v.payment)
      }
    }
  }
}
onJoinStep.done = {
  println "onJoinStep.done"
}

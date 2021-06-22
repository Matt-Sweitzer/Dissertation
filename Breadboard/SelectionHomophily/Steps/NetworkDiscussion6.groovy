networkDiscussion6 = stepFactory.createStep("NetworkDiscussion6")

networkDiscussion6.run = {
  println "networkDiscussion6.run"
  a.addEvent("StepStart", ["step":"networkDiscussion6"])

  a.setDropPlayers(true)

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Discussion", "6", getTopic(Topic6_Q), getQText(Topic6_Q, Topic6_A), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v).toInteger()))
  }

  def playerList = []
  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    playerList << v.number
  }
  def sortedList = playerList.sort()

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    if(v.curNeighbors.size() == 0){
      v.text += c.get("Network_Disc_NoAlters")
    }
    if(v.curNeighbors.size() > 0){
      v.text += c.get("Network_Disc_Alters_Head", v.curNeighbors.size())
      def rowCount = 0
      for(i = 0; i < sortedList.size(); i++) {
        if(v.History_Rnd6[i] > 0){
          rowCount += 1
          if(rowCount == 1){
            v.text += c.get("Network_Disc_Alters1", "Participant " + sortedList[i].toString(), attConvert(v.History_Rnd6[i]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v).toInteger()))
            rowCount +=1
          }
          if(rowCount == 3){
            v.text += c.get("Network_Disc_Alters2", "Participant " + sortedList[i].toString(), attConvert(v.History_Rnd6[i]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v).toInteger()))
            rowCount = 0
          }
        }
      }
    }
    a.add(v, [name: "Next", result:{
		  a.addEvent("AltersRead", ["v.number":v.number])
      v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Disc_Wait", "6")
    }, class: "btn btn-custom1 btn-lg btn-block"])
  }
}

networkDiscussion6.done = {
  println "networkDiscussion6.done"

  counter = 1
  maintainCount = 0
  dissolveCount = 0
  addCount = 0
  ignoreCount = 0

  numPlayer = g.V.filter{it.goodplayer || it.ai==1}.count()
  choiceCount = 1
  while (choiceCount <= (((numPlayer * (numPlayer-1)) / 2) * tieChoiceProb)){
    choiceCount += 1
  }

  newNetworkTieDecision6.start()
}

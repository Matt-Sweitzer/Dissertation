networkDisclosure1 = stepFactory.createStep("NetworkDisclosure1")

networkDisclosure1.run = {
 println "networkDisclosure1.run"
 a.addEvent("StepStart", ["step":"networkDisclosure1"])

 a.setDropPlayers(true)

 g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
   v.text = c.get("Payment", String.format("%.2f", (v.payment)))
 }

 g.V.filter{it.goodplayer || it.ai==1}.each{ v->
   v.curNeighbors = v.neighbors.number.toList()
   v.curNeighbors = v.curNeighbors.toArray()
   v.curNeighbors = v.curNeighbors.sort()
   v.alterHist = v.neighbors.number.toList()
   v.alterHist = v.alterHist.toArray()
   v.alterHist = v.alterHist.sort()
   v.maintainN = 0
   v.dissolveN = 0
   v.addN = 0
   v.ignoreN = 0
   v.nMaintain = 0
   v.nDissolve = 0
   v.nAdd = 0
   v.nIgnore = 0
   v.E_Maintain_1 = []
   v.E_Dissolve_1 = []
   v.E_Add_1 = []
   v.E_Ignore_1 = []
   v.A_Maintain_1 = []
   v.A_Dissolve_1 = []
   v.A_Add_1 = []
   v.A_Ignore_1 = []
   v.updatedThisRound = []
 }

 g.V.filter{it.goodplayer || it.ai==1}.each{v->
  if(condition==2){
    v.Disclose_1 = v.curNeighbors
  }
 }

 if(condition==2){
   g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
     v.text += c.get("Network_Disclose_Head", "1", getTopic(Topic1_Q))
   }
   g.V.filter{it.goodplayer && it.bot==false && it.curNeighbors.size()==0}.each{v->
     v.Disclose_1 = []
     v.text += c.get("Network_Disclose_NoAlters")
     v.active = true
   }
   g.V.filter{it.goodplayer && it.bot==false && it.curNeighbors.size()>0}.each{v->
     v.text += c.get("Network_Disclose", v.curNeighbors.size().toString())
     QOpts = []
     for(i=0; i<v.curNeighbors.size(); i++){
       QOpts << [value:v.curNeighbors[i], text:v.curNeighbors[i]]
     }
     v.QOpts = QOpts
     qDisclose_1 = [id: "qDisclose_1",
      name: "qDisclose_1",
      header: "",
      text: "",
      qtype: "checkbox2",
      options: v.QOpts
     ]
     a.add(v, createSurveyQuestion(qDisclose_1, v))
   }
 }
}

networkDisclosure1.done = {
  println "networkDisclosure1.done"
  networkDiscloseRecord1.start()
}

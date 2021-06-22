networkDisclosure10 = stepFactory.createStep("NetworkDisclosure10")

networkDisclosure10.run = {
 println "networkDisclosure10.run"
 a.addEvent("StepStart", ["step":"networkDisclosure10"])

 a.setDropPlayers(true)

 g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
   v.text = c.get("Payment", String.format("%.2f", (v.payment)))
 }

 g.V.filter{it.goodplayer || it.ai==1}.each{ v->
   v.curNeighbors = v.neighbors.number.toList()
   v.curNeighbors = v.curNeighbors.toArray()
   v.curNeighbors = v.curNeighbors.sort()
   v.maintainN = 0
   v.dissolveN = 0
   v.addN = 0
   v.ignoreN = 0
   v.nMaintain = 0
   v.nDissolve = 0
   v.nAdd = 0
   v.nIgnore = 0
   v.E_Maintain_10 = []
   v.E_Dissolve_10 = []
   v.E_Add_10 = []
   v.E_Ignore_10 = []
   v.A_Maintain_10 = []
   v.A_Dissolve_10 = []
   v.A_Add_10 = []
   v.A_Ignore_10 = []
   v.updatedThisRound = []
 }

 g.V.filter{it.goodplayer || it.ai==1}.each{v->
  if(condition==2){
    v.Disclose_10 = v.curNeighbors
  }
 }

 if(condition==2){
   g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
     v.text += c.get("Network_Disclose_Head", "10", getTopic(Topic10_Q))
   }
   g.V.filter{it.goodplayer && it.bot==false && it.curNeighbors.size()==0}.each{v->
     v.Disclose_10 = []
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
     qDisclose_10 = [id: "qDisclose_10",
      name: "qDisclose_10",
      header: "",
      text: "",
      qtype: "checkbox2",
      options: v.QOpts
     ]
     a.add(v, createSurveyQuestion(qDisclose_10, v))
   }
 }
}

networkDisclosure10.done = {
  println "networkDisclosure10.done"
  networkDiscloseRecord10.start()
}

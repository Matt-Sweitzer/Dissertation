networkDisclosure6 = stepFactory.createStep("NetworkDisclosure6")

networkDisclosure6.run = {
 println "networkDisclosure6.run"
 a.addEvent("StepStart", ["step":"networkDisclosure6"])

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
   v.E_Maintain_6 = []
   v.E_Dissolve_6 = []
   v.E_Add_6 = []
   v.E_Ignore_6 = []
   v.A_Maintain_6 = []
   v.A_Dissolve_6 = []
   v.A_Add_6 = []
   v.A_Ignore_6 = []
   v.updatedThisRound = []
 }

 g.V.filter{it.goodplayer || it.ai==1}.each{v->
  if(condition==2){
    v.Disclose_6 = v.curNeighbors
  }
 }

 if(condition==2){
   g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
     v.text += c.get("Network_Disclose_Head", "6", getTopic(Topic6_Q))
   }
   g.V.filter{it.goodplayer && it.bot==false && it.curNeighbors.size()==0}.each{v->
     v.Disclose_6 = []
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
     qDisclose_6 = [id: "qDisclose_6",
      name: "qDisclose_6",
      header: "",
      text: "",
      qtype: "checkbox2",
      options: v.QOpts
     ]
     a.add(v, createSurveyQuestion(qDisclose_6, v))
   }
 }
}

networkDisclosure6.done = {
  println "networkDisclosure6.done"
  networkDiscloseRecord6.start()
}

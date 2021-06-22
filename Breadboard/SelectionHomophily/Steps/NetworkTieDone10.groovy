networkTieDone10 = stepFactory.createStep("NetworkTieDone10")

networkTieDone10.run = {
  println "networkTieDone10.run"
  a.addEvent("StepStart", ["step":"networkTieDone10"])

  println "# Ties Maintained: " + maintainCount
  println "# Ties Dissolved: " + dissolveCount
  println "# Ties Added: " + addCount
  println "# Ties Ignored: " + ignoreCount
  a.addEvent("Round10Totals", ["tiesMaintained":maintainCount, "tiesDissolved":dissolveCount, "tiesAdded":addCount, "tiesIgnored":ignoreCount])

  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    a.addEvent("Round10PlayerSummary", ["v.number":v.number, "tiesMaintained":v.maintainN, "tiesDissolved":v.dissolveN, "tiesAdded":v.addN, "tiesIgnored":v.ignoreN])
  }

  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    v.neighbors.each{n->
      a.addEvent("EdgeList10", ["v.number":v.number, "n.number":n.number])
      println("Edgelist Time_10")
      println(v.number + " " + n.number)
    }
  }

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    v.payment += (NetStepPay/10).round(2)
  }

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
   if(v.E_Maintain_10.size() == 0){v.E_Maintain_Txt = "no one"}
   if(v.E_Dissolve_10.size() == 0){v.E_Dissolve_Txt = "no one"}
   if(v.E_Add_10.size() == 0){v.E_Add_Txt = "no one"}
   if(v.E_Ignore_10.size() == 0){v.E_Ignore_Txt = "no one"}
   if(v.A_Maintain_10.size() == 0){v.A_Maintain_Txt = "no one"}
   if(v.A_Dissolve_10.size() == 0){v.A_Dissolve_Txt = "no one"}
   if(v.A_Add_10.size() == 0){v.A_Add_Txt = "no one"}
   if(v.A_Ignore_10.size() == 0){v.A_Ignore_Txt = "no one"}

   if(v.E_Maintain_10.size() == 1){v.E_Maintain_Txt = v.E_Maintain_10[0].toString()}
   if(v.E_Dissolve_10.size() == 1){v.E_Dissolve_Txt = v.E_Dissolve_10[0].toString()}
   if(v.E_Add_10.size() == 1){v.E_Add_Txt = v.E_Add_10[0].toString()}
   if(v.E_Ignore_10.size() == 1){v.E_Ignore_Txt = v.E_Ignore_10[0].toString()}
   if(v.A_Maintain_10.size() == 1){v.A_Maintain_Txt = v.A_Maintain_10[0].toString()}
   if(v.A_Dissolve_10.size() == 1){v.A_Dissolve_Txt = v.A_Dissolve_10[0].toString()}
   if(v.A_Add_10.size() == 1){v.A_Add_Txt = v.A_Add_10[0].toString()}
   if(v.A_Ignore_10.size() == 1){v.A_Ignore_Txt = v.A_Ignore_10[0].toString()}

   if(v.E_Maintain_10.size() == 2){v.E_Maintain_Txt = v.E_Maintain_10[0].toString() + " & " + v.E_Maintain_10[1].toString()}
   if(v.E_Dissolve_10.size() == 2){v.E_Dissolve_Txt = v.E_Dissolve_10[0].toString() + " & " + v.E_Dissolve_10[1].toString()}
   if(v.E_Add_10.size() == 2){v.E_Add_Txt = v.E_Add_10[0].toString() + " & " + v.E_Add_10[1].toString()}
   if(v.E_Ignore_10.size() == 2){v.E_Ignore_Txt = v.E_Ignore_10[0].toString() + " & " + v.E_Ignore_10[1].toString()}
   if(v.A_Maintain_10.size() == 2){v.A_Maintain_Txt = v.A_Maintain_10[0].toString() + " & " + v.A_Maintain_10[1].toString()}
   if(v.A_Dissolve_10.size() == 2){v.A_Dissolve_Txt = v.A_Dissolve_10[0].toString() + " & " + v.A_Dissolve_10[1].toString()}
   if(v.A_Add_10.size() == 2){v.A_Add_Txt = v.A_Add_10[0].toString() + " & " + v.A_Add_10[1].toString()}
   if(v.A_Ignore_10.size() == 2){v.A_Ignore_Txt = v.A_Ignore_10[0].toString() + " & " + v.A_Ignore_10[1].toString()}

   if(v.E_Maintain_10.size() > 2){
    textArray = []
    textArray[0] = v.E_Maintain_10[0]
    for(i=1; i<v.E_Maintain_10.size()-1; i++){textArray[i] = v.E_Maintain_10[i]}
    textArray[v.E_Maintain_10.size()-1] = "& " + v.E_Maintain_10[v.E_Maintain_10.size()-1]
    tempText1 = textArray.toString()
    tempText2 = tempText1.replace('[', '')
    tempText3 = tempText2.replace(']', '')
    v.E_Maintain_Txt = tempText3
   }
   if(v.E_Dissolve_10.size() > 2){
    textArray = []
    textArray[0] = v.E_Dissolve_10[0]
    for(i=1; i<v.E_Dissolve_10.size()-1; i++){textArray[i] = v.E_Dissolve_10[i]}
    textArray[v.E_Dissolve_10.size()-1] = "& " + v.E_Dissolve_10[v.E_Dissolve_10.size()-1]
    tempText1 = textArray.toString()
    tempText2 = tempText1.replace('[', '')
    tempText3 = tempText2.replace(']', '')
    v.E_Dissolve_Txt = tempText3
   }
   if(v.E_Add_10.size() > 2){
    textArray = []
    textArray[0] = v.E_Add_10[0]
    for(i=1; i<v.E_Add_10.size()-1; i++){textArray[i] = v.E_Add_10[i]}
    textArray[v.E_Add_10.size()-1] = "& " + v.E_Add_10[v.E_Add_10.size()-1]
    tempText1 = textArray.toString()
    tempText2 = tempText1.replace('[', '')
    tempText3 = tempText2.replace(']', '')
    v.E_Add_Txt = tempText3
   }
   if(v.E_Ignore_10.size() > 2){
    textArray = []
    textArray[0] = v.E_Ignore_10[0]
    for(i=1; i<v.E_Ignore_10.size()-1; i++){textArray[i] = v.E_Ignore_10[i]}
    textArray[v.E_Ignore_10.size()-1] = "& " + v.E_Ignore_10[v.E_Ignore_10.size()-1]
    tempText1 = textArray.toString()
    tempText2 = tempText1.replace('[', '')
    tempText3 = tempText2.replace(']', '')
    v.E_Ignore_Txt = tempText3
   }
   if(v.A_Maintain_10.size() > 2){
    textArray = []
    textArray[0] = v.A_Maintain_10[0]
    for(i=1; i<v.A_Maintain_10.size()-1; i++){textArray[i] = v.A_Maintain_10[i]}
    textArray[v.A_Maintain_10.size()-1] = "& " + v.A_Maintain_10[v.A_Maintain_10.size()-1]
    tempText1 = textArray.toString()
    tempText2 = tempText1.replace('[', '')
    tempText3 = tempText2.replace(']', '')
    v.A_Maintain_Txt = tempText3
   }
   if(v.A_Dissolve_10.size() > 2){
    textArray = []
    textArray[0] = v.A_Dissolve_10[0]
    for(i=1; i<v.A_Dissolve_10.size()-1; i++){textArray[i] = v.A_Dissolve_10[i]}
    textArray[v.A_Dissolve_10.size()-1] = "& " + v.A_Dissolve_10[v.A_Dissolve_10.size()-1]
    tempText1 = textArray.toString()
    tempText2 = tempText1.replace('[', '')
    tempText3 = tempText2.replace(']', '')
    v.A_Dissolve_Txt = tempText3
   }
   if(v.A_Add_10.size() > 2){
    textArray = []
    textArray[0] = v.A_Add_10[0]
    for(i=1; i<v.A_Add_10.size()-1; i++){textArray[i] = v.A_Add_10[i]}
    textArray[v.A_Add_10.size()-1] = "& " + v.A_Add_10[v.A_Add_10.size()-1]
    tempText1 = textArray.toString()
    tempText2 = tempText1.replace('[', '')
    tempText3 = tempText2.replace(']', '')
    v.A_Add_Txt = tempText3
   }
   if(v.A_Ignore_10.size() > 2){
    textArray = []
    textArray[0] = v.A_Ignore_10[0]
    for(i=1; i<v.A_Ignore_10.size()-1; i++){textArray[i] = v.A_Ignore_10[i]}
    textArray[v.A_Ignore_10.size()-1] = "& " + v.A_Ignore_10[v.A_Ignore_10.size()-1]
    tempText1 = textArray.toString()
    tempText2 = tempText1.replace('[', '')
    tempText3 = tempText2.replace(']', '')
    v.A_Ignore_Txt = tempText3
   }
  }

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Tie_Done", "10", v.E_Maintain_Txt, v.E_Dissolve_Txt, v.E_Add_Txt, v.E_Ignore_Txt)
    a.add(v,[name: "Next", result: {
      v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Tie_Done_2", "10", v.A_Maintain_Txt, v.A_Dissolve_Txt, v.A_Add_Txt, v.A_Ignore_Txt)
      a.add(v,[name: "Next", result: {
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Tie_Done_Wait", "10")
        a.addEvent("NetworkSynopsis10Read", ["v.number":v.number])
      }, class: "btn btn-custom1 btn-lg btn-block"])
    }, class: "btn btn-custom1 btn-lg btn-block"])
  }
}

networkTieDone10.done = {
  println "networkTieDone10.done"
  demoInstructions.start()
}

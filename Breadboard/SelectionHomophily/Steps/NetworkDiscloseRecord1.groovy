networkDiscloseRecord1 = stepFactory.createStep("networkDiscloseRecord1")

networkDiscloseRecord1.run = {
  println "networkDiscloseRecord1.run"
  a.addEvent("StepStart", ["step":"networkDiscloseRecord1"])

  a.setDropPlayers(true)

  if(condition==2){
    //ai (testing) disclose to half of all alters
    println "AI disclosure run"
    g.V.filter{it.ai == 1}.each{v->
      Disclose = []
      if(v.curNeighbors.size()>0){
        for(i=0; i<v.curNeighbors.size(); i++){
          rnum = r.nextDouble()
          if(rnum > 0.5){
            Disclose << v.curNeighbors[i]
          }
        }
      }
      v.Disclose_1 = Disclose
    }
    println "AI disclosure done"
  }

  println "Sorted list run"
  def playerList = []
  g.V.filter{it.goodplayer || it.ai==1}.each{v->
    playerList << v.number
  }
  def sortedList = playerList.sort()
  println "Sorted list done"

  if(condition==2){
    println "MasterDisclose list run"
    masterDisclose_1 = []
    for(i = 0; i < sortedList.size(); i++){
      g.V.filter{it.number==sortedList[i]}.each{v->
        masterDisclose_1 << v.Disclose_1
      }
    }
    println "MasterDisclose list done"
  }

  g.V.filter{it.goodplayer || it.ai==1}.each{v->
    println "History list for v.number " + v.number + " run"
    def allHist = []
    for(i = 0; i < sortedList.size(); i++){
      if(v.curNeighbors.contains(sortedList[i])){
        g.V.filter{it.number == sortedList[i]}.each{n->
          if(condition==1){
            allHist << getAttitudeN(Topic1_Q, Topic1_A, n).toInteger()
          }
          if(condition==2){
            if(masterDisclose_1[i].contains(v.number)){
              allHist << getAttitudeN(Topic1_Q, Topic1_A, n).toInteger()
            }
            if(! masterDisclose_1[i].contains(v.number)){
              allHist << 99
            }
          }
        }
      }
      if(! v.curNeighbors.contains(sortedList[i])){
        allHist << 0
      }
    }
    v.History_Rnd1 = allHist
    println "History list for v.number " + v.number + " done"
  }
}

networkDiscloseRecord1.done = {
  println "networkDiscloseRecord1.done"
  networkDiscussion1.start()
}

networkDiscloseRecord6 = stepFactory.createStep("networkDiscloseRecord6")

networkDiscloseRecord6.run = {
  println "networkDiscloseRecord6.run"
  a.addEvent("StepStart", ["step":"networkDiscloseRecord6"])

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
      v.Disclose_6 = Disclose
    }
    println "AI disclosure done"
  }

  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    println "Add new alters for v.number " + v.number + " run"
    def newAlters = []
    if(v.curNeighbors.size() != 0){
      for(i = 0; i < v.curNeighbors.size(); i++){
        if(! v.alterHist.contains(v.curNeighbors[i])){
          newAlters << v.curNeighbors[i]
        }
      }
    }
    def allAlters = []
    if(v.alterHist.size() != 0){
      for(i = 0; i < v.alterHist.size(); i++){
        allAlters << v.alterHist[i]
      }
    }
    if(newAlters.size() != 0){
      for(i = 0; i < newAlters.size(); i++){
        allAlters << newAlters[i]
      }
    }
    v.alterHist = allAlters
    println "Add new alters for v.number " + v.number + " done"
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
    masterDisclose_6 = []
    for(i = 0; i < sortedList.size(); i++){
      g.V.filter{it.number==sortedList[i]}.each{v->
        masterDisclose_6 << v.Disclose_6
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
            allHist << getAttitudeN(Topic6_Q, Topic6_A, n).toInteger()
          }
          if(condition==2){
            if(masterDisclose_6[i].contains(v.number)){
              allHist << getAttitudeN(Topic6_Q, Topic6_A, n).toInteger()
            }
            if(! masterDisclose_6[i].contains(v.number)){
              allHist << 99
            }
          }
        }
      }
      if(! v.curNeighbors.contains(sortedList[i])){
        allHist << 0
      }
    }
    v.History_Rnd6 = allHist
    println "History list for v.number " + v.number + " done"
  }
}

networkDiscloseRecord6.done = {
  println "networkDiscloseRecord6.done"
  networkDiscussion6.start()
}

newNetworkTieDecision9 = stepFactory.createStep("newNetworkTieDecision9")

newNetworkTieDecision9.run = {
  //Add record of step start
  println("newNetworkTieDecision9.run -- Counter: " + counter)
  a.addEvent("StepStart", ["step":"newNetworkTieDecision9", "counter":counter])

  //Drop inactive players
  a.setDropPlayers(true)

  //Set a default waiting html content for all players
  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Tie_Wait", "9")
  }

  //Set a default graph CSS for all nodes and vertices in the graph
  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    v.selectedfortie = 0
    v.private.selectedfortie = 0
  }
  g.E.each{ e->
    e.selectedfortie = 0
    e.private(e.getVertex(Direction.IN), ["selectedfortie":0])
    e.private(e.getVertex(Direction.OUT), ["selectedfortie":0])
  }

  //Determine which directed relationships will update this round; alters are stored in ego attribute "updatedThisRound"
  while(counter < choiceCount){
    //Choose two random vertices
    g.V.shuffle.filter{it.goodplayer || it.ai==1}.next(1).each{v1->
      g.V.shuffle.filter{(it.goodplayer || it.ai==1) && it.number!=v1.number}.next(1).each{v2->
        //Determine if the selected directed edge has already been updated; if yes, pass without updating the counter
        if(v1.updatedThisRound.contains(v2.number)){
          println("Re-update attempt squashed: " + v1.number + " -> " + v2.number)
        }
        //Determine if the ego is a bot; if so, defer to the human if possible
        if(v1.bot == true && !v1.updatedThisRound.contains(v2.number)){
          //If the inverse direction has already been selected, or if the alter is also a bot, then the ego bot needs to make a decision
          if(v2.updatedThisRound.contains(v1.number) || v2.bot == true){
              //NORMALLY, bot would be assigned the tie in updatedThisRound
              //HOWEVER, because bots always maintain the status quo
              //A HUGE chunk of code can be skipped later by not assigning a tie
              //AND just recording that they "chose" the status quo instead
            //Record selection
            println("Selected for tie: " + v1.number + " and " + v2.number)
            if(!v1.curNeighbors.contains(v2.number) && !v1.alterHist.contains(v2.number)){// new, no history
              a.addEvent("SelectedForTieUpdate", ["round":"9", "ego.number":v1.number, "alter.number":v2.number, "status":"New - no history"])
              //Record bot ignored
                a.addEvent("BotTieIgnored", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
                println("Bot #" + v1.number + " ignored a tie with player #" + v2.number)
                ignoreCount += 1
                v1.ignoreN += 1
                v2.nIgnore += 1
                EList = []
                if(v1.E_Ignore_9.size() > 0){
                  for(i = 0; i < v1.E_Ignore_9.size(); i++){
                    EList << v1.E_Ignore_9[i]
                  }
                }
                EList << v2.number
                v1.E_Ignore_9 = EList
                AList = []
                if(v2.A_Ignore_9.size() > 0){
                  for(i = 0; i < v2.A_Ignore_9.size(); i++){
                    AList << v2.A_Ignore_9[i]
                  }
                }
                AList << v1.number
                v2.A_Ignore_9 = AList
                if(g.getEdge(v1, v2) != null){
                  g.removeEdge(g.getEdge(v1, v2))
                }
            }
            if(!v1.curNeighbors.contains(v2.number) && v1.alterHist.contains(v2.number)){// new, have history
              a.addEvent("SelectedForTieUpdate", ["round":"9", "ego.number":v1.number, "alter.number":v2.number, "status":"New - have history"])
              //Record bot ignored
                a.addEvent("BotTieIgnored", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
                println("Bot #" + v1.number + " ignored a tie with player #" + v2.number)
                ignoreCount += 1
                v1.ignoreN += 1
                v2.nIgnore += 1
                EList = []
                if(v1.E_Ignore_9.size() > 0){
                  for(i = 0; i < v1.E_Ignore_9.size(); i++){
                    EList << v1.E_Ignore_9[i]
                  }
                }
                EList << v2.number
                v1.E_Ignore_9 = EList
                AList = []
                if(v2.A_Ignore_9.size() > 0){
                  for(i = 0; i < v2.A_Ignore_9.size(); i++){
                    AList << v2.A_Ignore_9[i]
                  }
                }
                AList << v1.number
                v2.A_Ignore_9 = AList
                if(g.getEdge(v1, v2) != null){
                  g.removeEdge(g.getEdge(v1, v2))
                }
            }
            if(v1.curNeighbors.contains(v2.number)){// existing tie
              a.addEvent("SelectedForTieUpdate", ["round":"9", "ego.number":v1.number, "alter.number":v2.number, "status":"Existing tie"])
              //Record bot maintained
                a.addEvent("BotTieMaintained", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
                println("Bot #" + v1.number + " maintained a tie with player #" + v2.number)
                maintainCount += 1
                v1.maintainN += 1
                v2.nMaintain += 1
                EList = []
                if(v1.E_Maintain_9.size() > 0){
                  for(i = 0; i < v1.E_Maintain_9.size(); i++){
                    EList << v1.E_Maintain_9[i]
                  }
                }
                EList << v2.number
                v1.E_Maintain_9 = EList
                AList = []
                if(v2.A_Maintain_9.size() > 0){
                  for(i = 0; i < v2.A_Maintain_9.size(); i++){
                    AList << v2.A_Maintain_9[i]
                  }
                }
                AList << v1.number
                v2.A_Maintain_9 = AList
            }
            //Update counter
            counter += 1
          }
          //If alter is not a bot and the inverse tie has not already been selected, the bot will defer to the player
          if(!v2.updatedThisRound.contains(v1.number) && v2.bot == false){
            updated = []
            if(v2.updatedThisRound.size() > 0){
              for(i = 0; i < v2.updatedThisRound.size(); i++){
                updated << v2.updatedThisRound[i]
              }
            }
            updated << v1.number
            v2.updatedThisRound = updated
            //Record selection
            println("Bot " + v1.number + " deferred to player " + v2.number)
            //println("Selected for tie: " + v2.number + " and " + v1.number)
            if(!v2.curNeighbors.contains(v1.number) && !v2.alterHist.contains(v1.number)){// new, no history
              a.addEvent("BotDeferred", ["round":"9", "bot.number":v1.number, "player.number":v2.number, "status":"New - no history"])
            //  a.addEvent("SelectedForTieUpdate", ["round":"1", "ego.number":v2.number, "alter.number":v1.number, "status":"New - no history"])
            }
            if(!v2.curNeighbors.contains(v1.number) && v2.alterHist.contains(v1.number)){// new, have history
              a.addEvent("BotDeferred", ["round":"9", "bot.number":v1.number, "player.number":v2.number, "status":"New - have history"])
            //  a.addEvent("SelectedForTieUpdate", ["round":"1", "ego.number":v2.number, "alter.number":v1.number, "status":"New - have history"])
            }
            if(v2.curNeighbors.contains(v1.number)){// existing tie
              a.addEvent("BotDeferred", ["round":"9", "bot.number":v1.number, "player.number":v2.number, "status":"Existing tie"])
            //  a.addEvent("SelectedForTieUpdate", ["round":"1", "ego.number":v2.number, "alter.number":v1.number, "status":"Existing tie"])
            }
            //Update counter
            counter += 1
          }
        }
        //Active ego players and AI (testing) for whom the directed tie with alter has not been selected will be updated
        if(v1.bot == false && !v1.updatedThisRound.contains(v2.number)){
          updated = []
          if(v1.updatedThisRound.size() > 0){
            for(i = 0; i < v1.updatedThisRound.size(); i++){
              updated << v1.updatedThisRound[i]
            }
          }
          updated << v2.number
          v1.updatedThisRound = updated
          //Record selection
          println("Selected for tie: " + v1.number + " and " + v2.number)
          if(!v1.curNeighbors.contains(v2.number) && !v1.alterHist.contains(v2.number)){// new, no history
            a.addEvent("SelectedForTieUpdate", ["round":"9", "ego.number":v1.number, "alter.number":v2.number, "status":"New - no history"])
          }
          if(!v1.curNeighbors.contains(v2.number) && v1.alterHist.contains(v2.number)){// new, have history
            a.addEvent("SelectedForTieUpdate", ["round":"9", "ego.number":v1.number, "alter.number":v2.number, "status":"New - have history"])
          }
          if(v1.curNeighbors.contains(v2.number)){// existing tie
            a.addEvent("SelectedForTieUpdate", ["round":"9", "ego.number":v1.number, "alter.number":v2.number, "status":"Existing tie"])
          }
          //Update counter
          counter += 1
        }
      }
    }
  }

  //All tie decision functions have been moved to the file "TieDecisionsFns.groovy"
    //Function name depends on the type of decision and round
    //Decision results include a recursive function to add subsequent decisions after the first

  //Start everyone with at least one choice to make on their first choice
  g.V.filter{(it.goodplayer || it.ai==1) && it.updatedThisRound.size() > 0}.each{v1->
    v1.updatedIndex = 0
    if(!v1.curNeighbors.contains(v1.updatedThisRound[0]) && !v1.alterHist.contains(v1.updatedThisRound[0])){//new, no history
      choiceNewTie_Rnd9(v1.number, v1.updatedThisRound[0])
    }
    if(!v1.curNeighbors.contains(v1.updatedThisRound[0]) && v1.alterHist.contains(v1.updatedThisRound[0])){//new, have history
      choiceNewTie_wHist_Rnd9(v1.number, v1.updatedThisRound[0])
    }
    if(v1.curNeighbors.contains(v1.updatedThisRound[0])){//existing tie
      choiceExistingTie_Rnd9(v1.number, v1.updatedThisRound[0])
    }
  }
}

newNetworkTieDecision9.done = {
  println("newNetworkTieDecision9.done -- Counter: " + counter)
  networkTieDone9.start()
}

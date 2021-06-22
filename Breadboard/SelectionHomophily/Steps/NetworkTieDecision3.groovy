networkTieDecision3 = stepFactory.createStep("NetworkTieDecision3")

networkTieDecision3.run = {
  println "networkTieDecision3.run -- Counter: " + counter
  a.addEvent("StepStart", ["step":"networkTieDecision3", "counter":counter])

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Network_Tie_Wait", "3")
  }

  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    v.selectedfortie = 0
    v.private.selectedfortie = 0
  }

  g.E.each{ e->
    e.selectedfortie = 0
    e.private(e.getVertex(Direction.IN), ["selectedfortie":0])
    e.private(e.getVertex(Direction.OUT), ["selectedfortie":0])
  }

  if(counter < choiceCount){
    g.V.shuffle.filter{it.goodplayer || it.ai==1}.next(1).each{ v1->
      g.V.shuffle.filter{(it.goodplayer || it.ai==1) && it.number!=v1.number}.next(1).each{ v2->
        if(v1.updatedThisRound.contains(v2.number)){
          println "Re-update attempt squashed: " + v1.number + " -> " + v2.number
        }
        if(!v1.updatedThisRound.contains(v2.number)){
          updated = []
          if(v1.updatedThisRound.size() > 0){
            for(i = 0; i < v1.updatedThisRound.size(); i++){
              updated << v1.updatedThisRound[i]
            }
          }
          updated << v2.number
          v1.updatedThisRound = updated
          println "Selected for tie: " + v1.number + " and " + v2.number
          if(g.hasEdge(v1, v2) == true){
            a.addEvent("ExistingTieRandomlyPicked", ["ego.number":v1.number, "alter.number":v2.number])
            println "Existing tie choice between: " + v1.number + " and " + v2.number
            v1.private.selectedfortie = 1
            v2.selectedfortie = 2
            curEdge = g.getEdge(v1, v2)
            curEdge.private(v1, ["selectedfortie":1])
            if(v1.bot == true){
              maintainCount += 1
              a.addEvent("BotTieMaintained", ["ego.number":v1.number, "alter.number":v2.number])
              println "Bot #" + v1.number + " maintained a tie with player #" + v2.number
              v1.private.selectedfortie = 0
              v2.selectedfortie = 0
              curEdge.private(v1, ["selectedfortie":0])
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_3.size() > 0){
                for(i = 0; i < v1.E_Maintain_3.size(); i++){
                  EList << v1.E_Maintain_3[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_3 = EList
              AList = []
              if(v2.A_Maintain_3.size() > 0){
                for(i = 0; i < v2.A_Maintain_3.size(); i++){
                  AList << v2.A_Maintain_3[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_3 = AList
              counter += 1
            }
            if(v1.ai != null && v1.ai==1 && v1.bot == false){
              randNum = r.nextDouble()
              if(randNum < connectivity){
                maintainCount += 1
                a.addEvent("AITieMaintained", ["ego.number":v1.number, "alter.number":v2.number])
                println "AI #" + v1.number + " maintained a tie with player #" + v2.number
                v1.private.selectedfortie = 0
                v2.selectedfortie = 0
                curEdge.private(v1, ["selectedfortie":0])
                v1.maintainN += 1
                v2.nMaintain += 1
                EList = []
                if(v1.E_Maintain_3.size() > 0){
                  for(i = 0; i < v1.E_Maintain_3.size(); i++){
                    EList << v1.E_Maintain_3[i]
                  }
                }
                EList << v2.number
                v1.E_Maintain_3 = EList
                AList = []
                if(v2.A_Maintain_3.size() > 0){
                  for(i = 0; i < v2.A_Maintain_3.size(); i++){
                    AList << v2.A_Maintain_3[i]
                  }
                }
                AList << v1.number
                v2.A_Maintain_3 = AList
                v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                counter += 1
              }
              if(randNum > connectivity){
                dissolveCount += 1
                a.addEvent("AITieDissolved", ["ego.number":v1.number, "alter.number":v2.number])
                println "AI #" + v1.number + " dissolved a tie with player #" + v2.number
                v1.private.selectedfortie = 0
                v2.selectedfortie = 0
                curEdge.private(v1, ["selectedfortie":0])
                v1.dissolveN += 1
                v2.nDissolve += 1
                EList = []
                if(v1.E_Dissolve_3.size() > 0){
                  for(i = 0; i < v1.E_Dissolve_3.size(); i++){
                    EList << v1.E_Dissolve_3[i]
                  }
                }
                EList << v2.number
                v1.E_Dissolve_3 = EList
                AList = []
                if(v2.A_Dissolve_3.size() > 0){
                  for(i = 0; i < v2.A_Dissolve_3.size(); i++){
                    AList << v2.A_Dissolve_3[i]
                  }
                }
                AList << v1.number
                v2.A_Dissolve_3 = AList
                if (curEdge != null) {
                  g.removeEdge(curEdge)
                }
                v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                counter += 1
              }
            }
            if(v1.bot == false && v1.ai == null){
              for(i = 0; i < v1.History_Ref.size(); i++) {
                if(v1.History_Ref[i] == v2.number){
                  histIndex = i
                }
              }
              if(uncertainty){
                histLength = 0
                if(v1.History_Rnd1[histIndex] > 0 && v1.History_Rnd1[histIndex] < 8){histLength += 1}
                if(v1.History_Rnd2[histIndex] > 0 && v1.History_Rnd2[histIndex] < 8){histLength += 1}
                if(v1.History_Rnd3[histIndex] > 0 && v1.History_Rnd3[histIndex] < 8){histLength += 1}
                v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "3", v2.number, histLength)
                v1.text += """<audio autoplay src="https://raw.githubusercontent.com/Matt-Sweitzer/matt-sweitzer.github.io/master/misc/beep-09.mp3"></audio>""".toString()
                qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic4_Q, Topic4_A) + "</strong>. We are interested in what you might think <strong>" + v2.number.toString() + "</strong> would say about this topic. <br> <br> <br> If you had to guess what <strong>" + v2.number.toString() + "</strong> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
                qtext2 = "How confident are you in your assessment of <strong>" + v2.number.toString() + "</strong>’s opinion on the topic of <strong>" + getTopic(Topic4_Q) + "</strong>?"
                qTopics = []
                qAAtt = []
                qEAtt = []
                if(v1.History_Rnd1[histIndex] != 0){
                  qTopics << getTopic(Topic1_Q)
                  qAAtt << attConvert(v1.History_Rnd1[histIndex])
                  qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
                }
                if(v1.History_Rnd2[histIndex] != 0){
                  qTopics << getTopic(Topic2_Q)
                  qAAtt << attConvert(v1.History_Rnd2[histIndex])
                  qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
                }
                if(v1.History_Rnd3[histIndex] != 0){
                  qTopics << getTopic(Topic3_Q)
                  qAAtt << attConvert(v1.History_Rnd3[histIndex])
                  qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
                }
                qUncer_Proj = [id: "qUncer_Proj",
                      name: "qUncer_Proj",
                      header: "",
                      text: qtext,
                      qtype: "radio",
                      Round: "3",
                      ANumber: v2.number.toString(),
                      HistLength: histLength,
                      options: [
                        [value: "7", text: "Strongly Agree"],
                        [value: "6", text: "Agree"],
                        [value: "5", text: "Somewhat Agree"],
                        [value: "4", text: "Neither Agree Nor Disagree"],
                        [value: "3", text: "Somewhat Disagree"],
                        [value: "2", text: "Disagree"],
                        [value: "1", text: "Strongly Disagree"]
                      ]]
                a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
                qUncer_Conf1 = [id: "qUncer_Conf1",
                      name: "qUncer_Conf1",
                      header: "",
                      text: qtext2,
                      qtype: "radio",
                      Round: "3",
                      ANumber: v2.number.toString(),
                      Topics: qTopics,
                      A_Attitudes: qAAtt,
                      E_Attitudes: qEAtt,
                      options: [
                        [value: "7", text: "Very Confident"],
                        [value: "6", text: ""],
                        [value: "5", text: ""],
                        [value: "4", text: "Somewhat Confident"],
                        [value: "3", text: ""],
                        [value: "2", text: ""],
                        [value: "1", text: "Not At All Confident"]
                      ]]
                a.add(v1, createSurveyQuestion(qUncer_Conf1, v1))
                a.add(v1, [
                  name: "Maintain Tie",
                  result:{
                    maintainCount += 1
                    a.addEvent("TieMaintained", ["ego.number":v1.number, "alter.number":v2.number])
                    println "Player #" + v1.number + " maintained a tie with player #" + v2.number
                    v1.private.selectedfortie = 0
                    v2.selectedfortie = 0
                    curEdge.private(v1, ["selectedfortie":0])
                    v1.maintainN += 1
                    v2.nMaintain += 1
                    EList = []
                    if(v1.E_Maintain_3.size() > 0){
                      for(i = 0; i < v1.E_Maintain_3.size(); i++){
                        EList << v1.E_Maintain_3[i]
                      }
                    }
                    EList << v2.number
                    v1.E_Maintain_3 = EList
                    AList = []
                    if(v2.A_Maintain_3.size() > 0){
                      for(i = 0; i < v2.A_Maintain_3.size(); i++){
                        AList << v2.A_Maintain_3[i]
                      }
                    }
                    AList << v1.number
                    v2.A_Maintain_3 = AList
                    v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                    counter += 1
                  }, class: "btn btn-custom1 btn-lg btn-block"], [
                  name: "Dissolve Tie",
                  result:{
                    dissolveCount += 1
                    a.addEvent("TieDissolved", ["ego.number":v1.number, "alter.number":v2.number])
                    println "Player #" + v1.number + " dissolved a tie with player #" + v2.number
                    v1.private.selectedfortie = 0
                    v2.selectedfortie = 0
                    curEdge.private(v1, ["selectedfortie":0])
                    v1.dissolveN += 1
                    v2.nDissolve += 1
                    EList = []
                    if(v1.E_Dissolve_3.size() > 0){
                      for(i = 0; i < v1.E_Dissolve_3.size(); i++){
                        EList << v1.E_Dissolve_3[i]
                      }
                    }
                    EList << v2.number
                    v1.E_Dissolve_3 = EList
                    AList = []
                    if(v2.A_Dissolve_3.size() > 0){
                      for(i = 0; i < v2.A_Dissolve_3.size(); i++){
                        AList << v2.A_Dissolve_3[i]
                      }
                    }
                    AList << v1.number
                    v2.A_Dissolve_3 = AList
                    if (curEdge != null) {
                      g.removeEdge(curEdge)
                    }
                    v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                    counter += 1
                  }, class: "btn btn-custom1 btn-lg btn-block"]
                )
              }
              if(! uncertainty){
                v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "3", v2.number)
                v1.text += """<audio autoplay src="https://raw.githubusercontent.com/Matt-Sweitzer/matt-sweitzer.github.io/master/misc/beep-09.mp3"></audio>""".toString()
                rowCount = 0
                if(v1.History_Rnd1[histIndex] != 0){
                  rowCount += 1
                  if(rowCount == 1){
                    v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[histIndex]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                    rowCount += 1
                  }
                  if(rowCount == 3){
                    v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[histIndex]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                    rowCount = 0
                  }
                }
                if(v1.History_Rnd2[histIndex] != 0){
                  rowCount += 1
                  if(rowCount == 1){
                    v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[histIndex]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                    rowCount += 1
                  }
                  if(rowCount == 3){
                    v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[histIndex]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                    rowCount = 0
                  }
                }
                if(v1.History_Rnd3[histIndex] != 0){
                  rowCount += 1
                  if(rowCount == 1){
                    v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[histIndex]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                    rowCount += 1
                  }
                  if(rowCount == 3){
                    v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[histIndex]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                    rowCount = 0
                  }
                }
                a.add(v1, [
                  name: "Maintain Tie",
                  result:{
                    maintainCount += 1
                    a.addEvent("TieMaintained", ["ego.number":v1.number, "alter.number":v2.number])
                    println "Player #" + v1.number + " maintained a tie with player #" + v2.number
                    v1.private.selectedfortie = 0
                    v2.selectedfortie = 0
                    curEdge.private(v1, ["selectedfortie":0])
                    v1.maintainN += 1
                    v2.nMaintain += 1
                    EList = []
                    if(v1.E_Maintain_3.size() > 0){
                      for(i = 0; i < v1.E_Maintain_3.size(); i++){
                        EList << v1.E_Maintain_3[i]
                      }
                    }
                    EList << v2.number
                    v1.E_Maintain_3 = EList
                    AList = []
                    if(v2.A_Maintain_3.size() > 0){
                      for(i = 0; i < v2.A_Maintain_3.size(); i++){
                        AList << v2.A_Maintain_3[i]
                      }
                    }
                    AList << v1.number
                    v2.A_Maintain_3 = AList
                    v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                    counter += 1
                  }, class: "btn btn-custom1 btn-lg btn-block"], [
                  name: "Dissolve Tie",
                  result:{
                    dissolveCount += 1
                    a.addEvent("TieDissolved", ["ego.number":v1.number, "alter.number":v2.number])
                    println "Player #" + v1.number + " dissolved a tie with player #" + v2.number
                    v1.private.selectedfortie = 0
                    v2.selectedfortie = 0
                    curEdge.private(v1, ["selectedfortie":0])
                    v1.dissolveN += 1
                    v2.nDissolve += 1
                    EList = []
                    if(v1.E_Dissolve_3.size() > 0){
                      for(i = 0; i < v1.E_Dissolve_3.size(); i++){
                        EList << v1.E_Dissolve_3[i]
                      }
                    }
                    EList << v2.number
                    v1.E_Dissolve_3 = EList
                    AList = []
                    if(v2.A_Dissolve_3.size() > 0){
                      for(i = 0; i < v2.A_Dissolve_3.size(); i++){
                        AList << v2.A_Dissolve_3[i]
                      }
                    }
                    AList << v1.number
                    v2.A_Dissolve_3 = AList
                    if (curEdge != null) {
                      g.removeEdge(curEdge)
                    }
                    v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                    counter += 1
                  }, class: "btn btn-custom1 btn-lg btn-block"]
                )
              }
            }
          }
          if(g.hasEdge(v1, v2) == false){
            v1.private.selectedfortie = 3
            v2.selectedfortie = 4
            curEdge = g.getEdge(v1, v2)
            if(curEdge == null){
              g.addEdge(v1, v2, "connected")
              curEdge = g.getEdge(v1, v2)
            }
            curEdge.private(v1, ["selectedfortie":2])
            if(v1.alterHist.contains(v2.number)){ //new, but have history
              a.addEvent("NewTieWHistoryRandomlyPicked", ["ego.number":v1.number, "alter.number":v2.number])
              println "New tie choice w/ history between: " + v1.number + " and " + v2.number
              for(i = 0; i < v1.History_Ref.size(); i++) {
                if(v1.History_Ref[i] == v2.number){
                  histIndex = i
                }
              }
              allHist = []
              for(i = 0; i < v1.History_Rnd3.size(); i++) {
                if(i != histIndex){
                  allHist << v1.History_Rnd3[i]
                }
                if(i == histIndex){
                  allHist << getAttitudeN(Topic3_Q, Topic3_A, v2).toInteger()
                }
              }
              v1.History_Rnd3 = allHist
              if(v1.bot == true){
                ignoreCount += 1
                a.addEvent("BotTieIgnored", ["ego.number":v1.number, "alter.number":v2.number])
                println "Bot #" + v1.number + " ignored a tie with player #" + v2.number
                v1.private.selectedfortie = 0
                v2.selectedfortie = 0
                curEdge.private(v1, ["selectedfortie":0])
                v1.ignoreN += 1
                v2.nIgnore += 1
                EList = []
                if(v1.E_Ignore_3.size() > 0){
                  for(i = 0; i < v1.E_Ignore_3.size(); i++){
                    EList << v1.E_Ignore_3[i]
                  }
                }
                EList << v2.number
                v1.E_Ignore_3 = EList
                AList = []
                if(v2.A_Ignore_3.size() > 0){
                  for(i = 0; i < v2.A_Ignore_3.size(); i++){
                    AList << v2.A_Ignore_3[i]
                  }
                }
                AList << v1.number
                v2.A_Ignore_3 = AList
                if (curEdge != null) {
                  g.removeEdge(curEdge)
                }
                counter += 1
              }
              if(v1.ai != null && v1.ai==1 && v1.bot == false){
                randNum = r.nextDouble()
                if(randNum < connectivity){
                  addCount += 1
                  a.addEvent("AITieAdded", ["ego.number":v1.number, "alter.number":v2.number])
                  println "AI #" + v1.number + " added a tie with player #" + v2.number
                  v1.private.selectedfortie = 0
                  v2.selectedfortie = 0
                  curEdge.private(v1, ["selectedfortie":0])
                  v1.addN += 1
                  v2.nAdd += 1
                  EList = []
                  if(v1.E_Add_3.size() > 0){
                    for(i = 0; i < v1.E_Add_3.size(); i++){
                      EList << v1.E_Add_3[i]
                    }
                  }
                  EList << v2.number
                  v1.E_Add_3 = EList
                  AList = []
                  if(v2.A_Add_3.size() > 0){
                    for(i = 0; i < v2.A_Add_3.size(); i++){
                      AList << v2.A_Add_3[i]
                    }
                  }
                  AList << v1.number
                  v2.A_Add_3 = AList
                  v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                  counter += 1
                }
                if(randNum > connectivity){
                  ignoreCount += 1
                  a.addEvent("AITieIgnored", ["ego.number":v1.number, "alter.number":v2.number])
                  println "AI #" + v1.number + " ignored a tie with player #" + v2.number
                  v1.private.selectedfortie = 0
                  v2.selectedfortie = 0
                  curEdge.private(v1, ["selectedfortie":0])
                  v1.ignoreN += 1
                  v2.nIgnore += 1
                  EList = []
                  if(v1.E_Ignore_3.size() > 0){
                    for(i = 0; i < v1.E_Ignore_3.size(); i++){
                      EList << v1.E_Ignore_3[i]
                    }
                  }
                  EList << v2.number
                  v1.E_Ignore_3 = EList
                  AList = []
                  if(v2.A_Ignore_3.size() > 0){
                    for(i = 0; i < v2.A_Ignore_3.size(); i++){
                      AList << v2.A_Ignore_3[i]
                    }
                  }
                  AList << v1.number
                  v2.A_Ignore_3 = AList
                  if (curEdge != null) {
                    g.removeEdge(curEdge)
                  }
                  v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                  counter += 1
                }
              }
              if(v1.bot == false && v1.ai == null){
                for(i = 0; i < v1.History_Ref.size(); i++) {
                  if(v1.History_Ref[i] == v2.number){
                    histIndex = i
                  }
                }
                if(uncertainty){
                  histLength = 0
                  if(v1.History_Rnd1[histIndex] > 0 && v1.History_Rnd1[histIndex] < 8){histLength += 1}
                  if(v1.History_Rnd2[histIndex] > 0 && v1.History_Rnd2[histIndex] < 8){histLength += 1}
                  v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "3", v2.number, histLength)
                  v1.text += """<audio autoplay src="https://raw.githubusercontent.com/Matt-Sweitzer/matt-sweitzer.github.io/master/misc/beep-09.mp3"></audio>""".toString()
                  qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic4_Q, Topic4_A) + "</strong>. We are interested in what you might think <strong>" + v2.number.toString() + "</strong> would say about this topic. <br> <br> <br> If you had to guess what <strong>" + v2.number.toString() + "</strong> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
                  qtext2 = "How confident are you in your assessment of <strong>" + v2.number.toString() + "</strong>’s opinion on the topic of <strong>" + getTopic(Topic4_Q) + "</strong>?"
                  qTopics = []
                  qAAtt = []
                  qEAtt = []
                  if(v1.History_Rnd1[histIndex] != 0){
                    qTopics << getTopic(Topic1_Q)
                    qAAtt << attConvert(v1.History_Rnd1[histIndex])
                    qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
                  }
                  if(v1.History_Rnd2[histIndex] != 0){
                    qTopics << getTopic(Topic2_Q)
                    qAAtt << attConvert(v1.History_Rnd2[histIndex])
                    qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
                  }
                  if(v1.History_Rnd3[histIndex] != 0){
                    qTopics << getTopic(Topic3_Q)
                    qAAtt << attConvert(v1.History_Rnd3[histIndex])
                    qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
                  }
                  qUncer_Proj = [id: "qUncer_Proj",
                        name: "qUncer_Proj",
                        header: "",
                        text: qtext,
                        qtype: "radio",
                        Round: "3",
                        ANumber: v2.number.toString(),
                        HistLength: histLength,
                        options: [
                          [value: "7", text: "Strongly Agree"],
                          [value: "6", text: "Agree"],
                          [value: "5", text: "Somewhat Agree"],
                          [value: "4", text: "Neither Agree Nor Disagree"],
                          [value: "3", text: "Somewhat Disagree"],
                          [value: "2", text: "Disagree"],
                          [value: "1", text: "Strongly Disagree"]
                        ]]
                  a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
                  qUncer_Conf2 = [id: "qUncer_Conf2",
                        name: "qUncer_Conf2",
                        header: "",
                        text: qtext2,
                        qtype: "radio",
                        Round: "3",
                        ANumber: v2.number.toString(),
                        Topics: qTopics,
                        A_Attitudes: qAAtt,
                        E_Attitudes: qEAtt,
                        options: [
                          [value: "7", text: "Very Confident"],
                          [value: "6", text: ""],
                          [value: "5", text: ""],
                          [value: "4", text: "Somewhat Confident"],
                          [value: "3", text: ""],
                          [value: "2", text: ""],
                          [value: "1", text: "Not At All Confident"]
                        ]]
                  a.add(v1, createSurveyQuestion(qUncer_Conf2, v1))
                  a.add(v1, [
                    name: "Add Tie",
                    result:{
                      addCount += 1
                      a.addEvent("TieAdded", ["ego.number":v1.number, "alter.number":v2.number])
                      println "Player #" + v1.number + " added a tie with player #" + v2.number
                      v1.private.selectedfortie = 0
                      v2.selectedfortie = 0
                      curEdge.private(v1, ["selectedfortie":0])
                      v1.addN += 1
                      v2.nAdd += 1
                      EList = []
                      if(v1.E_Add_3.size() > 0){
                        for(i = 0; i < v1.E_Add_3.size(); i++){
                          EList << v1.E_Add_3[i]
                        }
                      }
                      EList << v2.number
                      v1.E_Add_3 = EList
                      AList = []
                      if(v2.A_Add_3.size() > 0){
                        for(i = 0; i < v2.A_Add_3.size(); i++){
                          AList << v2.A_Add_3[i]
                        }
                      }
                      AList << v1.number
                      v2.A_Add_3 = AList
                      v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                      counter += 1
                    }, class: "btn btn-custom1 btn-lg btn-block"], [
                    name: "Ignore Tie",
                    result:{
                      ignoreCount += 1
                      a.addEvent("TieIgnored", ["ego.number":v1.number, "alter.number":v2.number])
                      println "Player #" + v1.number + " ignored a tie with player #" + v2.number
                      v1.private.selectedfortie = 0
                      v2.selectedfortie = 0
                      curEdge.private(v1, ["selectedfortie":0])
                      v1.ignoreN += 1
                      v2.nIgnore += 1
                      EList = []
                      if(v1.E_Ignore_3.size() > 0){
                        for(i = 0; i < v1.E_Ignore_3.size(); i++){
                          EList << v1.E_Ignore_3[i]
                        }
                      }
                      EList << v2.number
                      v1.E_Ignore_3 = EList
                      AList = []
                      if(v2.A_Ignore_3.size() > 0){
                        for(i = 0; i < v2.A_Ignore_3.size(); i++){
                          AList << v2.A_Ignore_3[i]
                        }
                      }
                      AList << v1.number
                      v2.A_Ignore_3 = AList
                      if (curEdge != null) {
                        g.removeEdge(curEdge)
                      }
                      v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                      counter += 1
                    }, class: "btn btn-custom1 btn-lg btn-block"]
                  )
                }
                if(! uncertainty){
                  v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "3", v2.number)
                  v1.text += """<audio autoplay src="https://raw.githubusercontent.com/Matt-Sweitzer/matt-sweitzer.github.io/master/misc/beep-09.mp3"></audio>""".toString()
                  rowCount = 0
                  if(v1.History_Rnd1[histIndex] != 0){
                    rowCount += 1
                    if(rowCount == 1){
                      v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[histIndex]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                      rowCount += 1
                    }
                    if(rowCount == 3){
                      v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[histIndex]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                      rowCount = 0
                    }
                  }
                  if(v1.History_Rnd2[histIndex] != 0){
                    rowCount += 1
                    if(rowCount == 1){
                      v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[histIndex]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                      rowCount += 1
                    }
                    if(rowCount == 3){
                      v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[histIndex]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                      rowCount = 0
                    }
                  }
                  if(v1.History_Rnd3[histIndex] != 0){
                    rowCount += 1
                    if(rowCount == 1){
                      v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[histIndex]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                      rowCount += 1
                    }
                    if(rowCount == 3){
                      v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[histIndex]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                      rowCount = 0
                    }
                  }
                  a.add(v1, [
                    name: "Add Tie",
                    result:{
                      addCount += 1
                      a.addEvent("TieAdded", ["ego.number":v1.number, "alter.number":v2.number])
                      println "Player #" + v1.number + " added a tie with player #" + v2.number
                      v1.private.selectedfortie = 0
                      v2.selectedfortie = 0
                      curEdge.private(v1, ["selectedfortie":0])
                      v1.addN += 1
                      v2.nAdd += 1
                      EList = []
                      if(v1.E_Add_3.size() > 0){
                        for(i = 0; i < v1.E_Add_3.size(); i++){
                          EList << v1.E_Add_3[i]
                        }
                      }
                      EList << v2.number
                      v1.E_Add_3 = EList
                      AList = []
                      if(v2.A_Add_3.size() > 0){
                        for(i = 0; i < v2.A_Add_3.size(); i++){
                          AList << v2.A_Add_3[i]
                        }
                      }
                      AList << v1.number
                      v2.A_Add_3 = AList
                      v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                      counter += 1
                    }, class: "btn btn-custom1 btn-lg btn-block"], [
                    name: "Ignore Tie",
                    result:{
                      ignoreCount += 1
                      a.addEvent("TieIgnored", ["ego.number":v1.number, "alter.number":v2.number])
                      println "Player #" + v1.number + " ignored a tie with player #" + v2.number
                      v1.private.selectedfortie = 0
                      v2.selectedfortie = 0
                      curEdge.private(v1, ["selectedfortie":0])
                      v1.ignoreN += 1
                      v2.nIgnore += 1
                      EList = []
                      if(v1.E_Ignore_3.size() > 0){
                        for(i = 0; i < v1.E_Ignore_3.size(); i++){
                          EList << v1.E_Ignore_3[i]
                        }
                      }
                      EList << v2.number
                      v1.E_Ignore_3 = EList
                      AList = []
                      if(v2.A_Ignore_3.size() > 0){
                        for(i = 0; i < v2.A_Ignore_3.size(); i++){
                          AList << v2.A_Ignore_3[i]
                        }
                      }
                      AList << v1.number
                      v2.A_Ignore_3 = AList
                      if (curEdge != null) {
                        g.removeEdge(curEdge)
                      }
                      v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                      counter += 1
                    }, class: "btn btn-custom1 btn-lg btn-block"]
                  )
                }
              }
            }
            if(!v1.alterHist.contains(v2.number)){ //new, no history
              a.addEvent("NewTieRandomlyPicked", ["ego.number":v1.number, "alter.number":v2.number])
              println "New tie choice w/o history between: " + v1.number + " and " + v2.number
              for(i = 0; i < v1.History_Ref.size(); i++) {
                if(v1.History_Ref[i] == v2.number){
                  histIndex = i
                }
              }
              allHist = []
              for(i = 0; i < v1.History_Rnd3.size(); i++) {
                if(i != histIndex){
                  allHist << v1.History_Rnd3[i]
                }
                if(i == histIndex){
                  allHist << getAttitudeN(Topic3_Q, Topic3_A, v2).toInteger()
                }
              }
              v1.History_Rnd3 = allHist
              alterHist = []
              for(i = 0; i < v1.alterHist.size(); i++) {
                alterHist << v1.alterHist[i]
              }
              alterHist << v2.number
              v1.alterHist = alterHist
              if(v1.bot == true){
                ignoreCount += 1
                a.addEvent("BotTieIgnored", ["ego.number":v1.number, "alter.number":v2.number])
                println "Bot #" + v1.number + " ignored a tie with player #" + v2.number
                v1.private.selectedfortie = 0
                v2.selectedfortie = 0
                curEdge.private(v1, ["selectedfortie":0])
                v1.ignoreN += 1
                v2.nIgnore += 1
                EList = []
                if(v1.E_Ignore_3.size() > 0){
                  for(i = 0; i < v1.E_Ignore_3.size(); i++){
                    EList << v1.E_Ignore_3[i]
                  }
                }
                EList << v2.number
                v1.E_Ignore_3 = EList
                AList = []
                if(v2.A_Ignore_3.size() > 0){
                  for(i = 0; i < v2.A_Ignore_3.size(); i++){
                    AList << v2.A_Ignore_3[i]
                  }
                }
                AList << v1.number
                v2.A_Ignore_3 = AList
                if (curEdge != null) {
                  g.removeEdge(curEdge)
                }
                counter += 1
              }
              if(v1.ai != null && v1.ai==1 && v1.bot == false){
                randNum = r.nextDouble()
                if(randNum < connectivity){
                  addCount += 1
                  a.addEvent("AITieAdded", ["ego.number":v1.number, "alter.number":v2.number])
                  println "AI #" + v1.number + " added a tie with player #" + v2.number
                  v1.private.selectedfortie = 0
                  v2.selectedfortie = 0
                  curEdge.private(v1, ["selectedfortie":0])
                  v1.addN += 1
                  v2.nAdd += 1
                  EList = []
                  if(v1.E_Add_3.size() > 0){
                    for(i = 0; i < v1.E_Add_3.size(); i++){
                      EList << v1.E_Add_3[i]
                    }
                  }
                  EList << v2.number
                  v1.E_Add_3 = EList
                  AList = []
                  if(v2.A_Add_3.size() > 0){
                    for(i = 0; i < v2.A_Add_3.size(); i++){
                      AList << v2.A_Add_3[i]
                    }
                  }
                  AList << v1.number
                  v2.A_Add_3 = AList
                  updated = []
                  if(v2.updatedThisRound.size() > 0){
                    for(i = 0; i < v2.updatedThisRound.size(); i++){
                      updated << v2.updatedThisRound[i]
                    }
                  }
                  updated << v1.number
                  v2.updatedThisRound = updated
                  v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                  counter += 1
                }
                if(randNum > connectivity){
                  ignoreCount += 1
                  a.addEvent("AITieIgnored", ["ego.number":v1.number, "alter.number":v2.number])
                  println "AI #" + v1.number + " ignored a tie with player #" + v2.number
                  v1.private.selectedfortie = 0
                  v2.selectedfortie = 0
                  curEdge.private(v1, ["selectedfortie":0])
                  v1.ignoreN += 1
                  v2.nIgnore += 1
                  EList = []
                  if(v1.E_Ignore_3.size() > 0){
                    for(i = 0; i < v1.E_Ignore_3.size(); i++){
                      EList << v1.E_Ignore_3[i]
                    }
                  }
                  EList << v2.number
                  v1.E_Ignore_3 = EList
                  AList = []
                  if(v2.A_Ignore_3.size() > 0){
                    for(i = 0; i < v2.A_Ignore_3.size(); i++){
                      AList << v2.A_Ignore_3[i]
                    }
                  }
                  AList << v1.number
                  v2.A_Ignore_3 = AList
                  if (curEdge != null) {
                    g.removeEdge(curEdge)
                  }
                  v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                  counter += 1
                }
              }
              if(v1.bot == false && v1.ai == null){
                for(i = 0; i < v1.History_Ref.size(); i++) {
                  if(v1.History_Ref[i] == v2.number){
                    histIndex = i
                  }
                }
                if(uncertainty){
                  v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "3", v2.number, "0")
                  v1.text += """<audio autoplay src="https://raw.githubusercontent.com/Matt-Sweitzer/matt-sweitzer.github.io/master/misc/beep-09.mp3"></audio>""".toString()
                  qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic4_Q, Topic4_A) + "</strong>. We are interested in what you might think <strong>" + v2.number.toString() + "</strong> would say about this topic. <br> <br> <br> If you had to guess what <strong>" + v2.number.toString() + "</strong> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
                  qtext2 = "How confident are you in your assessment of <strong>" + v2.number.toString() + "</strong>’s opinion on the topic of <strong>" + getTopic(Topic4_Q) + "</strong>?"
                  qUncer_Proj = [id: "qUncer_Proj",
                        name: "qUncer_Proj",
                        header: "",
                        text: qtext,
                        qtype: "radio",
                        Round: "3",
                        ANumber: v2.number.toString(),
                        HistLength: "0",
                        options: [
                          [value: "7", text: "Strongly Agree"],
                          [value: "6", text: "Agree"],
                          [value: "5", text: "Somewhat Agree"],
                          [value: "4", text: "Neither Agree Nor Disagree"],
                          [value: "3", text: "Somewhat Disagree"],
                          [value: "2", text: "Disagree"],
                          [value: "1", text: "Strongly Disagree"]
                        ]]
                  a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
                  qUncer_Conf3 = [id: "qUncer_Conf3",
                        name: "qUncer_Conf3",
                        header: "",
                        text: qtext2,
                        qtype: "radio",
                        Round: "3",
                        ANumber: v2.number.toString(),
                        CurTopText: getQText(Topic3_Q, Topic3_A),
                        Topics: getTopic(Topic3_Q),
                        A_Attitudes: attConvert(getAttitudeN(Topic3_Q, Topic3_A, v2).toInteger()),
                        E_Attitudes: attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()),
                        options: [
                          [value: "7", text: "Very Confident"],
                          [value: "6", text: ""],
                          [value: "5", text: ""],
                          [value: "4", text: "Somewhat Confident"],
                          [value: "3", text: ""],
                          [value: "2", text: ""],
                          [value: "1", text: "Not At All Confident"]
                        ]]
                  a.add(v1, createSurveyQuestion(qUncer_Conf3, v1))
                  a.add(v1, [
                    name: "Add Tie",
                    result:{
                      addCount += 1
                      a.addEvent("TieAdded", ["ego.number":v1.number, "alter.number":v2.number])
                      println "Player #" + v1.number + " added a tie with player #" + v2.number
                      v1.private.selectedfortie = 0
                      v2.selectedfortie = 0
                      curEdge.private(v1, ["selectedfortie":0])
                      v1.addN += 1
                      v2.nAdd += 1
                      EList = []
                      if(v1.E_Add_3.size() > 0){
                        for(i = 0; i < v1.E_Add_3.size(); i++){
                          EList << v1.E_Add_3[i]
                        }
                      }
                      EList << v2.number
                      v1.E_Add_3 = EList
                      AList = []
                      if(v2.A_Add_3.size() > 0){
                        for(i = 0; i < v2.A_Add_3.size(); i++){
                          AList << v2.A_Add_3[i]
                        }
                      }
                      AList << v1.number
                      v2.A_Add_3 = AList
                      updated = []
                      if(v2.updatedThisRound.size() > 0){
                        for(i = 0; i < v2.updatedThisRound.size(); i++){
                          updated << v2.updatedThisRound[i]
                        }
                      }
                      updated << v1.number
                      v2.updatedThisRound = updated
                      v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                      counter += 1
                    }, class: "btn btn-custom1 btn-lg btn-block"], [
                    name: "Ignore Tie",
                    result:{
                      ignoreCount += 1
                      a.addEvent("TieIgnored", ["ego.number":v1.number, "alter.number":v2.number])
                      println "Player #" + v1.number + " ignored a tie with player #" + v2.number
                      v1.private.selectedfortie = 0
                      v2.selectedfortie = 0
                      curEdge.private(v1, ["selectedfortie":0])
                      v1.ignoreN += 1
                      v2.nIgnore += 1
                      EList = []
                      if(v1.E_Ignore_3.size() > 0){
                        for(i = 0; i < v1.E_Ignore_3.size(); i++){
                          EList << v1.E_Ignore_3[i]
                        }
                      }
                      EList << v2.number
                      v1.E_Ignore_3 = EList
                      AList = []
                      if(v2.A_Ignore_3.size() > 0){
                        for(i = 0; i < v2.A_Ignore_3.size(); i++){
                          AList << v2.A_Ignore_3[i]
                        }
                      }
                      AList << v1.number
                      v2.A_Ignore_3 = AList
                      if (curEdge != null) {
                        g.removeEdge(curEdge)
                      }
                      v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                      counter += 1
                    }, class: "btn btn-custom1 btn-lg btn-block"]
                  )
                }
                if(! uncertainty){
                  v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "3", v2.number, getQText(Topic3_Q, Topic3_A)) + c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v2).toInteger()), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                  v1.text += """<audio autoplay src="https://raw.githubusercontent.com/Matt-Sweitzer/matt-sweitzer.github.io/master/misc/beep-09.mp3"></audio>""".toString()
                  a.add(v1, [
                    name: "Add Tie",
                    result:{
                      addCount += 1
                      a.addEvent("TieAdded", ["ego.number":v1.number, "alter.number":v2.number])
                      println "Player #" + v1.number + " added a tie with player #" + v2.number
                      v1.private.selectedfortie = 0
                      v2.selectedfortie = 0
                      curEdge.private(v1, ["selectedfortie":0])
                      v1.addN += 1
                      v2.nAdd += 1
                      EList = []
                      if(v1.E_Add_3.size() > 0){
                        for(i = 0; i < v1.E_Add_3.size(); i++){
                          EList << v1.E_Add_3[i]
                        }
                      }
                      EList << v2.number
                      v1.E_Add_3 = EList
                      AList = []
                      if(v2.A_Add_3.size() > 0){
                        for(i = 0; i < v2.A_Add_3.size(); i++){
                          AList << v2.A_Add_3[i]
                        }
                      }
                      AList << v1.number
                      v2.A_Add_3 = AList
                      updated = []
                      if(v2.updatedThisRound.size() > 0){
                        for(i = 0; i < v2.updatedThisRound.size(); i++){
                          updated << v2.updatedThisRound[i]
                        }
                      }
                      updated << v1.number
                      v2.updatedThisRound = updated
                      v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                      counter += 1
                    }, class: "btn btn-custom1 btn-lg btn-block"], [
                    name: "Ignore Tie",
                    result:{
                      ignoreCount += 1
                      a.addEvent("TieIgnored", ["ego.number":v1.number, "alter.number":v2.number])
                      println "Player #" + v1.number + " ignored a tie with player #" + v2.number
                      v1.private.selectedfortie = 0
                      v2.selectedfortie = 0
                      curEdge.private(v1, ["selectedfortie":0])
                      v1.ignoreN += 1
                      v2.nIgnore += 1
                      EList = []
                      if(v1.E_Ignore_3.size() > 0){
                        for(i = 0; i < v1.E_Ignore_3.size(); i++){
                          EList << v1.E_Ignore_3[i]
                        }
                      }
                      EList << v2.number
                      v1.E_Ignore_3 = EList
                      AList = []
                      if(v2.A_Ignore_3.size() > 0){
                        for(i = 0; i < v2.A_Ignore_3.size(); i++){
                          AList << v2.A_Ignore_3[i]
                        }
                      }
                      AList << v1.number
                      v2.A_Ignore_3 = AList
                      if (curEdge != null) {
                        g.removeEdge(curEdge)
                      }
                      v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
                      counter += 1
                    }, class: "btn btn-custom1 btn-lg btn-block"]
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

networkTieDecision3.done = {
  println "networkTieDecision3.done -- Counter: " + counter
  if(counter < choiceCount){
    networkTieDecision3.start()
  }
  if(counter >= choiceCount && counter < 9999){
    counter = 10000
    networkTieDone3.start()
  }
}

//Now comes the workhorse part of the tie decision steps
  //The three blocks of code within each round contain functions (called in the tie decision steps), each pertaining to a type of tie choice
  //New tie with no history, new tie with history, and existing tie
  //Each function accomplishes the following:
    // - Print the tie choice assignment to the log file
    // - If a tie is not present currently, add it (for ego network graph)
    // - Change the css styling of the ego node and tie under consideration
    // - Add the uncertainty projection question to the queue if the parameter is true
      // - Retrieve uncertainty data
      // - Add attitude projection of the next topic question
      // - Add projection conficence question
    // - Add the decision to the action queue
      // - Add the appropriate text to describe the tie decision
      // - If new topic, add the current issue to discussion history
    // - Once decision has been made:
      // - Record the decision reached
      // - Remove the CSS styling
      // - Change the text to the default (waiting)
      // - Remove the edge if decision calls for removal
      // - Increment ego's index of updatedThisRound
      // - Check updatedThisRound to see if a new decision should be assigned

//~~//Round  1~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                      111                                                                           //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                      11                                                                           //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|         11                                                                           //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                      11                                                                           //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                     11111                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd1(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic2_Q, Topic2_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic2_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "1",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "1", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "1",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic1_Q, Topic1_A),
              Topics: getTopic(Topic1_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic1_Q, Topic1_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "1", v2.number, getQText(Topic1_Q, Topic1_A)) + c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v2).toInteger()), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd1.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd1[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic1_Q, Topic1_A, v2).toInteger()
              }
            }
            v1.History_Rnd1 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":1, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_1.size() > 0){
                for(i = 0; i < v1.E_Add_1.size(); i++){
                  EList << v1.E_Add_1[i]
                }
              }
              EList << v2.number
              v1.E_Add_1 = EList
              AList = []
              if(v2.A_Add_1.size() > 0){
                for(i = 0; i < v2.A_Add_1.size(); i++){
                  AList << v2.A_Add_1[i]
                }
              }
              AList << v1.number
              v2.A_Add_1 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "1")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":1, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_1.size() > 0){
                for(i = 0; i < v1.E_Ignore_1.size(); i++){
                  EList << v1.E_Ignore_1[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_1 = EList
              AList = []
              if(v2.A_Ignore_1.size() > 0){
                for(i = 0; i < v2.A_Ignore_1.size(); i++){
                  AList << v2.A_Ignore_1[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_1 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "1")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd1(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic2_Q, Topic2_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic2_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "1",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "1", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "1",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "1", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd1.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd1[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic1_Q, Topic1_A, v2).toInteger()
              }
            }
            v1.History_Rnd1 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":1, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_1.size() > 0){
                for(i = 0; i < v1.E_Add_1.size(); i++){
                  EList << v1.E_Add_1[i]
                }
              }
              EList << v2.number
              v1.E_Add_1 = EList
              AList = []
              if(v2.A_Add_1.size() > 0){
                for(i = 0; i < v2.A_Add_1.size(); i++){
                  AList << v2.A_Add_1[i]
                }
              }
              AList << v1.number
              v2.A_Add_1 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "1")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":1, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_1.size() > 0){
                for(i = 0; i < v1.E_Ignore_1.size(); i++){
                  EList << v1.E_Ignore_1[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_1 = EList
              AList = []
              if(v2.A_Ignore_1.size() > 0){
                for(i = 0; i < v2.A_Ignore_1.size(); i++){
                  AList << v2.A_Ignore_1[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_1 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "1")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd1(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic2_Q, Topic2_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic2_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "1",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "1", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "1",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "1", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":1, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_1.size() > 0){
                for(i = 0; i < v1.E_Maintain_1.size(); i++){
                  EList << v1.E_Maintain_1[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_1 = EList
              AList = []
              if(v2.A_Maintain_1.size() > 0){
                for(i = 0; i < v2.A_Maintain_1.size(); i++){
                  AList << v2.A_Maintain_1[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_1 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "1")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":1, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_1.size() > 0){
                for(i = 0; i < v1.E_Dissolve_1.size(); i++){
                  EList << v1.E_Dissolve_1[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_1 = EList
              AList = []
              if(v2.A_Dissolve_1.size() > 0){
                for(i = 0; i < v2.A_Dissolve_1.size(); i++){
                  AList << v2.A_Dissolve_1[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_1 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "1")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd1(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round  2~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                     22222                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                       22                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|       22222                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                    22                                                                             //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                     22222                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd2(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic3_Q, Topic3_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic3_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "2",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "2", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "2",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic2_Q, Topic2_A),
              Topics: getTopic(Topic2_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic2_Q, Topic2_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "2", v2.number, getQText(Topic2_Q, Topic2_A)) + c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v2).toInteger()), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd2.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd2[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic2_Q, Topic2_A, v2).toInteger()
              }
            }
            v1.History_Rnd2 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":2, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_2.size() > 0){
                for(i = 0; i < v1.E_Add_2.size(); i++){
                  EList << v1.E_Add_2[i]
                }
              }
              EList << v2.number
              v1.E_Add_2 = EList
              AList = []
              if(v2.A_Add_2.size() > 0){
                for(i = 0; i < v2.A_Add_2.size(); i++){
                  AList << v2.A_Add_2[i]
                }
              }
              AList << v1.number
              v2.A_Add_2 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "2")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":2, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_2.size() > 0){
                for(i = 0; i < v1.E_Ignore_2.size(); i++){
                  EList << v1.E_Ignore_2[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_2 = EList
              AList = []
              if(v2.A_Ignore_2.size() > 0){
                for(i = 0; i < v2.A_Ignore_2.size(); i++){
                  AList << v2.A_Ignore_2[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_2 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "2")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd2(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic3_Q, Topic3_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic3_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "2",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "2", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "2",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "2", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd2.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd2[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic2_Q, Topic2_A, v2).toInteger()
              }
            }
            v1.History_Rnd2 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":2, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_2.size() > 0){
                for(i = 0; i < v1.E_Add_2.size(); i++){
                  EList << v1.E_Add_2[i]
                }
              }
              EList << v2.number
              v1.E_Add_2 = EList
              AList = []
              if(v2.A_Add_2.size() > 0){
                for(i = 0; i < v2.A_Add_2.size(); i++){
                  AList << v2.A_Add_2[i]
                }
              }
              AList << v1.number
              v2.A_Add_2 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "2")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":2, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_2.size() > 0){
                for(i = 0; i < v1.E_Ignore_2.size(); i++){
                  EList << v1.E_Ignore_2[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_2 = EList
              AList = []
              if(v2.A_Ignore_2.size() > 0){
                for(i = 0; i < v2.A_Ignore_2.size(); i++){
                  AList << v2.A_Ignore_2[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_2 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "2")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd2(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic3_Q, Topic3_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic3_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "2",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "2", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "2",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "2", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":2, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_2.size() > 0){
                for(i = 0; i < v1.E_Maintain_2.size(); i++){
                  EList << v1.E_Maintain_2[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_2 = EList
              AList = []
              if(v2.A_Maintain_2.size() > 0){
                for(i = 0; i < v2.A_Maintain_2.size(); i++){
                  AList << v2.A_Maintain_2[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_2 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "2")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":2, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_2.size() > 0){
                for(i = 0; i < v1.E_Dissolve_2.size(); i++){
                  EList << v1.E_Dissolve_2[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_2 = EList
              AList = []
              if(v2.A_Dissolve_2.size() > 0){
                for(i = 0; i < v2.A_Dissolve_2.size(); i++){
                  AList << v2.A_Dissolve_2[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_2 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "2")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd2(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round  3~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                     33333                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                       33                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|        3333                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                       33                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                     33333                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd3(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic4_Q, Topic4_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic4_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "3",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "3", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "3", v2.number, getQText(Topic3_Q, Topic3_A)) + c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v2).toInteger()), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd3.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd3[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic3_Q, Topic3_A, v2).toInteger()
              }
            }
            v1.History_Rnd3 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":3, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":3, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd3(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic4_Q, Topic4_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic4_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "3",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "3", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "3", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd3.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd3[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic3_Q, Topic3_A, v2).toInteger()
              }
            }
            v1.History_Rnd3 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":3, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":3, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd3(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic4_Q, Topic4_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic4_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "3",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "3", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "3", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":3, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":3, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "3")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd3(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round  4~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                     4  44                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                    4  44                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|       44444                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                       44                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                        44                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd4(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic5_Q, Topic5_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic5_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "4",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "4", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "4",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic4_Q, Topic4_A),
              Topics: getTopic(Topic4_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic4_Q, Topic4_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "4", v2.number, getQText(Topic4_Q, Topic4_A)) + c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v2).toInteger()), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd4.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd4[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic4_Q, Topic4_A, v2).toInteger()
              }
            }
            v1.History_Rnd4 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":4, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_4.size() > 0){
                for(i = 0; i < v1.E_Add_4.size(); i++){
                  EList << v1.E_Add_4[i]
                }
              }
              EList << v2.number
              v1.E_Add_4 = EList
              AList = []
              if(v2.A_Add_4.size() > 0){
                for(i = 0; i < v2.A_Add_4.size(); i++){
                  AList << v2.A_Add_4[i]
                }
              }
              AList << v1.number
              v2.A_Add_4 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "4")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":4, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_4.size() > 0){
                for(i = 0; i < v1.E_Ignore_4.size(); i++){
                  EList << v1.E_Ignore_4[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_4 = EList
              AList = []
              if(v2.A_Ignore_4.size() > 0){
                for(i = 0; i < v2.A_Ignore_4.size(); i++){
                  AList << v2.A_Ignore_4[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_4 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "4")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd4(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic5_Q, Topic5_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic5_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "4",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "4", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "4",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "4", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd4.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd4[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic4_Q, Topic4_A, v2).toInteger()
              }
            }
            v1.History_Rnd4 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":4, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_4.size() > 0){
                for(i = 0; i < v1.E_Add_4.size(); i++){
                  EList << v1.E_Add_4[i]
                }
              }
              EList << v2.number
              v1.E_Add_4 = EList
              AList = []
              if(v2.A_Add_4.size() > 0){
                for(i = 0; i < v2.A_Add_4.size(); i++){
                  AList << v2.A_Add_4[i]
                }
              }
              AList << v1.number
              v2.A_Add_4 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "4")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":4, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_4.size() > 0){
                for(i = 0; i < v1.E_Ignore_4.size(); i++){
                  EList << v1.E_Ignore_4[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_4 = EList
              AList = []
              if(v2.A_Ignore_4.size() > 0){
                for(i = 0; i < v2.A_Ignore_4.size(); i++){
                  AList << v2.A_Ignore_4[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_4 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "4")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd4(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic5_Q, Topic5_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic5_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "4",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "4", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "4",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "4", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":4, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_4.size() > 0){
                for(i = 0; i < v1.E_Maintain_4.size(); i++){
                  EList << v1.E_Maintain_4[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_4 = EList
              AList = []
              if(v2.A_Maintain_4.size() > 0){
                for(i = 0; i < v2.A_Maintain_4.size(); i++){
                  AList << v2.A_Maintain_4[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_4 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "4")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":4, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_4.size() > 0){
                for(i = 0; i < v1.E_Dissolve_4.size(); i++){
                  EList << v1.E_Dissolve_4[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_4 = EList
              AList = []
              if(v2.A_Dissolve_4.size() > 0){
                for(i = 0; i < v2.A_Dissolve_4.size(); i++){
                  AList << v2.A_Dissolve_4[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_4 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "4")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd4(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round  5~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                     55555                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                    55                                                                             //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|       55555                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                       55                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                     55555                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd5(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic6_Q, Topic6_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic6_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "5",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "5", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "5",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic5_Q, Topic5_A),
              Topics: getTopic(Topic5_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic5_Q, Topic5_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "5", v2.number, getQText(Topic5_Q, Topic5_A)) + c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v2).toInteger()), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd5.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd5[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic5_Q, Topic5_A, v2).toInteger()
              }
            }
            v1.History_Rnd5 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":5, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_5.size() > 0){
                for(i = 0; i < v1.E_Add_5.size(); i++){
                  EList << v1.E_Add_5[i]
                }
              }
              EList << v2.number
              v1.E_Add_5 = EList
              AList = []
              if(v2.A_Add_5.size() > 0){
                for(i = 0; i < v2.A_Add_5.size(); i++){
                  AList << v2.A_Add_5[i]
                }
              }
              AList << v1.number
              v2.A_Add_5 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "5")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":5, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_5.size() > 0){
                for(i = 0; i < v1.E_Ignore_5.size(); i++){
                  EList << v1.E_Ignore_5[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_5 = EList
              AList = []
              if(v2.A_Ignore_5.size() > 0){
                for(i = 0; i < v2.A_Ignore_5.size(); i++){
                  AList << v2.A_Ignore_5[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_5 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "5")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd5(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic6_Q, Topic6_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic6_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "5",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "5", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "5",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "5", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd5.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd5[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic5_Q, Topic5_A, v2).toInteger()
              }
            }
            v1.History_Rnd5 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":5, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_5.size() > 0){
                for(i = 0; i < v1.E_Add_5.size(); i++){
                  EList << v1.E_Add_5[i]
                }
              }
              EList << v2.number
              v1.E_Add_5 = EList
              AList = []
              if(v2.A_Add_5.size() > 0){
                for(i = 0; i < v2.A_Add_5.size(); i++){
                  AList << v2.A_Add_5[i]
                }
              }
              AList << v1.number
              v2.A_Add_5 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "5")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":5, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_5.size() > 0){
                for(i = 0; i < v1.E_Ignore_5.size(); i++){
                  EList << v1.E_Ignore_5[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_5 = EList
              AList = []
              if(v2.A_Ignore_5.size() > 0){
                for(i = 0; i < v2.A_Ignore_5.size(); i++){
                  AList << v2.A_Ignore_5[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_5 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "5")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd5(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic6_Q, Topic6_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic6_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "5",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "5", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "5",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "5", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":5, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_5.size() > 0){
                for(i = 0; i < v1.E_Maintain_5.size(); i++){
                  EList << v1.E_Maintain_5[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_5 = EList
              AList = []
              if(v2.A_Maintain_5.size() > 0){
                for(i = 0; i < v2.A_Maintain_5.size(); i++){
                  AList << v2.A_Maintain_5[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_5 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "5")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":5, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_5.size() > 0){
                for(i = 0; i < v1.E_Dissolve_5.size(); i++){
                  EList << v1.E_Dissolve_5[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_5 = EList
              AList = []
              if(v2.A_Dissolve_5.size() > 0){
                for(i = 0; i < v2.A_Dissolve_5.size(); i++){
                  AList << v2.A_Dissolve_5[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_5 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "5")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd5(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round  6~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                     66666                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                    66                                                                             //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|       66666                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                    66  6                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                     66666                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd6(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic7_Q, Topic7_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic7_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "6",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "6", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "6",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic6_Q, Topic6_A),
              Topics: getTopic(Topic6_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic6_Q, Topic6_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "6", v2.number, getQText(Topic6_Q, Topic6_A)) + c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v2).toInteger()), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd6.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd6[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic6_Q, Topic6_A, v2).toInteger()
              }
            }
            v1.History_Rnd6 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":6, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_6.size() > 0){
                for(i = 0; i < v1.E_Add_6.size(); i++){
                  EList << v1.E_Add_6[i]
                }
              }
              EList << v2.number
              v1.E_Add_6 = EList
              AList = []
              if(v2.A_Add_6.size() > 0){
                for(i = 0; i < v2.A_Add_6.size(); i++){
                  AList << v2.A_Add_6[i]
                }
              }
              AList << v1.number
              v2.A_Add_6 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "6")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":6, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_6.size() > 0){
                for(i = 0; i < v1.E_Ignore_6.size(); i++){
                  EList << v1.E_Ignore_6[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_6 = EList
              AList = []
              if(v2.A_Ignore_6.size() > 0){
                for(i = 0; i < v2.A_Ignore_6.size(); i++){
                  AList << v2.A_Ignore_6[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_6 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "6")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd6(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic7_Q, Topic7_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic7_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "6",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "6", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "6",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "6", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd6.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd6[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic6_Q, Topic6_A, v2).toInteger()
              }
            }
            v1.History_Rnd6 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":6, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_6.size() > 0){
                for(i = 0; i < v1.E_Add_6.size(); i++){
                  EList << v1.E_Add_6[i]
                }
              }
              EList << v2.number
              v1.E_Add_6 = EList
              AList = []
              if(v2.A_Add_6.size() > 0){
                for(i = 0; i < v2.A_Add_6.size(); i++){
                  AList << v2.A_Add_6[i]
                }
              }
              AList << v1.number
              v2.A_Add_6 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "6")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":6, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_6.size() > 0){
                for(i = 0; i < v1.E_Ignore_6.size(); i++){
                  EList << v1.E_Ignore_6[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_6 = EList
              AList = []
              if(v2.A_Ignore_6.size() > 0){
                for(i = 0; i < v2.A_Ignore_6.size(); i++){
                  AList << v2.A_Ignore_6[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_6 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "6")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd6(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic7_Q, Topic7_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic7_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "6",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "6", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "6",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "6", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":6, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_6.size() > 0){
                for(i = 0; i < v1.E_Maintain_6.size(); i++){
                  EList << v1.E_Maintain_6[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_6 = EList
              AList = []
              if(v2.A_Maintain_6.size() > 0){
                for(i = 0; i < v2.A_Maintain_6.size(); i++){
                  AList << v2.A_Maintain_6[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_6 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "6")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":6, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_6.size() > 0){
                for(i = 0; i < v1.E_Dissolve_6.size(); i++){
                  EList << v1.E_Dissolve_6[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_6 = EList
              AList = []
              if(v2.A_Dissolve_6.size() > 0){
                for(i = 0; i < v2.A_Dissolve_6.size(); i++){
                  AList << v2.A_Dissolve_6[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_6 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "6")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd6(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round  7~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                     77777                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                       77                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|          77                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                      77                                                                           //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                      77                                                                            //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd7(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic8_Q, Topic8_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic8_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "7",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "7", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "7",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic7_Q, Topic7_A),
              Topics: getTopic(Topic7_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic7_Q, Topic7_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "7", v2.number, getQText(Topic7_Q, Topic7_A)) + c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v2).toInteger()), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd7.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd7[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic7_Q, Topic7_A, v2).toInteger()
              }
            }
            v1.History_Rnd7 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":7, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_7.size() > 0){
                for(i = 0; i < v1.E_Add_7.size(); i++){
                  EList << v1.E_Add_7[i]
                }
              }
              EList << v2.number
              v1.E_Add_7 = EList
              AList = []
              if(v2.A_Add_7.size() > 0){
                for(i = 0; i < v2.A_Add_7.size(); i++){
                  AList << v2.A_Add_7[i]
                }
              }
              AList << v1.number
              v2.A_Add_7 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "7")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":7, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_7.size() > 0){
                for(i = 0; i < v1.E_Ignore_7.size(); i++){
                  EList << v1.E_Ignore_7[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_7 = EList
              AList = []
              if(v2.A_Ignore_7.size() > 0){
                for(i = 0; i < v2.A_Ignore_7.size(); i++){
                  AList << v2.A_Ignore_7[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_7 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "7")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd7(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic7_Q)
              qAAtt << attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic8_Q, Topic8_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic8_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "7",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "7", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "7",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "7", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd7.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd7[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic7_Q, Topic7_A, v2).toInteger()
              }
            }
            v1.History_Rnd7 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":7, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_7.size() > 0){
                for(i = 0; i < v1.E_Add_7.size(); i++){
                  EList << v1.E_Add_7[i]
                }
              }
              EList << v2.number
              v1.E_Add_7 = EList
              AList = []
              if(v2.A_Add_7.size() > 0){
                for(i = 0; i < v2.A_Add_7.size(); i++){
                  AList << v2.A_Add_7[i]
                }
              }
              AList << v1.number
              v2.A_Add_7 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "7")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":7, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_7.size() > 0){
                for(i = 0; i < v1.E_Ignore_7.size(); i++){
                  EList << v1.E_Ignore_7[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_7 = EList
              AList = []
              if(v2.A_Ignore_7.size() > 0){
                for(i = 0; i < v2.A_Ignore_7.size(); i++){
                  AList << v2.A_Ignore_7[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_7 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "7")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd7(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic7_Q)
              qAAtt << attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic8_Q, Topic8_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic8_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "7",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "7", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "7",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "7", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":7, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_7.size() > 0){
                for(i = 0; i < v1.E_Maintain_7.size(); i++){
                  EList << v1.E_Maintain_7[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_7 = EList
              AList = []
              if(v2.A_Maintain_7.size() > 0){
                for(i = 0; i < v2.A_Maintain_7.size(); i++){
                  AList << v2.A_Maintain_7[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_7 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "7")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":7, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_7.size() > 0){
                for(i = 0; i < v1.E_Dissolve_7.size(); i++){
                  EList << v1.E_Dissolve_7[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_7 = EList
              AList = []
              if(v2.A_Dissolve_7.size() > 0){
                for(i = 0; i < v2.A_Dissolve_7.size(); i++){
                  AList << v2.A_Dissolve_7[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_7 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "7")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd7(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round  8~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                     88888                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                    88  8                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|       88888                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                    88  8                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                     88888                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd8(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic9_Q, Topic9_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic9_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "8",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "8", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "8",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic8_Q, Topic8_A),
              Topics: getTopic(Topic8_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic8_Q, Topic8_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "8", v2.number, getQText(Topic8_Q, Topic8_A)) + c.get("Network_Tie_Table_1", getTopic(Topic8_Q), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v2).toInteger()), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd8.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd8[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic8_Q, Topic8_A, v2).toInteger()
              }
            }
            v1.History_Rnd8 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":8, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_8.size() > 0){
                for(i = 0; i < v1.E_Add_8.size(); i++){
                  EList << v1.E_Add_8[i]
                }
              }
              EList << v2.number
              v1.E_Add_8 = EList
              AList = []
              if(v2.A_Add_8.size() > 0){
                for(i = 0; i < v2.A_Add_8.size(); i++){
                  AList << v2.A_Add_8[i]
                }
              }
              AList << v1.number
              v2.A_Add_8 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "8")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":8, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_8.size() > 0){
                for(i = 0; i < v1.E_Ignore_8.size(); i++){
                  EList << v1.E_Ignore_8[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_8 = EList
              AList = []
              if(v2.A_Ignore_8.size() > 0){
                for(i = 0; i < v2.A_Ignore_8.size(); i++){
                  AList << v2.A_Ignore_8[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_8 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "8")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd8(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic7_Q)
              qAAtt << attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger())
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic8_Q)
              qAAtt << attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic9_Q, Topic9_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic9_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "8",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "8", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "8",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "8", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd8.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd8[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic8_Q, Topic8_A, v2).toInteger()
              }
            }
            v1.History_Rnd8 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":8, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_8.size() > 0){
                for(i = 0; i < v1.E_Add_8.size(); i++){
                  EList << v1.E_Add_8[i]
                }
              }
              EList << v2.number
              v1.E_Add_8 = EList
              AList = []
              if(v2.A_Add_8.size() > 0){
                for(i = 0; i < v2.A_Add_8.size(); i++){
                  AList << v2.A_Add_8[i]
                }
              }
              AList << v1.number
              v2.A_Add_8 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "8")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":8, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_8.size() > 0){
                for(i = 0; i < v1.E_Ignore_8.size(); i++){
                  EList << v1.E_Ignore_8[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_8 = EList
              AList = []
              if(v2.A_Ignore_8.size() > 0){
                for(i = 0; i < v2.A_Ignore_8.size(); i++){
                  AList << v2.A_Ignore_8[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_8 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "8")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd8(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic7_Q)
              qAAtt << attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger())
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic8_Q)
              qAAtt << attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic9_Q, Topic9_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic9_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "8",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "8", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "8",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "8", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":8, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_8.size() > 0){
                for(i = 0; i < v1.E_Maintain_8.size(); i++){
                  EList << v1.E_Maintain_8[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_8 = EList
              AList = []
              if(v2.A_Maintain_8.size() > 0){
                for(i = 0; i < v2.A_Maintain_8.size(); i++){
                  AList << v2.A_Maintain_8[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_8 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "8")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":8, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_8.size() > 0){
                for(i = 0; i < v1.E_Dissolve_8.size(); i++){
                  EList << v1.E_Dissolve_8[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_8 = EList
              AList = []
              if(v2.A_Dissolve_8.size() > 0){
                for(i = 0; i < v2.A_Dissolve_8.size(); i++){
                  AList << v2.A_Dissolve_8[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_8 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "8")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd8(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round  9~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                     99999                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                    9  99                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|       99999                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                       99                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                          RR  RR   OOOO    UUUU   NN    NNN  DDDDD                     99999                                                                          //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd9(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic10_Q, Topic10_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic10_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "9",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "9", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "9",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic9_Q, Topic9_A),
              Topics: getTopic(Topic9_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic9_Q, Topic9_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "9", v2.number, getQText(Topic9_Q, Topic9_A)) + c.get("Network_Tie_Table_1", getTopic(Topic9_Q), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v2).toInteger()), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd9.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd9[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic9_Q, Topic9_A, v2).toInteger()
              }
            }
            v1.History_Rnd9 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_9.size() > 0){
                for(i = 0; i < v1.E_Add_9.size(); i++){
                  EList << v1.E_Add_9[i]
                }
              }
              EList << v2.number
              v1.E_Add_9 = EList
              AList = []
              if(v2.A_Add_9.size() > 0){
                for(i = 0; i < v2.A_Add_9.size(); i++){
                  AList << v2.A_Add_9[i]
                }
              }
              AList << v1.number
              v2.A_Add_9 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "9")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "9")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd9(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic7_Q)
              qAAtt << attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger())
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic8_Q)
              qAAtt << attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger())
            }
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic9_Q)
              qAAtt << attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic10_Q, Topic10_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic10_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "9",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "9", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "9",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "9", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic9_Q), attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic9_Q), attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd9.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd9[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic9_Q, Topic9_A, v2).toInteger()
              }
            }
            v1.History_Rnd9 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_9.size() > 0){
                for(i = 0; i < v1.E_Add_9.size(); i++){
                  EList << v1.E_Add_9[i]
                }
              }
              EList << v2.number
              v1.E_Add_9 = EList
              AList = []
              if(v2.A_Add_9.size() > 0){
                for(i = 0; i < v2.A_Add_9.size(); i++){
                  AList << v2.A_Add_9[i]
                }
              }
              AList << v1.number
              v2.A_Add_9 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "9")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "9")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd9(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic7_Q)
              qAAtt << attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger())
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic8_Q)
              qAAtt << attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger())
            }
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic9_Q)
              qAAtt << attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic10_Q, Topic10_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic10_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "9",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "9", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "9",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "9", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic9_Q), attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic9_Q), attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
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
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "9")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":9, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_9.size() > 0){
                for(i = 0; i < v1.E_Dissolve_9.size(); i++){
                  EList << v1.E_Dissolve_9[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_9 = EList
              AList = []
              if(v2.A_Dissolve_9.size() > 0){
                for(i = 0; i < v2.A_Dissolve_9.size(); i++){
                  AList << v2.A_Dissolve_9[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_9 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "9")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd9(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

//~~//Round 10~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                       RRRRR    OOOO   UU  UU  NNN    NN  DDDDD                      111   000                                                                        //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                       RR  RR  OO  OO  UU  UU  NNNN   NN  DD  DD                      11  0  00                                                                       //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                       RRRR    OO  OO  UU  UU  NN NNN NN  DD  DD       |====|         11  0  00                                                                       //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                       RR RR   OO  OO  UU  UU  NN   NNNN  DD  DD                      11  0  00                                                                       //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                       RR  RR   OOOO    UUUU   NN    NNN  DDDDD                     11111  000                                                                        //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//                                                                                                                                                                                                                      //~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//
//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//~~//

def choiceNewTie_Rnd10(ego, alter){//new tie, no history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, no history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic_HeldOut_Q, Topic_HeldOut_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic_HeldOut_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "10",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "10", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf3 = [id: "qUncer_Conf3",
              name: "qUncer_Conf3",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "10",
              ANumber: v2.number.toString(),
              CurTopText: getQText(Topic10_Q, Topic10_A),
              Topics: getTopic(Topic10_Q),
              A_Attitudes: attConvert(getAttitudeN(Topic10_Q, Topic10_A, v2).toInteger()),
              E_Attitudes: attConvert(getAttitudeN(Topic10_Q, Topic10_A, v1).toInteger()),
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New", "10", v2.number, getQText(Topic10_Q, Topic10_A)) + c.get("Network_Tie_Table_1", getTopic(Topic10_Q), attConvert(getAttitudeN(Topic10_Q, Topic10_A, v2).toInteger()), attConvert(getAttitudeN(Topic10_Q, Topic10_A, v1).toInteger()))
          //If new topic, add the current issue to discussion history
            allHist = []
            for(i = 0; i < v1.History_Rnd10.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd10[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic10_Q, Topic10_A, v2).toInteger()
              }
            }
            v1.History_Rnd10 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":10, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_10.size() > 0){
                for(i = 0; i < v1.E_Add_10.size(); i++){
                  EList << v1.E_Add_10[i]
                }
              }
              EList << v2.number
              v1.E_Add_10 = EList
              AList = []
              if(v2.A_Add_10.size() > 0){
                for(i = 0; i < v2.A_Add_10.size(); i++){
                  AList << v2.A_Add_10[i]
                }
              }
              AList << v1.number
              v2.A_Add_10 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "10")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":10, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_10.size() > 0){
                for(i = 0; i < v1.E_Ignore_10.size(); i++){
                  EList << v1.E_Ignore_10[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_10 = EList
              AList = []
              if(v2.A_Ignore_10.size() > 0){
                for(i = 0; i < v2.A_Ignore_10.size(); i++){
                  AList << v2.A_Ignore_10[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_10 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "10")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceNewTie_wHist_Rnd10(ego, alter){//new tie, some history
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("New tie, some history updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 3
        g.getEdge(v1, v2).private(v1, ["selectedfortie":2])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic7_Q)
              qAAtt << attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger())
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic8_Q)
              qAAtt << attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger())
            }
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic9_Q)
              qAAtt << attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger())
            }
            if(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic10_Q)
              qAAtt << attConvert(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic10_Q, Topic10_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic_HeldOut_Q, Topic_HeldOut_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic_HeldOut_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "10",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "10", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf2 = [id: "qUncer_Conf2",
              name: "qUncer_Conf2",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "10",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_New_wHistory", "10", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic9_Q), attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic9_Q), attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic10_Q), attConvert(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic10_Q, Topic10_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic10_Q), attConvert(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic10_Q, Topic10_A, v1).toInteger()))
                rowCount = 0
              }
            }
          //If new topic, add the current issue to discussion history (double check)
            allHist = []
            for(i = 0; i < v1.History_Rnd10.size(); i++) {
              if(i != v1.History_Ref.indexOf(v2.number)){
                allHist << v1.History_Rnd10[i]
              }
              if(i == v1.History_Ref.indexOf(v2.number)){
                allHist << getAttitudeN(Topic10_Q, Topic10_A, v2).toInteger()
              }
            }
            v1.History_Rnd10 = allHist
          },
          [name: "Add Tie", result:{
            //Record the decision reached
              a.addEvent("TieAdded", ["round.number":10, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " added a tie with player #" + v2.number)
              addCount += 1
              v1.addN += 1
              v2.nAdd += 1
              EList = []
              if(v1.E_Add_10.size() > 0){
                for(i = 0; i < v1.E_Add_10.size(); i++){
                  EList << v1.E_Add_10[i]
                }
              }
              EList << v2.number
              v1.E_Add_10 = EList
              AList = []
              if(v2.A_Add_10.size() > 0){
                for(i = 0; i < v2.A_Add_10.size(); i++){
                  AList << v2.A_Add_10[i]
                }
              }
              AList << v1.number
              v2.A_Add_10 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "10")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Ignore Tie", result:{
            //Record the decision reached
              a.addEvent("TieIgnored", ["round.number":10, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " ignored a tie with player #" + v2.number)
              ignoreCount += 1
              v1.ignoreN += 1
              v2.nIgnore += 1
              EList = []
              if(v1.E_Ignore_10.size() > 0){
                for(i = 0; i < v1.E_Ignore_10.size(); i++){
                  EList << v1.E_Ignore_10[i]
                }
              }
              EList << v2.number
              v1.E_Ignore_10 = EList
              AList = []
              if(v2.A_Ignore_10.size() > 0){
                for(i = 0; i < v2.A_Ignore_10.size(); i++){
                  AList << v2.A_Ignore_10[i]
                }
              }
              AList << v1.number
              v2.A_Ignore_10 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "10")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

def choiceExistingTie_Rnd10(ego, alter){//existing tie
  g.V.filter{it.number==ego}.each{v1->
    g.V.filter{it.number==alter}.each{v2->
      //Print the tie choice assignment to the log file
        println("Existing tie updating between ego: " + v1.number + " and alter: " + v2.number)
      //If a tie is not present currently, add it (for ego network graph)
        if(g.getEdge(v1, v2) == null){
          g.addEdge(v1, v2, "connected")
        }
      //Change the css styling of the ego node and tie under consideration
        v1.private.selectedfortie = 1
        g.getEdge(v1, v2).private(v1, ["selectedfortie":1])
      //Add the uncertainty projection question to the queue if the parameter is true
        if(uncertainty && v1.ai == null){
          //Retrieve the uncertainty data
            histLength = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            if(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)] > 0 && v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)] < 8){histLength += 1}
            qTopics = []
            qAAtt = []
            qEAtt = []
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic1_Q)
              qAAtt << attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger())
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic2_Q)
              qAAtt << attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger())
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic3_Q)
              qAAtt << attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger())
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic4_Q)
              qAAtt << attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger())
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic5_Q)
              qAAtt << attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger())
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic6_Q)
              qAAtt << attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger())
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic7_Q)
              qAAtt << attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger())
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic8_Q)
              qAAtt << attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger())
            }
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic9_Q)
              qAAtt << attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger())
            }
            if(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)] != 0){
              qTopics << getTopic(Topic10_Q)
              qAAtt << attConvert(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)])
              qEAtt << attConvert(getAttitudeN(Topic10_Q, Topic10_A, v1).toInteger())
            }
            qtext = "In the next round, the topic of discussion will be <strong>" + getQText(Topic_HeldOut_Q, Topic_HeldOut_A) + "</strong>. We are interested in what you might think <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> would say about this topic. <br> <br> <br> If you had to guess what <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span> thinks about this topic, where would you place them on the following scale? Consider <em>only their opinion</em> and not whether you agree with them."
            qtext2 = "How confident are you in your assessment of <span style='background-color: #ffdd00;'><strong>&ensp; participant " + v2.number.toString() + " &ensp;</strong></span>’s opinion on the topic of <strong>" + getTopic(Topic_HeldOut_Q) + "</strong>?"
          //Add attitude projection of the next topic question
            qUncer_Proj = [id: "qUncer_Proj",
              name: "qUncer_Proj",
              header: "",
              text: qtext,
              qtype: "radio",
              Round: "10",
              ANumber: v2.number.toString(),
              HistLength: histLength.toString(),
              options: [
                [value: "7", text: "Strongly Agree"],
                [value: "6", text: "Agree"],
                [value: "5", text: "Somewhat Agree"],
                [value: "4", text: "Neither Agree Nor Disagree"],
                [value: "3", text: "Somewhat Disagree"],
                [value: "2", text: "Disagree"],
                [value: "1", text: "Strongly Disagree"]
              ]]
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Uncertain_Head", "10", v2.number, histLength.toString())
            a.add(v1, createSurveyQuestion(qUncer_Proj, v1))
          //Add projection confidence question
            qUncer_Conf1 = [id: "qUncer_Conf1",
              name: "qUncer_Conf1",
              header: "",
              text: qtext2,
              qtype: "radio",
              Round: "10",
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
        }
      //Add the decision to the action queue
        a.add(v1, {
          //Add the appropriate text to describe the tie decision
            v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Existing", "10", v2.number)
            rowCount = 0
            if(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic1_Q), attConvert(v1.History_Rnd1[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic1_Q, Topic1_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic2_Q), attConvert(v1.History_Rnd2[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic2_Q, Topic2_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic3_Q), attConvert(v1.History_Rnd3[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic3_Q, Topic3_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic4_Q), attConvert(v1.History_Rnd4[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic4_Q, Topic4_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic5_Q), attConvert(v1.History_Rnd5[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic5_Q, Topic5_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic6_Q), attConvert(v1.History_Rnd6[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic6_Q, Topic6_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic7_Q), attConvert(v1.History_Rnd7[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic7_Q, Topic7_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic8_Q), attConvert(v1.History_Rnd8[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic8_Q, Topic8_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic9_Q), attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic9_Q), attConvert(v1.History_Rnd9[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic9_Q, Topic9_A, v1).toInteger()))
                rowCount = 0
              }
            }
            if(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)] != 0){
              rowCount += 1
              if(rowCount == 1){
                v1.text += c.get("Network_Tie_Table_1", getTopic(Topic10_Q), attConvert(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic10_Q, Topic10_A, v1).toInteger()))
                rowCount += 1
              }
              if(rowCount == 3){
                v1.text += c.get("Network_Tie_Table_2", getTopic(Topic10_Q), attConvert(v1.History_Rnd10[v1.History_Ref.indexOf(v2.number)]), attConvert(getAttitudeN(Topic10_Q, Topic10_A, v1).toInteger()))
                rowCount = 0
              }
            }
          },
          [name: "Maintain Tie", result:{
            //Record the decision reached
              a.addEvent("TieMaintained", ["round.number":10, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " maintained a tie with player #" + v2.number)
              maintainCount += 1
              v1.maintainN += 1
              v2.nMaintain += 1
              EList = []
              if(v1.E_Maintain_10.size() > 0){
                for(i = 0; i < v1.E_Maintain_10.size(); i++){
                  EList << v1.E_Maintain_10[i]
                }
              }
              EList << v2.number
              v1.E_Maintain_10 = EList
              AList = []
              if(v2.A_Maintain_10.size() > 0){
                for(i = 0; i < v2.A_Maintain_10.size(); i++){
                  AList << v2.A_Maintain_10[i]
                }
              }
              AList << v1.number
              v2.A_Maintain_10 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "10")
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"],
          [name: "Dissolve Tie", result:{
            //Record the decision reached
              a.addEvent("TieDissolved", ["round.number":10, "ego.number":v1.number, "alter.number":v2.number])
              println("Player #" + v1.number + " dissolved a tie with player #" + v2.number)
              dissolveCount += 1
              v1.dissolveN += 1
              v2.nDissolve += 1
              EList = []
              if(v1.E_Dissolve_10.size() > 0){
                for(i = 0; i < v1.E_Dissolve_10.size(); i++){
                  EList << v1.E_Dissolve_10[i]
                }
              }
              EList << v2.number
              v1.E_Dissolve_10 = EList
              AList = []
              if(v2.A_Dissolve_10.size() > 0){
                for(i = 0; i < v2.A_Dissolve_10.size(); i++){
                  AList << v2.A_Dissolve_10[i]
                }
              }
              AList << v1.number
              v2.A_Dissolve_10 = AList
            //Remove the CSS styling
              v1.private.selectedfortie = 0
              if(g.getEdge(v1, v2) != null){
                g.getEdge(v1, v2).private(v1, ["selectedfortie":0])
              }
            //Change the text to the default (waiting)
              v1.text = c.get("Payment", String.format("%.2f", (v1.payment))) + c.get("Network_Tie_Wait", "10")
            //Remove the edge if decision calls for removal
              if (g.getEdge(v1, v2) != null) {
                g.removeEdge(g.getEdge(v1, v2))
              }
            //Increment ego's index of updatedThisRound
              v1.updatedIndex += 1
            //Check updatedThisRound to see if a new decision should be assigned
              if(v1.updatedIndex < v1.updatedThisRound.size()){
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && !v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, no history
                  choiceNewTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(!v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex]) && v1.alterHist.contains(v1.updatedThisRound[v1.updatedIndex])){//new, have history
                  choiceNewTie_wHist_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
                if(v1.curNeighbors.contains(v1.updatedThisRound[v1.updatedIndex])){//existing tie
                  choiceExistingTie_Rnd10(v1.number, v1.updatedThisRound[v1.updatedIndex])
                }
              }
          }, class: "btn btn-custom1 btn-lg btn-block"]
        )
    }
  }
}

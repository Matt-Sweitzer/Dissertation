chatSelection2 = stepFactory.createStep("ChatSelection2")

chatSelection2.run = {
  println "chatSelection2.run"
  a.addEvent("StepStart", ["step":"chatSelection2"])

  a.setDropPlayers(true)

  g.V.filter{it.goodplayer || it.ai==1}.each{ v->
    if(v.chat==1){
      v.selectedfortie = 6
      v.private.selectedfortie = 6
      v.chatChoice = 1
    }
    if(v.chat==2){
      v.selectedfortie = 7
      v.private.selectedfortie = 7
      v.chatChoice = 2
    }
    v.nGreen = 0
    v.nOrange = 0
  }

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    if(v.finalNeighbors.size() > 0){
      for(i = 0; i < v.finalNeighbors.size(); i++){
        g.V.filter{it.number==v.finalNeighbors[i]}.each{ n->
          if(n.chatChoice==1){
            v.nGreen += 1
          }
          if(n.chatChoice==2){
            v.nOrange += 1
          }
        }
      }
    }
  }

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    if(v.chatChoice==1){
      v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Instructions_2_GREEN", v.nGreen, v.nOrange)
      a.add(v, [
        name: "Stay in the GREEN room",
          result:{
            v.chat = 1
            a.addEvent("Stayed_GreenRoom", ["v.number":v.number])
            v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Wait")
          }, class:"btn btn-customg btn-lg btn-block"
        ], [
        name: "Switch to the ORANGE room",
          result:{
            v.chat = 2
            a.addEvent("SwitchedTo_OrangeRoom", ["v.number":v.number])
            v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Wait")
          }, class:"btn btn-customo btn-lg btn-block"
        ]
      )
    }

    if(v.chatChoice==2){
      v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Instructions_2_ORANGE", v.nOrange, v.nGreen)
      a.add(v, [
        name: "Stay in the ORANGE room",
          result:{
            v.chat = 2
            a.addEvent("Stayed_OrangeRoom", ["v.number":v.number])
            v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Wait")
          }, class:"btn btn-customo btn-lg btn-block"
        ], [
        name: "Switch to the GREEN room",
          result:{
            v.chat = 1
            a.addEvent("SwitchedTo_GreenRoom", ["v.number":v.number])
            v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Wait")
          }, class:"btn btn-customg btn-lg btn-block"
        ]
      )
    }

  }
}

chatSelection2.done = {
  println "chatSelection2.done"
  g.V.filter{it.bot==true}.each{ v->
    v.chat = r.nextInt(2)+1
  }
  chatSelection3.start()
}

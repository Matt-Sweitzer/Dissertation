chatSelection1 = stepFactory.createStep("ChatSelection1")

chatSelection1.run = {
  println "chatSelection1.run"
  a.addEvent("StepStart", ["step":"chatSelection1"])

  a.setDropPlayers(true)

  g.V.filter{it.bot==true}.each{ v->
    v.selectedfortie = 5
    v.private.selectedfortie = 5
  }

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    if(v.finalNeighbors.size() > 0){
      for(i = 0; i < v.finalNeighbors.size(); i++){
        g.V.filter{it.number==v.finalNeighbors[i]}.each{ n->
          if(!g.hasEdge(v, n)){
            g.addEdge(v, n, "connected")
          }
        }
      }
    }
  }

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Instructions_1")
    v.selectedfortie = 5
    v.private.selectedfortie = 5
    a.add(v, [
      name: "GREEN room",
        result:{
          v.chat = 1
          a.addEvent("GreenRoomSelected", ["v.number":v.number])
          v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Wait")
        }, class: "btn btn-customg btn-lg btn-block"
      ], [
      name: "ORANGE room",
        result:{
          v.chat = 2
          a.addEvent("OrangeRoomSelected", ["v.number":v.number])
          v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Wait")
      }, class: "btn btn-customo btn-lg btn-block"])
  }
}

chatSelection1.done = {
  println "chatSelection1.done"
  g.V.filter{it.bot==true}.each{ v->
    v.chat = r.nextInt(2)+1
  }
  chatSelection2.start()
}

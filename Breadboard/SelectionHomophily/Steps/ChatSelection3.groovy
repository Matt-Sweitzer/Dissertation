chatSelection3 = stepFactory.createStep("ChatSelection3")

chatSelection3.run = {
  println "chatSelection3.run"
  a.addEvent("StepStart", ["step":"chatSelection3"])

  a.setDropPlayers(false)

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
  }

  g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
    if(MTurk == true){
      if(v.chatChoice==1){
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Instructions_3_GREEN", v.number, "When you are finished, please return to this browser window and submit the HIT on this page. Thank you for participating today!")
        v.text += "<br><div id='countdown'></div>"
      }
      if(v.chatChoice==2){
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Instructions_3_ORANGE", v.number, "When you are finished, please return to this browser window and submit the HIT on this page. Thank you for participating today!")
        v.text += "<br><div id='countdown'></div>"
      }
    }
    if(MTurk == false){
      if(v.chatChoice==1){
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Instructions_3_GREEN", v.number, "When you are finished, please close your browser window. The study administrator will issue your payment electronically. You may contact the study administrators with any questions or concerns at <a href='mailto:selectivitystudy@mattsweitzer.com' target='_blank'>selectivitystudy@mattsweitzer.com</a>. Thank you for participating today!")
      }
      if(v.chatChoice==2){
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Chat_Instructions_3_ORANGE", v.number, "When you are finished, please close your browser window. The study administrator will issue your payment electronically. You may contact the study administrators with any questions or concerns at <a href='mailto:selectivitystudy@mattsweitzer.com' target='_blank'>selectivitystudy@mattsweitzer.com</a>. Thank you for participating today!")
      }
    }
  }


  def timer = new Timer()
  def task = timer.runAfter(60000) {
    g.V.filter{(it.goodplayer && it.bot==false) || it.ai==1}.each{ v->
      v.active = true
      v.finished = true
      if (MTurk == true) {
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Finished_MTurk") + g.getSubmitForm(v, v.payment, "Player ${v.number} finished", amtSandbox, true)
      }
      if (MTurk == false) {
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Finished_RM", v.number, v.payment)
      }
      a.addEvent("PlayerFinished", ["v.id": v.id, "v.number":v.number, "payment":v.payment])
    }
  }
}

chatSelection3.done = {
  println "chatSelection3.done"
}

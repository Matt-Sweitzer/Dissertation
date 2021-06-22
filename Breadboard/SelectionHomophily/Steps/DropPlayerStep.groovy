// Players will be warned after 30 seconds and dropped after 60 seconds
a.setIdleTime(30)

a.setDropPlayerClosure({ v ->
    if(v.consent == false){
      if(v.late == false){
        v.dropped = true
        a.remove(v)
        a.addEvent("PlayerRemoved - No Consent", ["v.id": v.id, "v.number":v.number, "payment":v.payment])
        if(MTurk == true){
          v.text = c.get("Dropped_NoConsent_MTurk")
        }
        if(MTurk == false){
          v.text = c.get("Dropped_NoConsent_RM", v.number)
        }
      }
      if(v.late == true){
        v.dropped = true
        a.remove(v)
        a.addEvent("PlayerRemoved - Joined Late", ["v.id": v.id, "v.number":v.number, "payment":v.payment])
        if (MTurk == true) {
          v.text = c.get("GameStarted_MTurk")
        }
        if (MTurk == false) {
          v.text = c.get("GameStarted_RM", v.number, v.payment)
        }
      }
    }
    if(v.consent == true && v.finished == false){
      v.dropped = true
      if (v.selectionSteps == false) {
        v.goodplayer = false
        a.remove(v)
        a.addEvent("PlayerDropped", ["v.id":v.id, "v.number":v.number, "payment":v.payment])
      }
      if (v.selectionSteps == true) {
        v.bot = true
        a.remove(v)
        if(v.updatedThisRound.size() > 0){
          v.updatedThisRound = []
        }
        a.addEvent("PlayerReplacedWBot", ["v.id":v.id, "v.number":v.number, "payment":v.payment])
      }
      if (MTurk == true) {
        v.text = c.get("Dropped_Else_MTurk") + g.getSubmitForm(v, v.payment, "Player ${v.number} dropped_inactive", amtSandbox, true)
      }
      if (MTurk == false) {
        v.text = c.get("Dropped_Else_RM", v.number, v.payment)
      }
    }
    if(v.consent == true && v.finished == true){
      if (MTurk == true) {
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Finished_MTurk") + g.getSubmitForm(v, v.payment, "finished", amtSandbox, true)
      }
      if (MTurk == false) {
        v.text = c.get("Payment", String.format("%.2f", (v.payment))) + c.get("Finished_RM", v.number, v.payment)
      }
      a.addEvent("PlayerFinished", ["v.id":v.id, "v.number":v.number, "payment":v.payment])
    }
  })

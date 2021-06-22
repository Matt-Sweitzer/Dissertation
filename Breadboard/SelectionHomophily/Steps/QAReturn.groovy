def getTopic(topicNum){
  if(topicNum == 1){
    return "The Economy"
  }
  if(topicNum == 2){
    return "Terrorism"
  }
  if(topicNum == 3){
    return "Immigration"
  }
  if(topicNum == 4){
    return "Foreign Policy"
  }
  if(topicNum == 5){
    return "Health Care"
  }
  if(topicNum == 6){
    return "Gun Policy"
  }
  if(topicNum == 7){
    return "Social Security"
  }
  if(topicNum == 8){
    return "International Trade Policy"
  }
  if(topicNum == 9){
    return "Education"
  }
  if(topicNum == 10){
    return "The Treatment of Racial/Ethnic Minorities"
  }
  if(topicNum == 11){
    return "Abortion"
  }
  if(topicNum == 12){
    return "The Environment"
  }
  if(topicNum == 13){
    return "The Treatment of LGBTQ Individuals"
  }
  if(topicNum == 14){
    return "Tax Policy"
  }
  if(topicNum == 15){
    return "Crime and Punishment"
  }
}

def getQText(topicNum, versionNum){
  if(topicNum == 1){
    if(versionNum == 0){
      return "Reducing regulations on business is an effective way to address problems with the economy"
    }
    if(versionNum == 1){
      return "The federal government should spend more on retraining programs to help people get jobs"
    }
  }
  if(topicNum == 2){
    if(versionNum == 0){
      return "The U.S. should NOT place a temporary ban on all Muslims entering the country"
    }
    if(versionNum == 1){
      return "The Islamic religion encourages violence more than other religions around the world"
    }
  }
  if(topicNum == 3){
    if(versionNum == 0){
      return "The U.S. should deport all immigrants who are here illegally, regardless of their actions"
    }
    if(versionNum == 1){
      return "The U.S. should create a path to citizenship for immigrants who are here illegally, but who are otherwise model citizens"
    }
  }
  if(topicNum == 4){
    if(versionNum == 0){
      return "Sanctions and diplomacy are more effective than military actions"
    }
    if(versionNum == 1){
      return "The U.S. should not have to contribute more financial resources than other countries to alliance networks like NATO"
    }
  }
  if(topicNum == 5){
    if(versionNum == 0){
      return "The federal government should leave health care to the insurance companies"
    }
    if(versionNum == 1){
      return "The health care system would be improved if the government used a single-payer system like in Europe or Canada"
    }
  }
  if(topicNum == 6){
    if(versionNum == 0){
      return "The lax gun laws in this country contribute to the likelihood of mass shootings like Sandy Hook"
    }
    if(versionNum == 1){
      return "Concealed-carry of guns makes the public safer"
    }
  }
  if(topicNum == 7){
    if(versionNum == 0){
      return "Social security should be privatized"
    }
    if(versionNum == 1){
      return "The federal government should spend more to ensure that social security is viable"
    }
  }
  if(topicNum == 8){
    if(versionNum == 0){
      return "Free trade deals have been a GOOD thing for jobs in the U.S."
    }
    if(versionNum == 1){
      return "Trade agreements harm the U.S. economy"
    }
  }
  if(topicNum == 9){
    if(versionNum == 0){
      return "Parents should be given school vouchers so their kids can attend any school they want"
    }
    if(versionNum == 1){
      return "The U.S. should provide free college tuition for low income students"
    }
  }
  if(topicNum == 10){
    if(versionNum == 0){
      return "Police agencies should receive training to reduce racial discrimination"
    }
    if(versionNum == 1){
      return "Affirmative action policies designed to increase the number of minorities at colleges or places of work should be abolished"
    }
  }
  if(topicNum == 11){
    if(versionNum == 0){
      return "The federal government should not provide funding to agencies which conduct abortions, like Planned Parenthood"
    }
    if(versionNum == 1){
      return "Employers should be required to provide employees with health care plans that cover contraception or birth control at no cost"
    }
  }
  if(topicNum == 12){
    if(versionNum == 0){
      return "The U.S. should invest in renewable sources of energy, like wind or solar"
    }
    if(versionNum == 1){
      return "The Earth's temperature is NOT influenced by human actions"
    }
  }
  if(topicNum == 13){
    if(versionNum == 0){
      return "Businesses should be allowed to refuse service to gay/lesbian couples on religious grounds"
    }
    if(versionNum == 1){
      return "Laws that restrict which bathroom a transgender person can use are discriminatory"
    }
  }
  if(topicNum == 14){
    if(versionNum == 0){
      return "The wealthiest people in this country do not currently pay their fair share in taxes"
    }
    if(versionNum == 1){
      return "Corporate taxes should be reduced to help create jobs"
    }
  }
  if(topicNum == 15){
    if(versionNum == 0){
      return "Sentencing guidelines for routine drug offenses should be strictly enforced"
    }
    if(versionNum == 1){
      return "Life imprisonment is a better punishment than the death penalty"
    }
  }
}

def getAttitudeN(topicNum, versionNum, v){
  if(topicNum == 1){
    if(versionNum == 0){
      return v.EconA
    }
    if(versionNum == 1){
      return v.EconB
    }
  }
  if(topicNum == 2){
    if(versionNum == 0){
      return v.TerrA
    }
    if(versionNum == 1){
      return v.TerrB
    }
  }
  if(topicNum == 3){
    if(versionNum == 0){
      return v.ImmiA
    }
    if(versionNum == 1){
      return v.ImmiB
    }
  }
  if(topicNum == 4){
    if(versionNum == 0){
      return v.ForeA
    }
    if(versionNum == 1){
      return v.ForeB
    }
  }
  if(topicNum == 5){
    if(versionNum == 0){
      return v.HealA
    }
    if(versionNum == 1){
      return v.HealB
    }
  }
  if(topicNum == 6){
    if(versionNum == 0){
      return v.GunsA
    }
    if(versionNum == 1){
      return v.GunsB
    }
  }
  if(topicNum == 7){
    if(versionNum == 0){
      return v.SociA
    }
    if(versionNum == 1){
      return v.SociB
    }
  }
  if(topicNum == 8){
    if(versionNum == 0){
      return v.TradA
    }
    if(versionNum == 1){
      return v.TradB
    }
  }
  if(topicNum == 9){
    if(versionNum == 0){
      return v.EducA
    }
    if(versionNum == 1){
      return v.EducB
    }
  }
  if(topicNum == 10){
    if(versionNum == 0){
      return v.RaceA
    }
    if(versionNum == 1){
      return v.RaceB
    }
  }
  if(topicNum == 11){
    if(versionNum == 0){
      return v.AborA
    }
    if(versionNum == 1){
      return v.AborB
    }
  }
  if(topicNum == 12){
    if(versionNum == 0){
      return v.EnviA
    }
    if(versionNum == 1){
      return v.EnviB
    }
  }
  if(topicNum == 13){
    if(versionNum == 0){
      return v.LgbtA
    }
    if(versionNum == 1){
      return v.LgbtB
    }
  }
  if(topicNum == 14){
    if(versionNum == 0){
      return v.TaxeA
    }
    if(versionNum == 1){
      return v.TaxeB
    }
  }
  if(topicNum == 15){
    if(versionNum == 0){
      return v.CrimA
    }
    if(versionNum == 1){
      return v.CrimB
    }
  }
}

def attConvert(attNum){
  if(attNum == 1){
    return "Strongly Disagree"
  }
  if(attNum == 2){
    return "Disagree"
  }
  if(attNum == 3){
    return "Somewhat Disagree"
  }
  if(attNum == 4){
    return "Neither Agree Nor Disagree"
  }
  if(attNum == 5){
    return "Somewhat Agree"
  }
  if(attNum == 6){
    return "Agree"
  }
  if(attNum == 7){
    return "Strongly Agree"
  }
  if(attNum == 99){
    return " "
  }
}

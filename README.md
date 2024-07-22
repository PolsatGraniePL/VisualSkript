# This version of VisualSkript is under construction.

Wydawało się łatwiejsze do napisania...

Expected action: (Built by figma)<br>
<img src="https://github.com/PolsatGraniePL/VisualSkript/assets/88681446/50618e31-4d04-4a57-b80c-4697ec15f556" width="50%">
```
on join:
  send subtitle "&cWitam" for 10 second
  if player is an operator:
    send "&4Dzień dobry!"
  else if size of all players is higher than 20:
    kick player due to {@kick_info}
  else:
    teleport player to {spawn}
```

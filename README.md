Instruction
-----------
This is the pop game programmed by libgdx engine just like pop star!

TEST
----
1.Test the main game logic, just run the game and judge if it works well.<br>
2.Test game state saved and loaded <br>
> For example:<br>
> 1. on pause screen, if you click the **return to menu button**, so this means the game state should be saved.<br>And the next time you start the game, you should see the last game state(*that is the game state should be loaded!*)<br>
> 2. run game screen, you click **system home button** back to your phone home, game state should also be saved.<br>So even if you kill this application by task explore, you can also restore the last game scene!<br>
> 3. just like item 2, the only different is the current scene is pause screen<br>
> 4. on level over action screen. this may be a little complex, because you should judge whether the game state is level up or failure.<br> When the game state is **level up**, you should record the next new level state. While if the game state is **failure**, game logic will not save the game state!<br>
> 5. failure screen, not save the game state.<br>

3.Test retry function when you failed. That is costing **5 lives** to retry this level, and current score will be the upper level's score(鍗冲垎鏁颁负涓婁竴鍏崇殑鏈�悗鎵�緱鍒�<br>
> In fact, this is also testing game state store. the test game state is best score, last level's score, level, target.<br>
> 1. lose game, retry.<br>
> 2. application unexpected closed, lose game, retry.<br>

SOUND
-----
1.Back music controlled by music button<br>
2.Sound music controlled by sound button<br>
> button click sound<br>
> pop bubble sound<br>
> update level sound, when your score is lager than the target, there should be a cheer sound!<br>
> game over sound<br>
><br>
> honour sound, different honour should play different sound, so there should be at least four sound files<br>
>> good sound<br>
>> cool sound<br>
>> great sound<br>
>> amazing sound<br>
><br>
> props sound
>> bomb sound, player used bomb props<br>
>> hammer sound, player used hammer props <br>
>> color sound, this can be the same as pop bubble sound<br> 
